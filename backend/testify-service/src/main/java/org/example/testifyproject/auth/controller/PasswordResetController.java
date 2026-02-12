package org.example.testifyproject.auth.controller;

import com.example.testify.libraries.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.auth.dto.request.ForgotPasswordRequest;
import org.example.testifyproject.auth.dto.request.ResetPasswordRequest;
import org.example.testifyproject.auth.dto.request.Verify6DigitRequest;
import org.example.testifyproject.auth.service.ForgotPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest req) {
        log.info("Forgot password request received. email={}", req.email());
        forgotPasswordService.sendVerifyCode(req.email());
        return Util.successResponse("Email has been sent to email address");
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@Valid @RequestBody Verify6DigitRequest req) {
        log.info("Verify 8 digit code. email={}", req.email());
        return Util.successResponse(forgotPasswordService.verify6DigitCode(req));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest req) {
        log.info("Reset password. email={}", req.email());
        forgotPasswordService.resetPassword(req);
        return Util.successResponse("");
    }
}
