package com.lilamaris.cozyr.kernel.web.response;

import com.lilamaris.shrturl.kernel.application.exception.ProcessReason;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StandardErrorDescriptor implements ErrorDescriptor {
    BAD_REQUEST(ProcessReason.REJECTED, "bad-request", "Bad Request"),
    NOT_FOUND(ProcessReason.REJECTED, "not-found", "Not Found"),
    ACCESS_DENIED(ProcessReason.REJECTED, "access-denied", "Access Denied"),
    INTERNAL_SERVER_ERROR(ProcessReason.FAILURE, "internal-server-error", "Internal server error"),
    UNEXPECTED_ERROR(ProcessReason.FAILURE, "unexpected-error", "Internal server error");

    private final ProcessReason reason;
    private final String type;
    private final String message;

    @Override
    public ProcessReason reason() {
        return reason;
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
