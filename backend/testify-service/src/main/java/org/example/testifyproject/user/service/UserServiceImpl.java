package org.example.testifyproject.user.service;

import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.common.exception.BaseException;
import com.example.testify.libraries.dtos.requests.VerifyMailRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.common.exception.InvalidVerifyEmailTokenException;
import org.example.testifyproject.common.exception.RoleNotFoundException;
import org.example.testifyproject.common.exception.UserIsExistException;
import org.example.testifyproject.common.util.mapper.UserMapper;
import org.example.testifyproject.infrastructure.redis.RedisService;
import org.example.testifyproject.file.dto.request.SaveAvatarConfirmRequest;
import org.example.testifyproject.auth.dto.request.SignupRequest;
import org.example.testifyproject.auth.dto.response.SignupResponse;
import org.example.testifyproject.entity.Role;
import org.example.testifyproject.entity.User;
import org.example.testifyproject.common.constant.UserStatus;
import org.example.testifyproject.user.repository.RoleRepository;
import org.example.testifyproject.user.repository.UserRepository;
import org.example.testifyproject.file.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final RedisService redisService;
    private final int TOKEN_DURATION_MINUTE = 3;
    private final String VERIFY_ACCOUNT_PREFIX = "verify:email:";
    private final String FALLBACK_VERIFY_ACCOUNT_PREFIX = "fallback:verify:email:";

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public SignupResponse saveNewUser(SignupRequest signupRequest) {
        log.info("Signup request. email={}", signupRequest.getEmail());

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            log.warn("Signup failed. Email already exists. email={}", signupRequest.getEmail());
            throw new UserIsExistException(signupRequest.getEmail());
        }

        Role defaultRole = roleRepository.findByName(signupRequest.getRoleType().toString()).orElseThrow(() -> new RoleNotFoundException(signupRequest.getRoleType().toString()));
        User newUser = userMapper.toEntity(signupRequest);
        newUser.setRole(defaultRole);
        newUser.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));

        try {
            User savedUser = userRepository.save(newUser);
            log.info("User created successfully. email={}", savedUser.getEmail());
            return userMapper.toSignupResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            log.warn("Signup failed due to duplicate resource. email={}", signupRequest.getEmail());
            throw new BaseException(StatusCode.DUPLICATE_RESOURCE, "Email or username already exists");
        } catch (Exception e) {
            log.error("Unexpected error during signup. email={}", signupRequest.getEmail(), e);
            throw new BaseException(StatusCode.INTERNAL_ERROR, "Unexpected error during save user");
        }
    }

    @Override
    public String getUserAvatar() {
        return fileService.getFileFromS3(findCurrentUserByEmail().getAvatarUrl());
    }

    @Override
    public SignupResponse updateAvatar(String key) {
        User user = findCurrentUserByEmail();

        user.setAvatarUrl(key);

        try {
            return userMapper.toSignupResponse(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(StatusCode.DUPLICATE_RESOURCE, "Email or username already exists");
        } catch (Exception e) {
            throw new BaseException(StatusCode.INTERNAL_ERROR, "Unexpected error during save user");
        }
    }

    @Override
    public SignupResponse saveAvatarSuccess(SaveAvatarConfirmRequest request) {
        return updateAvatar(request.getKey());
    }

    @Override
    public VerifyMailRequest generateConfirmURL(SignupRequest signupRequest, HttpServletRequest request) {
        log.info("Generate verify email URL. email={}", signupRequest.getEmail());
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        String appUrl = scheme + "://" + serverName + ":" + serverPort + contextPath;

        String email = signupRequest.getEmail();
        //Generate verify va fallback token
        String verifyToken = generateToken();
        String fallbackToken = generateToken();

        //Generate 2 url
        String verifyURL = appUrl + "/auth/verify-account/" + verifyToken;
        String fallbackURL = appUrl + "/auth/fallback-verify-account/" + fallbackToken;

        //Luu token va fallback token vao redis
        saveVerifyTokenRedis(email, verifyToken, VERIFY_ACCOUNT_PREFIX);
        saveVerifyTokenRedis(email, fallbackToken, FALLBACK_VERIFY_ACCOUNT_PREFIX);

        return VerifyMailRequest.builder().appName(appName).toAddress(email).verifyLink(verifyURL).fallbackLink(fallbackURL).minuteExpireTime(TOKEN_DURATION_MINUTE).build();
    }

    private void saveVerifyTokenRedis(String email, String token, String prefix) {
        try {
            String key = prefix + token;
            Duration duration = Duration.ofMinutes(TOKEN_DURATION_MINUTE);

            redisService.set(key, email, duration);
        } catch (Exception e) {
            log.error("Failed to save verify token into Redis. email={}", email, e);
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void verifyAccount(String token) {
        log.info("Verify account request received");

        //Check token trong redis
        String verifyKey = VERIFY_ACCOUNT_PREFIX + token;
        String fallbackKey = FALLBACK_VERIFY_ACCOUNT_PREFIX + token;

        String verifyEmail = redisService.get(verifyKey, String.class);

        if (verifyEmail == null) {
            verifyEmail = redisService.get(fallbackKey, String.class);
        }

        if (verifyEmail != null) {
            log.info("Account verified successfully");

            //Doi trang thai account trong db
            User user = userRepository.findByEmail(verifyEmail).orElseThrow(() -> new UsernameNotFoundException("Invalid user or token"));
            user.setEmailVerified(true);
            userRepository.save(user);
            //Xoa 2 token trong redis
            redisService.delete(verifyKey);
            redisService.delete(fallbackKey);
        } else {
            log.warn("Verify account failed. Invalid or expired token");
            throw new InvalidVerifyEmailTokenException();
        }
    }

    @Override
    public void lockAccount(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUserStatus(UserStatus.LOCKED);
        userRepository.save(user);
    }

    private User findCurrentUserByEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
