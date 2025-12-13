package org.example.testifyproject.service;

import org.example.testifyproject.dtos.request.GenerateUrlFileRequest;

import java.util.Map;

public interface FileService {
    Map<String, String> generateURLFileUpload(GenerateUrlFileRequest request);

    String getFileFromS3(String request);
}
