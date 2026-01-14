package org.example.testifyproject.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum S3Folder {
    AVATAR("avatars/");

    private final String folder;
}
