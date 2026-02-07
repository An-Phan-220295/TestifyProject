package org.example.testifyproject.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email must not be blank")
        String email
) {
}
