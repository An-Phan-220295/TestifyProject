package org.example.testifyproject.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email must not be blank")
        String email,

        @NotBlank(message = "Verify code must not be blank")
        String resetToken,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
                message = "Password must contain upper, lower, number, special char (8–15 chars)"
        )
        String newPassword
) {
}
