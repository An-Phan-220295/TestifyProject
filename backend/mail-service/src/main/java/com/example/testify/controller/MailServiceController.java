package com.example.testify.controller;

import com.example.testify.libraries.common.util.Util;
import com.example.testify.libraries.dtos.requests.VerifyMailRequest;
import com.example.testify.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mail-service")
@RequiredArgsConstructor
@Slf4j
public class MailServiceController {
    private final MailService mailService;

    @PostMapping("/send-verify-email")
    public ResponseEntity<?> sendVerifyEmail(@Valid @RequestBody VerifyMailRequest verifyMailRequest) throws Exception {
        log.info("Received request to send verify email. to={}", verifyMailRequest.getToAddress());
        mailService.sendVerifyEmail(verifyMailRequest);
        return Util.successResponse("");
    }
}
