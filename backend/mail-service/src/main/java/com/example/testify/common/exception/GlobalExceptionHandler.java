package com.example.testify.common.exception;

import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.common.exception.BaseException;
import com.example.testify.libraries.dtos.responses.ErrorResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Hidden
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    ResponseEntity<?> baseExceptionHandle(BaseException ex, HttpServletRequest http) {
        return ResponseEntity.status(ex.getCodes().getHttpStatus()).body(setErrorResponse(ex.getCodes(), ex.getCodes().getMessage(), http.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex, HttpServletRequest http) {
        return ResponseEntity.internalServerError().body(setErrorResponse(StatusCode.INTERNAL_ERROR, ex.getMessage(), http.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest http) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(setErrorResponse(StatusCode.BAD_REQUEST, errors, http.getRequestURI()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<?> handleMessagingException(MessagingException ex, HttpServletRequest http) {
        return ResponseEntity.internalServerError().body(setErrorResponse(StatusCode.INTERNAL_ERROR, ex.getMessage(), http.getRequestURI()));
    }

    private ErrorResponse setErrorResponse(StatusCode statusCode, Object message, String path) {
        return ErrorResponse.builder().status(statusCode.getHttpCode()).error(statusCode.getMessage()).message(message).path(path).timestamp(Instant.now()).build();
    }
}
