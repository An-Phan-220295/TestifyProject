package com.example.testify.dto.request;

import com.example.testify.common.type.MailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyMailRequest {
    @NotEmpty
    private String appName;

    @NotEmpty
    @Email
    private String toAddress;

    @NotNull
    private String verifyLink;

    @NotNull
    private String fallbackLink;

    @NotNull
    @Min(value = 0)
    private int minuteExpireTime;
}
