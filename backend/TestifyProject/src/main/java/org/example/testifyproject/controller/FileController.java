package org.example.testifyproject.controller;

import com.example.testify_libraries.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testifyproject.dtos.request.GenerateUrlFileRequest;
import org.example.testifyproject.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
public class FileController {
    private final FileService fileService;

    @PostMapping("/generate-url")
    public ResponseEntity<?> generateURLFileUpload(@Valid @RequestBody GenerateUrlFileRequest request) {
        return Util.successResponse(fileService.generateURLFileUpload(request));
    }
}
