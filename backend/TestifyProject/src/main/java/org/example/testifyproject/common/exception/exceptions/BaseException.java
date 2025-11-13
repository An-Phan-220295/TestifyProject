package org.example.testifyproject.common.exception.exceptions;

import lombok.Getter;
import org.example.testifyproject.entity.enums.StatusCode;

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
