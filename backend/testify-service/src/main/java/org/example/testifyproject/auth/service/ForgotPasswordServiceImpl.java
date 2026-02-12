package org.example.testifyproject.auth.service;

import com.example.testify.libraries.common.util.Util;
import com.example.testify.libraries.dtos.requests.Verify8DigitMailRequest;
import lombok.RequiredArgsConstructor;
import org.example.testifyproject.auth.dto.request.ResetPasswordRequest;
import org.example.testifyproject.auth.dto.request.Verify6DigitRequest;
import org.example.testifyproject.common.exception.InvalidVerifyEmailTokenException;
import org.example.testifyproject.infrastructure.mail.MailServiceAdapter;
import org.example.testifyproject.infrastructure.redis.RedisService;
import org.example.testifyproject.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final UserService userService;
    private final MailServiceAdapter mailServiceAdapter;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.application.name}")
    private String appName;
    @Value("${app.security.reset-secret}")
    private String secret;
    private final String VERIFY_CODE_FORGOT_PASSWORD_REGEX = "verifyCodeForgotPassword";
    private final String RESET_TOKEN_FORGOT_PASSWORD_REGEX = "resetTokenForgotPassword";

    @Override
    public void sendVerifyCode(String email) {
        //Check email existing
        if (!userService.userIsExist(email)) {
            throw new UsernameNotFoundException("Invalid user");
        }

        //Generate verify code
        String verifyCode = Util.generateRandom8DigitNumber();

        //Save verify code to Redis
        redisService.set(VERIFY_CODE_FORGOT_PASSWORD_REGEX + email
                , Util.hashCode(verifyCode, secret), Duration.ofMinutes(15));

        //Send verify code to email address
        Verify8DigitMailRequest request = Verify8DigitMailRequest.builder()
                .appName(appName).toAddress(email).verifyCode(verifyCode).minuteExpireTime(15).build();
        mailServiceAdapter.send8DigitVerifyEmail(request);
    }

    @Override
    public String verify6DigitCode(Verify6DigitRequest req) {
        if (!userService.userIsExist(req.email())) {
            throw new UsernameNotFoundException("Invalid user");
        }

        String hashCode = Util.hashCode(req.code(), secret);
        String redisCode = redisService.get(VERIFY_CODE_FORGOT_PASSWORD_REGEX + req.email(), String.class);

        if (!hashCode.equals(redisCode)) {
            throw new InvalidVerifyEmailTokenException();
        }

        String resetToken = Util.generateToken();
        redisService.set(RESET_TOKEN_FORGOT_PASSWORD_REGEX + req.email()
                , Util.hashCode(resetToken, secret), Duration.ofMinutes(15));
        redisService.delete(VERIFY_CODE_FORGOT_PASSWORD_REGEX + req.email());

        return resetToken;
    }

    @Override
    public void resetPassword(ResetPasswordRequest req) {
        if (!userService.userIsExist(req.email())) {
            throw new UsernameNotFoundException("Invalid user");
        }

        String hashResetToken = Util.hashCode(req.resetToken(), secret);
        String redisToken = redisService.get(RESET_TOKEN_FORGOT_PASSWORD_REGEX + req.email(), String.class);

        if (!hashResetToken.equals(redisToken)) {
            throw new InvalidVerifyEmailTokenException();
        }

        userService.saveNewPassword(passwordEncoder.encode(req.newPassword()), req.email());
        redisService.delete(RESET_TOKEN_FORGOT_PASSWORD_REGEX + req.email());
    }
}
