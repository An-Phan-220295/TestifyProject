package org.example.testifyproject.user.service;

import com.example.testify.libraries.dtos.requests.VerifyURLMailRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.example.testifyproject.auth.dto.request.SignupRequest;
import org.example.testifyproject.auth.dto.response.SignupResponse;
import org.example.testifyproject.file.dto.request.SaveAvatarConfirmRequest;

public interface UserService {
    SignupResponse saveNewUser(SignupRequest signupRequest);

    SignupResponse updateAvatar(String key);

    String getUserAvatar();

    SignupResponse saveAvatarSuccess(SaveAvatarConfirmRequest request);

    VerifyURLMailRequest generateConfirmURL(SignupRequest signupRequest, HttpServletRequest request);

    void verifyAccount(String token);

    void lockAccount(String email);

    boolean userIsExist(String email);

    int saveNewPassword(String newPassword, String email);
}
