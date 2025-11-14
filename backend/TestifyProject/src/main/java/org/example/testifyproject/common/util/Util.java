package org.example.testifyproject.common.util;

import org.example.testifyproject.dtos.response.BaseResponse;
import org.example.testifyproject.entity.enums.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class Util {
    public ResponseEntity<?> successResponse(Object data) {
        BaseResponse baseResponse = BaseResponse.builder()
                .status(StatusCode.SUCCESS.getHttpCode())
                .message(StatusCode.SUCCESS.getMessage())
                .data(data)
                .timeStamp(Instant.now()).build();
        return ResponseEntity.ok().body(baseResponse);
    }
}
