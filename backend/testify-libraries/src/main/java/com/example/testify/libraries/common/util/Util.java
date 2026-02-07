package com.example.testify.libraries.common.util;


import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.dtos.responses.BaseResponse;
import org.springframework.http.ResponseEntity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Random;
import java.util.UUID;

public class Util {
    private static final String HMAC_ALGO = "HmacSHA256";

    public static ResponseEntity<?> successResponse(Object data) {
        BaseResponse baseResponse = BaseResponse.builder()
                .status(StatusCode.SUCCESS.getHttpCode())
                .message(StatusCode.SUCCESS.getMessage())
                .data(data)
                .timeStamp(Instant.now()).build();
        return ResponseEntity.ok().body(baseResponse);
    }

    public static String generateRandom8DigitNumber() {
        Random random = new Random();
        int number = random.nextInt(999999);

        return String.format("%06d", number);
    }

    public static String hashCode(String data, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec key = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    HMAC_ALGO
            );
            mac.init(key);

            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(raw);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to hash reset value", e);
        }
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
