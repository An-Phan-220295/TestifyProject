package com.example.testify.config.mail;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MailServiceConfig {
    private final MailSenderProperties mailSenderProperties;

    @PostConstruct
    void init() {
        if (mailSenderProperties.getUsername() == null || mailSenderProperties.getPassword() == null) {
            throw new IllegalArgumentException("Username or password is null, please check the env");
        }
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailSenderProperties.getHost());
        javaMailSender.setPort(mailSenderProperties.getPort());
        javaMailSender.setUsername(mailSenderProperties.getUsername());
        javaMailSender.setPassword(mailSenderProperties.getPassword());
        javaMailSender.setProtocol(mailSenderProperties.getProtocol());

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailSenderProperties.getProperties().getMail().getSmtp().isAuth());
        props.put("mail.smtp.starttls.enable", mailSenderProperties.getProperties().getMail().getSmtp().getStarttls().isEnable());
        props.put("mail.debug", "true");

        return javaMailSender;
    }
}
