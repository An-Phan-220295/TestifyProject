package com.example.testify.common.exception.exceptions;

import com.example.testify_libraries.common.enums.StatusCode;
import com.example.testify_libraries.common.exception.BaseException;

public class TemplateNotFoundException extends BaseException {
    public TemplateNotFoundException(String template) {
        super(StatusCode.NOT_FOUND, "Template not found: " + template);
    }
}
