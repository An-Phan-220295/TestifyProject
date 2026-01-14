package org.example.testifyproject.user.controller;

import com.example.testify.libraries.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.testifyproject.file.dto.request.SaveAvatarConfirmRequest;
import org.example.testifyproject.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/save-file-completed")
    public ResponseEntity<?> saveFileCompleted(@Valid @RequestBody SaveAvatarConfirmRequest request) {
        log.info("Save avatar file completed request received");

        Object result = userService.saveAvatarSuccess(request);

        log.info("User avatar saved successfully");

        return Util.successResponse(result);
    }

    @PostMapping("/get-avatar")
    public ResponseEntity<?> getAvatar() {
        log.info("Get user avatar request received");

        Object avatar = userService.getUserAvatar();

        log.info("Get user avatar successfully");

        return Util.successResponse(avatar);
    }
}
