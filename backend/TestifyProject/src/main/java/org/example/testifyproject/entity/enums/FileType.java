package org.example.testifyproject.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FileType {
    PNG(".pnj", "image/png"),
    JPG(".jpg", "image/jpg");

    private final String type;
    private final String contentType;
}
