package org.example.testifyproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testifyproject.dtos.request.DownloadFileRequest;
import org.example.testifyproject.dtos.request.GenerateUrlFileRequest;
import org.example.testifyproject.service.FileService;
import org.example.testifyproject.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Presigner s3Presigner;

    @Override
    public Map<String, String> generateURLFileUpload(GenerateUrlFileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = request.getFolder().getFolder() + email + "_"
                + Instant.now().toString() + request.getFileType().getType();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(request.getFileType().getContentType())
                .build();

        PutObjectPresignRequest presignRequest =
                PutObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .putObjectRequest(objectRequest)
                        .build();

        Map<String, String> data = new HashMap<>();
        data.put("key", key);
        data.put("upload-url", s3Presigner.presignPutObject(presignRequest).url().toString());

        return data;
    }

    @Override
    public String getFileFromS3(String request) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(request)
                .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(5))
                        .getObjectRequest(getObjectRequest)
                        .build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
