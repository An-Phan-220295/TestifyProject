package com.example.testify.libraries.dtos.requests;

import com.example.testify.libraries.common.type.MailType;

public abstract class BaseMailRequest {
    public abstract MailType getMailType();
    public abstract String getToAddress();
    public abstract String buildContent();

}
