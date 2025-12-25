package org.example.testifyproject.service;

import com.example.testify.libraries.dtos.requests.VerifyMailRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.example.testifyproject.dtos.request.SaveAvatarConfirmRequest;
import org.example.testifyproject.dtos.request.SignupRequest;
import org.example.testifyproject.dtos.response.SignupResponse;

public interface UserService {
    SignupResponse saveNewUser(SignupRequest signupRequest);

    SignupResponse updateAvatar(String key);

    String getUserAvatar();

    SignupResponse saveAvatarSuccess(SaveAvatarConfirmRequest request);

    VerifyMailRequest generateConfirmURL(SignupRequest signupRequest, HttpServletRequest request);

    void verifyAccount(String token);

    void lockAccount(String email);
}
