package org.example.testifyproject.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.testifyproject.common.exception.exceptions.BaseException;
import org.example.testifyproject.common.exception.exceptions.RoleNotFoundException;
import org.example.testifyproject.common.mapper.UserMapper;
import org.example.testifyproject.dtos.request.DownloadFileRequest;
import org.example.testifyproject.dtos.request.SaveAvatarConfirmRequest;
import org.example.testifyproject.dtos.request.SignupRequest;
import org.example.testifyproject.dtos.response.SignupResponse;
import org.example.testifyproject.entity.Role;
import org.example.testifyproject.entity.User;
import org.example.testifyproject.entity.enums.StatusCode;
import org.example.testifyproject.repository.RoleRepository;
import org.example.testifyproject.repository.UserRepository;
import org.example.testifyproject.service.FileService;
import org.example.testifyproject.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;

    @Override
    public SignupResponse saveNewUser(SignupRequest signupRequest) {
        Role defaultRole = roleRepository.findByName(signupRequest.getRoleType().toString())
                .orElseThrow(() -> new RoleNotFoundException(signupRequest.getRoleType().toString()));
        User newUser = userMapper.toEntity(signupRequest);
        newUser.setRole(defaultRole);
        newUser.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));

        try {
            User savedUser = userRepository.save(newUser);
            return userMapper.toSignupResponse(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new BaseException(StatusCode.DUPLICATE_RESOURCE, "Email or username already exists");
        } catch (Exception e) {
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

    private User findCurrentUserByEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Override
    public SignupResponse saveAvatarSuccess(SaveAvatarConfirmRequest request) {
        return updateAvatar(request.getKey());
    }
}
