package com.example.testify.libraries.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusCode {
    // --- 2xx Success ---
    SUCCESS(HttpStatus.OK, "Operation completed successfully"),
    CREATED(HttpStatus.CREATED, "Resource created successfully"),

    // --- 4xx Client Errors ---
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    LOCKED(HttpStatus.LOCKED, "Account is locked"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "Resource already exists"),
    USER_EXIST(HttpStatus.CONFLICT, "User already exists"),
    INVALID_VERIFY_EMAIL_TOKEN(HttpStatus.BAD_REQUEST, "Invalid verify email token"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password"),

    // --- 5xx Server Errors ---
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service temporarily unavailable");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpCode() {
        return httpStatus.value();
    }
}
