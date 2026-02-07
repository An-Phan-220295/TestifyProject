package org.example.testifyproject.infrastructure.mail;

import com.example.testify.libraries.dtos.requests.Verify8DigitMailRequest;
import com.example.testify.libraries.dtos.requests.VerifyURLMailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "mail-service", url = "http://localhost:8081")
public interface MailServiceAdapter {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/mail-service/send-url-verify-email")
    ResponseEntity<String> sendURLVerifyEmail(@RequestBody VerifyURLMailRequest verifyMailRequest);

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/mail-service/send-8-digit-verify-email")
    ResponseEntity<String> send8DigitVerifyEmail(@RequestBody Verify8DigitMailRequest request);
}
