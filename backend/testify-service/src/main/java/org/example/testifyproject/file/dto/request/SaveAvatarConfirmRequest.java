package org.example.testifyproject.file.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SaveAvatarConfirmRequest {
    @NotNull(message = "Key is required")
    private String key;
}
