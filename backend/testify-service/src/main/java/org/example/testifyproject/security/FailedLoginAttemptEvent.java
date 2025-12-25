package org.example.testifyproject.security;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FailedLoginAttemptEvent extends ApplicationEvent {
    private final String email;
    private final int attempts;


    public FailedLoginAttemptEvent(Object source, String email, int attempts) {
        super(source);
        this.email = email;
        this.attempts = attempts;
    }
}
