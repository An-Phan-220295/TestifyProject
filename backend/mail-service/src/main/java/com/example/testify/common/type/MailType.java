package com.example.testify.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    VERIFY_EMAIL("verify_email");

    private final String mailType;
}
