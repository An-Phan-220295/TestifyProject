package com.example.testify.service;

import com.example.testify.dto.request.VerifyMailRequest;

public interface MailService {
    void sendVerifyEmail(VerifyMailRequest verifyMailRequest) throws Exception;
}
