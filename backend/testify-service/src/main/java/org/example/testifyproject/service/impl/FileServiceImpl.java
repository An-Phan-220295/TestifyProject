package org.example.testifyproject.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.dtos.request.GenerateUrlFileRequest;
import org.example.testifyproject.service.FileService;
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
@Slf4j
public class FileServiceImpl implements FileService {
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private final S3Presigner s3Presigner;

    @Override
    public Map<String, String> generateURLFileUpload(GenerateUrlFileRequest request) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        log.info("Generate presigned upload URL. email={}, folder={}, fileType={}",
                email,
                request.getFolder(),
                request.getFileType());

        String key = request.getFolder().getFolder()
                + email + "_"
                + Instant.now() + request.getFileType().getType();

        try {
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
            data.put("upload-url",
                    s3Presigner.presignPutObject(presignRequest).url().toString());

            log.info("Presigned upload URL generated successfully. email={}, bucket={}",
                    email, bucketName);

            return data;

        } catch (Exception e) {
            log.error("Failed to generate presigned upload URL. email={}, bucket={}",
                    email, bucketName, e);
            throw e;
        }
    }


    @Override
    public String getFileFromS3(String key) {

        log.info("Generate presigned download URL. bucket={}", bucketName);

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest =
                    GetObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(5))
                            .getObjectRequest(getObjectRequest)
                            .build();

            return s3Presigner.presignGetObject(presignRequest)
                    .url()
                    .toString();

        } catch (Exception e) {
            log.error("Failed to generate presigned download URL. bucket={}",
                    bucketName, e);
            throw e;
        }
    }

}
