package com.example.testify.controller;

import com.example.testify.dto.request.VerifyMailRequest;
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

    @PostMapping("/send-verify-email")
    public ResponseEntity<?> sendVerifyEmail(@Valid @RequestBody VerifyMailRequest verifyMailRequest) throws Exception {
        mailService.sendVerifyEmail(verifyMailRequest);
        return new ResponseEntity<>("Operation completed successfully", HttpStatusCode.valueOf(200));
    }
}
