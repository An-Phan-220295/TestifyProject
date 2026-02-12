package com.example.testify.service.impl;

import com.example.testify.common.exception.exceptions.TemplateNotFoundException;
import com.example.testify.common.type.MailType;
import com.example.testify.entity.MailTemplate;
import com.example.testify.libraries.dtos.requests.Verify8DigitMailRequest;
import com.example.testify.libraries.dtos.requests.VerifyURLMailRequest;
import com.example.testify.repository.MailTemplateRepository;
import com.example.testify.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    @Value("${spring.mail.username}")
    private String sendMailForm;

    private final JavaMailSender javaMailSender;
    private final MailTemplateRepository mailTemplateRepository;

    @Override
    public void sendURLVerifyEmail(VerifyURLMailRequest verifyMailRequest) throws MessagingException {
        log.info("Start sending verify email to={}", verifyMailRequest.getToAddress());
        MailTemplate mailTemplate = mailTemplateRepository.findMailTemplateByTemplateName(MailType.VERIFY_URL_EMAIL.toString())
                .orElseThrow(() -> {
                    log.warn("Mail template not found. type={}", MailType.VERIFY_URL_EMAIL);
                    return new TemplateNotFoundException(MailType.VERIFY_URL_EMAIL.toString());
                });

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = getMimeMessageHelper(
                    verifyMailRequest, mimeMessage, mailTemplate
            );

            helper.setTo(verifyMailRequest.getToAddress());
            helper.setSubject(mailTemplate.getSubject());
            helper.setFrom(sendMailForm);

            javaMailSender.send(mimeMessage);

            log.info("Verify email sent successfully to={}", verifyMailRequest.getToAddress());
        } catch (MessagingException ex) {
            log.error("Failed to send verify email to={}", verifyMailRequest.getToAddress(), ex);
            throw ex;
        }
    }

    @Override
    public void send8DigitVerifyEmail(Verify8DigitMailRequest request) throws MessagingException {
        log.info("Start sending verify email to={}", request.getToAddress());
        MailTemplate mailTemplate = mailTemplateRepository.findMailTemplateByTemplateName(MailType.VERIFY_8_DIGIT_EMAIL.toString())
                .orElseThrow(() -> {
                    log.warn("Mail template not found. type={}", MailType.VERIFY_8_DIGIT_EMAIL);
                    return new TemplateNotFoundException(MailType.VERIFY_8_DIGIT_EMAIL.toString());
                });

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            log.debug("Building email content for to={}", request.getToAddress());

            MimeMessageHelper helper = getMimeMessageHelper(request, mimeMessage, mailTemplate);

            helper.setTo(request.getToAddress());
            helper.setSubject(mailTemplate.getSubject());
            helper.setFrom(sendMailForm);

            javaMailSender.send(mimeMessage);

            log.info("Verify email sent successfully to={}", request.getToAddress());
        } catch (MessagingException ex) {
            log.error("Failed to send verify email to={}", request.getToAddress(), ex);
            throw ex;
        }
    }

    private static MimeMessageHelper getMimeMessageHelper(Verify8DigitMailRequest request, MimeMessage mimeMessage, MailTemplate mailTemplate) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String content = String.format(
                mailTemplate.getTemplate(),
                request.getToAddress(),                   // %s - user name
                request.getAppName(),                     // %s - app name
                request.getVerifyCode(),
                request.getMinuteExpireTime() + " minute", // %s - expiration time
                request.getAppName()                      // %s - app name again
        );
        helper.setText(content, true);
        return helper;
    }

    private MimeMessageHelper getMimeMessageHelper(VerifyURLMailRequest verifyMailRequest
            , MimeMessage mimeMessage, MailTemplate mailTemplate) throws MessagingException {
        log.debug("Building email content for to={}", verifyMailRequest.getToAddress());
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
