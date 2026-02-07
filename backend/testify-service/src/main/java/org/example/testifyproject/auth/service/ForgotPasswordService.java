package org.example.testifyproject.auth.service;

import org.example.testifyproject.auth.dto.request.ResetPasswordRequest;
import org.example.testifyproject.auth.dto.request.Verify6DigitRequest;

public interface ForgotPasswordService {
    void sendVerifyCode(String email);

    String verify6DigitCode(Verify6DigitRequest req);

    void resetPassword(ResetPasswordRequest req);
}
