package org.example.testifyproject.file.service;

import org.example.testifyproject.file.dto.request.GenerateUrlFileRequest;

import java.util.Map;

public interface FileService {
    Map<String, String> generateURLFileUpload(GenerateUrlFileRequest request);

    String getFileFromS3(String request);
}
