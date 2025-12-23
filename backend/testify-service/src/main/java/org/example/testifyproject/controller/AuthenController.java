package org.example.testifyproject.controller;

import com.example.testify.libraries.common.util.Util;
import com.example.testify.libraries.dtos.requests.VerifyMailRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.adapter.MailServiceAdapter;
import org.example.testifyproject.dtos.authen.AccessTokenOnly;
import org.example.testifyproject.dtos.authen.LoginRequest;
import org.example.testifyproject.dtos.authen.RefreshRequest;
import org.example.testifyproject.dtos.authen.TokenResponse;
import org.example.testifyproject.dtos.request.SignupRequest;
import org.example.testifyproject.security.AppUserDetails;
import org.example.testifyproject.security.CustomUserDetailsService;
import org.example.testifyproject.security.jwt.JwtService;
import org.example.testifyproject.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthenController {
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtUtil;
    private final UserService userService;
    private final MailServiceAdapter mailServiceAdapter;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        log.info("Login request received. email={}", req.getEmail());
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        AppUserDetails user = (AppUserDetails) auth.getPrincipal();
        String access = jwtUtil.generateAccessToken(user);
        String refresh = jwtUtil.generateAndSaveRefreshToken(user);
        log.info("Login successful. email={}", user.getEmail());

        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        log.info("Logout request received");
        boolean result = jwtUtil.logout(authHeader);

        if (!result) {
            log.warn("Logout failed due to server error");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Logout failed due to server error"));
        }
        log.info("Logout successful");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req) {
        log.info("Refresh token request received");

        String refreshToken = req.getRefreshToken();

        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            log.warn("Invalid or expired refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired refresh token"));
        }

        String email = jwtUtil.validateAndExtractUsername(refreshToken);

        AppUserDetails user = (AppUserDetails) userDetailsService.loadUserByUsername(email);
        String newAccess = jwtUtil.generateAccessToken(user);
        log.info("Refresh token successful. email={}", email);

        return ResponseEntity.ok(new AccessTokenOnly(newAccess));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
        log.info("Signup request received. email={}", signupRequest.getEmail());

        userService.saveNewUser(signupRequest);

        VerifyMailRequest verifyMailRequest = userService.generateConfirmURL(signupRequest, request);

        String result = mailServiceAdapter.sendEmail(verifyMailRequest).getBody();

        log.info("Signup successful, verification email sent. email={}", signupRequest.getEmail());

        return Util.successResponse(result);
    }

    @GetMapping("/verify-account/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable String token) {
        log.info("Verify account request received");
        userService.verifyAccount(token);

        log.info("Account verified successfully");
        return Util.successResponse("Verified success");
    }

    @GetMapping("/fallback-verify-account/{token}")
    public ResponseEntity<?> fallbackVerifyAccount(@PathVariable String token) {
        log.info("Fallback verify account request received");
        userService.verifyAccount(token);

        log.info("Account verified successfully via fallback");
        return Util.successResponse("Verified success");
    }
}
