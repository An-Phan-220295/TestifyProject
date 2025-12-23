package org.example.testifyproject.controller;

import com.example.testify.libraries.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("/generate-url")
    public ResponseEntity<?> generateURLFileUpload(@Valid @RequestBody GenerateUrlFileRequest request) {
        log.info("Generate file upload URL request received. fileName={}, fileType={}",
                request.getFolder(), request.getFileType());

        Object result = fileService.generateURLFileUpload(request);

        log.info("Generate file upload URL successfully");

        return Util.successResponse(result);
    }
}
