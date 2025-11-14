package org.example.testifyproject.service;

import org.example.testifyproject.dtos.request.SaveAvatarConfirmRequest;
import org.example.testifyproject.dtos.request.SignupRequest;
import org.example.testifyproject.dtos.response.SignupResponse;

public interface UserService {
    SignupResponse saveNewUser(SignupRequest signupRequest);

    SignupResponse updateAvatar(String key);

    String getUserAvatar();

    SignupResponse saveAvatarSuccess(SaveAvatarConfirmRequest request);
}
