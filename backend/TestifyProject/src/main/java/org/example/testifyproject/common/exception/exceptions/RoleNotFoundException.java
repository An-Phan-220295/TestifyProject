package org.example.testifyproject.common.exception.exceptions;

import org.example.testifyproject.entity.enums.StatusCode;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(String roleName) {
        super(StatusCode.NOT_FOUND, "Role not found: " + roleName);
    }
}
