package org.example.testifyproject.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.testifyproject.common.exception.exceptions.BaseException;
import org.example.testifyproject.dtos.response.ErrorResponse;
import org.example.testifyproject.entity.enums.StatusCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    ResponseEntity<?> baseExceptionHandle(BaseException ex, HttpServletRequest http) {
        return ResponseEntity.status(ex.getCodes().getHttpStatus())
                .body(setErrorResponse(ex.getCodes(), ex.getCodes().getMessage(), http.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest http) {
        return ResponseEntity.internalServerError()
                .body(setErrorResponse(StatusCode.INTERNAL_ERROR, ex.getMessage(), http.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest http) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        return ResponseEntity.badRequest()
                .body(setErrorResponse(StatusCode.BAD_REQUEST, errors, http.getRequestURI()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleEnumWrongValue(HttpMessageNotReadableException ex, HttpServletRequest http) {
        String message = "";
        if (ex.getCause() instanceof InvalidFormatException ife && ife.getTargetType().isEnum()) {
            message = String.format(
                    "Invalid value '%s' for field '%s'. Accepted values: %s",
                    ife.getValue(),
                    ife.getPath().get(0).getFieldName(),
                    Arrays.toString(ife.getTargetType().getEnumConstants())
            );
        }

        return ResponseEntity.internalServerError()
                .body(setErrorResponse(StatusCode.INTERNAL_ERROR, message, http.getRequestURI()));
    }

    private ErrorResponse setErrorResponse(StatusCode statusCode, Object message, String path) {
        return ErrorResponse.builder()
                .status(statusCode.getHttpCode())
                .error(statusCode.getMessage())
                .message(message)
                .path(path)
                .timestamp(Instant.now())
                .build();
    }
}
