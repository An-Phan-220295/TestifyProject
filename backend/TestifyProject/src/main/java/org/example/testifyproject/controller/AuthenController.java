package org.example.testifyproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testifyproject.common.util.Util;
import org.example.testifyproject.dtos.authen.AccessTokenOnly;
import org.example.testifyproject.dtos.authen.LoginRequest;
import org.example.testifyproject.dtos.authen.RefreshRequest;
import org.example.testifyproject.dtos.authen.TokenResponse;
import org.example.testifyproject.dtos.request.SignupRequest;
import org.example.testifyproject.dtos.response.BaseResponse;
import org.example.testifyproject.entity.enums.StatusCode;
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

import java.time.Instant;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenController {
    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtUtil;
    private final UserService userService;
    private final Util util;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        AppUserDetails user = (AppUserDetails) auth.getPrincipal();
        String access = jwtUtil.generateAccessToken(user);
        String refresh = jwtUtil.generateAndSaveRefreshToken(user);

        return ResponseEntity.ok(new TokenResponse(access, refresh));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        boolean result = jwtUtil.logout(authHeader);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Logout failed due to server error"));
        }

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req) {
        String refreshToken = req.getRefreshToken();

        if (!jwtUtil.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired refresh token"));
        }

        String email = jwtUtil.validateAndExtractUsername(refreshToken);

        AppUserDetails user = (AppUserDetails) userDetailsService.loadUserByUsername(email);
        String newAccess = jwtUtil.generateAccessToken(user);

        return ResponseEntity.ok(new AccessTokenOnly(newAccess));
    }

    @PostMapping("signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return util.successResponse(userService.saveNewUser(signupRequest));
    }
}
