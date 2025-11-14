package org.example.testifyproject.service;

import org.example.testifyproject.dtos.request.SignupRequest;
import org.example.testifyproject.dtos.response.SignupResponse;
import org.example.testifyproject.entity.User;

public interface UserService {
    SignupResponse saveNewUser(SignupRequest signupRequest);
    SignupResponse updateAvatar(String key);
}
