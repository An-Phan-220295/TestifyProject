package com.example.testify_libraries.dtos.responses;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private int status;
    private String message;
    private Object data;
    private Instant timeStamp;
}
