package com.example.testify.libraries.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    VERIFY_URL_EMAIL("verify_url_email"),
    VERIFY_8_DIGIT_EMAIL("verify_8_digit_email");

    private final String mailType;
}
