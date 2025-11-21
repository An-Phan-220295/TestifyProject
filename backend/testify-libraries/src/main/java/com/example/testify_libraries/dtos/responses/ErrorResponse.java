package com.example.testify_libraries.dtos.responses;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private Object message;
    private String path;
    private Instant timestamp;
}
