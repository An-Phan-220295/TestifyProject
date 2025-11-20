package com.example.testify.service;

import com.example.testify.dto.request.MailServiceRequest;

public interface MailService {
    void sendEmail(MailServiceRequest mailServiceRequest);
}
