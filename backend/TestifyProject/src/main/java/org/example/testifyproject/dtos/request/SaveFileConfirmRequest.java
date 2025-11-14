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
public class SaveFileConfirmRequest {
    @NotNull(message = "Key is required")
    private String key;

    @NotNull(message = "S3Folder is required")
    private S3Folder folder;
}
