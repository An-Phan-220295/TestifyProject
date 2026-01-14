package org.example.testifyproject.common.exception;

import com.example.testify.libraries.common.enums.StatusCode;
import com.example.testify.libraries.common.exception.BaseException;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(String roleName) {
        super(StatusCode.NOT_FOUND, "Role not found: " + roleName);
    }
}
