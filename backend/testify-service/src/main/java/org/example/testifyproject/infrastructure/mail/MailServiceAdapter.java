package org.example.testifyproject.infrastructure.mail;

import com.example.testify.libraries.dtos.requests.VerifyMailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "mail-service", url = "http://localhost:8081")
public interface MailServiceAdapter {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/mail-service/send-verify-email")
    ResponseEntity<String> sendEmail(@RequestBody VerifyMailRequest verifyMailRequest);
}
