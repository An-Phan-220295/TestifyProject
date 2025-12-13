package org.example.testifyproject.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.testifyproject.entity.enums.Gender;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignupResponse {
    private String email;
    private String phoneNumber;
    private String fullName;
    private LocalDate dob;
    private String avatarUrl;
    private Gender gender;
    private String roleType;
}
