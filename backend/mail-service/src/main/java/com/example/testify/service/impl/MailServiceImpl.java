package com.example.testify.service.impl;

import com.example.testify.dto.request.MailServiceRequest;
import com.example.testify.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(MailServiceRequest mailServiceRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo("phucanphan1995@gmail.com");
        message.setSubject("Test MailService");
        message.setText("Tessssssssssssssssst");
        javaMailSender.send(message);
    }
}
