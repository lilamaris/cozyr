package com.lilamaris.cozyr.identity.application.exception;

import com.lilamaris.shrturl.kernel.application.exception.ApplicationProgressCode;
import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IdentityServiceProgressCode implements ApplicationProgressCode {
    USER_NOT_FOUND(ProcessReason.REJECTED, "user", "not-found", "사용자를 찾을 수 없습니다."),
    ACCOUNT_NOT_FOUND(ProcessReason.REJECTED, "account", "not-found", "계정을 찾을 수 없습니다."),
    EMAIL_DUPLICATED(ProcessReason.REJECTED, "account", "duplicated", "이미 사용 중인 이메일입니다."),
    AUTHENTICATE_FAILED(ProcessReason.REJECTED, "account", "unauthorized", "인증 실패.")
    ;

    private final ProcessReason reason;
    private final String resourceName;
    private final String type;
    private final String message;

    @Override
    public ProcessReason reason() {
        return reason;
    }

    @Override
    public String resourceName() {
        return resourceName;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String message() {
        return message;
    }
}
