package org.example.testifyproject.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record Verify6DigitRequest(
        @Email(message = "Email is invalid")
        @NotBlank(message = "Email must not be blank")
        String email,

        @NotBlank(message = "Verify code must not be blank")
        @Pattern(
                regexp = "^\\d{6}$",
                message = "Verify code must be exactly 6 digits"
        )
        String code
) {
}
