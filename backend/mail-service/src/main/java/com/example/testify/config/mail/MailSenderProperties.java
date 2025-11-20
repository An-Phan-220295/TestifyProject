package com.example.testify.config.mail;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail")
public class MailSenderProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String protocol;

    private NestedProperties  properties;

    @Getter
    @Setter
    public static class NestedProperties  {
        private Mail mail;
    }

    @Getter
    @Setter
    public static class Mail {
        private Smtp smtp;
    }

    @Getter
    @Setter
    public static class Smtp {
        private boolean auth;
        private Starttls starttls;
    }

    @Getter
    @Setter
    public static class Starttls {
        private boolean enable;
    }
}
