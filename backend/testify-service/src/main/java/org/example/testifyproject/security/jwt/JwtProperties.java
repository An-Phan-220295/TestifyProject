package org.example.testifyproject.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String secret;
    private String issuer;
    private int accessTokenMinutes;
    private int refreshTokenDays;
}
