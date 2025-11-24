package org.example.testifyproject.common.exception.exceptions;

import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.common.exception.BaseException;

public class InvalidVerifyEmailTokenException extends BaseException {
    public InvalidVerifyEmailTokenException() {
        super(StatusCode.INVALID_VERIFY_EMAIL_TOKEN, StatusCode.INVALID_VERIFY_EMAIL_TOKEN.getMessage());
    }
}
