package org.example.testifyproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testifyproject.common.util.Util;
import org.example.testifyproject.dtos.request.GenerateUrlFileRequest;
import org.example.testifyproject.dtos.request.SaveFileConfirmRequest;
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
    private final Util util;

    @PostMapping("/generate-url")
    public ResponseEntity<?> generateURLFileUpload(@Valid @RequestBody GenerateUrlFileRequest request) {
        return util.successResponse(fileService.generateURLFileUpload(request));
    }

    @PostMapping("/save-file-completed")
    public ResponseEntity<?> saveFileCompleted(@Valid @RequestBody SaveFileConfirmRequest request) {
        return util.successResponse(fileService.saveFileSuccess(request));
    }
}
