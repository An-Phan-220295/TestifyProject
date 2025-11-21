package org.example.testifyproject.controller;

import com.example.testify_libraries.common.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testifyproject.dtos.request.SaveAvatarConfirmRequest;
import org.example.testifyproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save-file-completed")
    public ResponseEntity<?> saveFileCompleted(@Valid @RequestBody SaveAvatarConfirmRequest request) {
        return Util.successResponse(userService.saveAvatarSuccess(request));
    }

    @PostMapping("/get-avatar")
    public ResponseEntity<?> getAvatar() {
        return Util.successResponse(userService.getUserAvatar());
    }
}
