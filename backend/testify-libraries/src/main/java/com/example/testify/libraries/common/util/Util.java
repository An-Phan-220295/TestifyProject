package com.example.testify.libraries.common.util;


import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.dtos.responses.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class Util {
    public static ResponseEntity<?> successResponse(Object data) {
        BaseResponse baseResponse = BaseResponse.builder()
                .status(StatusCode.SUCCESS.getHttpCode())
                .message(StatusCode.SUCCESS.getMessage())
                .data(data)
                .timeStamp(Instant.now()).build();
        return ResponseEntity.ok().body(baseResponse);
    }
}
