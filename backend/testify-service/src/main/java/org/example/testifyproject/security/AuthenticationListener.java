package org.example.testifyproject.security;

import lombok.RequiredArgsConstructor;
import org.example.testifyproject.common.util.RedisService;
import org.example.testifyproject.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationListener {
    private final RedisService redisService;
    private final UserService userService;
    private static final String FAILED_LOGIN_PREFIX = "FAILED_LOGIN_ATTEMPTS:";
    private static final int MAX_FAILED_ATTEMPTS = 6;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();
        redisService.delete(FAILED_LOGIN_PREFIX + email);
    }

    @EventListener
    public void onFailedLogin(FailedLoginAttemptEvent event) {
        if (event.getAttempts() == MAX_FAILED_ATTEMPTS) {
            userService.lockAccount(event.getEmail());
        }
    }

}
