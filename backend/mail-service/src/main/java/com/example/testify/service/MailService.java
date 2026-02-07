package com.example.testify.service;


import com.example.testify.libraries.dtos.requests.Verify8DigitMailRequest;
import com.example.testify.libraries.dtos.requests.VerifyURLMailRequest;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendURLVerifyEmail(VerifyURLMailRequest verifyMailRequest) throws Exception;

    void send8DigitVerifyEmail(Verify8DigitMailRequest request) throws MessagingException;
}
