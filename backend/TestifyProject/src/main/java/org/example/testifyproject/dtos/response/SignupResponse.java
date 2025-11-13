package org.example.testifyproject.dtos.response;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.testifyproject.entity.enums.Gender;
import org.example.testifyproject.entity.enums.RoleType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignupResponse {
    private String email;
    private String phoneNumber;
    private String fullName;
    private String avatarUrl;
    private LocalDate dob;
    private Gender gender;
    private String roleType;
}
