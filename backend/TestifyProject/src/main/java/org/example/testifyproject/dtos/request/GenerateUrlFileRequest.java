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
public class GenerateUrlFileRequest {
    @NotNull(message = "S3 bucket is required")
    private S3Folder folder;

    @NotNull(message = "Filetype is required")
    private FileType fileType;
}
