package org.example.testifyproject.service;

import org.example.testifyproject.dtos.request.GenerateUrlFileRequest;
import org.example.testifyproject.dtos.request.SaveFileConfirmRequest;
import org.example.testifyproject.dtos.response.SignupResponse;

import java.util.Map;

public interface FileService {
    Map<String,String> generateURLFileUpload(GenerateUrlFileRequest request);

    SignupResponse saveFileSuccess(SaveFileConfirmRequest request);
}
