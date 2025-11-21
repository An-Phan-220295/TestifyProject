package com.example.testify_libraries.common.exception;

import com.example.testify_libraries.common.enums.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final StatusCode codes;

    public BaseException(StatusCode codes) {
        this.codes = codes;
    }

    public BaseException(StatusCode codes, String customMessage) {
        super(customMessage);
        this.codes = codes;
    }
}
