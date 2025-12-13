package org.example.testifyproject.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.testifyproject.entity.enums.FileType;
import org.example.testifyproject.entity.enums.S3Folder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SaveAvatarConfirmRequest {
    @NotNull(message = "Key is required")
    private String key;
}
