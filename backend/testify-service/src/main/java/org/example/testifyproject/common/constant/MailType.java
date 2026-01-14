package org.example.testifyproject.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailType {
    VERIFY_EMAIL("verify_email");

    private final String mailType;
}
