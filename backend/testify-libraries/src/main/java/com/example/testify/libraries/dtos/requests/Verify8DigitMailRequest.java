package com.example.testify.libraries.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Verify8DigitMailRequest {
    @NotEmpty
    private String appName;

    @NotEmpty
    @Email
    private String toAddress;

    @NotNull
    private String verifyCode;

    @NotNull
    @Min(value = 0)
    private int minuteExpireTime;
}
