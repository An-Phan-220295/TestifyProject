package org.example.testifyproject.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.infrastructure.redis.RedisService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    private final ApplicationEventPublisher applicationEventPublisher;
    public CustomDaoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            RedisService redisService,
            ApplicationEventPublisher eventPublisher
    ) {

        super(userDetailsService);
        this.passwordEncoder = passwordEncoder;
        this.setPasswordEncoder(passwordEncoder);
        this.redisService = redisService;
        this.applicationEventPublisher = eventPublisher;
        log.info("CustomDaoAuthenticationProvider CREATED");
    }


    private static final String REDIS_FAILED_LOGIN_ATTEMPTS_PREFIX = "FAILED_LOGIN_ATTEMPTS:";
    private final int REDIS_FAILED_LOGIN_ATTEMPTS_DURATION = 120;


    private int increaseFailedAttempts(String email) {
        String key = REDIS_FAILED_LOGIN_ATTEMPTS_PREFIX + email;
        Integer attemptCount = redisService.get(key, Integer.class);

        int newAttempts = (attemptCount == null) ? 1 : attemptCount + 1;

        Duration ttl = (attemptCount == null)
                ? Duration.ofMinutes(REDIS_FAILED_LOGIN_ATTEMPTS_DURATION)
                : redisService.checkTTL(key, TimeUnit.MINUTES);

        redisService.set(key, newAttempts, ttl);
        return newAttempts;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {

        String rawPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            String email = userDetails.getUsername();
            int attempts = increaseFailedAttempts(email);

            log.warn("Authentication failed. email={}, attempts={}", email, attempts);

            applicationEventPublisher.publishEvent(new FailedLoginAttemptEvent(this, email, attempts));

            throw new BadCredentialsException("Invalid credentials");
        }
    }

}

