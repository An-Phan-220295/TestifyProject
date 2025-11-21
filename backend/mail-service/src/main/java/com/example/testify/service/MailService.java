package com.example.testify.service;

import com.example.testify.libraries.dtos.requests.VerifyMailRequest;

public interface MailService {
    void sendVerifyEmail(VerifyMailRequest verifyMailRequest) throws Exception;
}
