package com.example.testify.service.impl;

import com.example.testify.common.type.MailType;
import com.example.testify_libraries.dtos.requests.VerifyMailRequest;
import com.example.testify.entity.MailTemplate;
import com.example.testify.repository.MailTemplateRepository;
import com.example.testify.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String sendMailForm;

    private final JavaMailSender javaMailSender;
    private final MailTemplateRepository mailTemplateRepository;

    @Override
    public void sendVerifyEmail(VerifyMailRequest verifyMailRequest) throws Exception {
        MailTemplate mailTemplate = mailTemplateRepository.findMailTemplateByTemplateName(MailType.VERIFY_EMAIL.toString())
                .orElseThrow(() -> new Exception(MailType.VERIFY_EMAIL.toString()));

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = getMimeMessageHelper(verifyMailRequest, mimeMessage, mailTemplate);
        helper.setTo(verifyMailRequest.getToAddress());
        helper.setSubject(mailTemplate.getSubject());
        helper.setFrom(sendMailForm);
        javaMailSender.send(mimeMessage);
    }

    private MimeMessageHelper getMimeMessageHelper(VerifyMailRequest verifyMailRequest, MimeMessage mimeMessage, MailTemplate mailTemplate) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String content = String.format(
                mailTemplate.getTemplate(),
                verifyMailRequest.getToAddress(),                   // %s - user name
                verifyMailRequest.getAppName(),                     // %s - app name
                verifyMailRequest.getVerifyLink(),                  // %s - activation link
                verifyMailRequest.getFallbackLink(),
                verifyMailRequest.getFallbackLink(),                // %s - fallback link
                verifyMailRequest.getMinuteExpireTime() + " minute", // %s - expiration time
                verifyMailRequest.getAppName()                      // %s - app name again
        );
        helper.setText(content, true);
        return helper;
    }
}
