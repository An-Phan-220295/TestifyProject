package org.example.testifyproject.common.util.mapper;

import org.example.testifyproject.auth.dto.request.SignupRequest;
import org.example.testifyproject.auth.dto.response.SignupResponse;
import org.example.testifyproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userStatus", constant = "ACTIVE")
    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "dob")
    User toEntity(SignupRequest request);

    @Mapping(target = "dob")
    @Mapping(target = "roleType", expression = "java(user.getRole().getName())")
    SignupResponse toSignupResponse(User user);
}
