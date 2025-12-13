package org.example.testifyproject.common.exception.exceptions;

import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.common.exception.BaseException;

public class UserIsExistException extends BaseException {
    public UserIsExistException(String email) {
        super(StatusCode.USER_EXIST, StatusCode.USER_EXIST.getMessage() + email);
    }
}
