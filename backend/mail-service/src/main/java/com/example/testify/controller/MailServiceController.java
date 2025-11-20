package com.example.testify.controller;

import com.example.testify.dto.request.MailServiceRequest;
import com.example.testify.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mail-service")
@RequiredArgsConstructor
public class MailServiceController {
    private final MailService mailService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody MailServiceRequest mailServiceRequest) {
        mailService.sendEmail(mailServiceRequest);
        return new ResponseEntity<>("mailService.sendEmail()", HttpStatusCode.valueOf(200));
    }
}
