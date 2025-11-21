package org.example.testifyproject.common.exception.exceptions;

import com.example.testify_libraries.common.enums.StatusCode;
import com.example.testify_libraries.common.exception.BaseException;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(String roleName) {
        super(StatusCode.NOT_FOUND, "Role not found: " + roleName);
    }
}
