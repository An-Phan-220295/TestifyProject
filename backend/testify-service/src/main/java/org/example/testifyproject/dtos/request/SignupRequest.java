package org.example.testifyproject.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.testifyproject.entity.enums.Gender;
import org.example.testifyproject.entity.enums.RoleType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignupRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^(\\+?84|0)\\d{9}$",
            message = "Phone number must start with 0 or +84 and have 10 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "Full name is required")
    @Size(max = 50)
    private String fullName;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$",
            message = "Password must contain upper, lower, number, special char (8–15 chars)"
    )
    private String password;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Role is required")
    private RoleType roleType;
}
