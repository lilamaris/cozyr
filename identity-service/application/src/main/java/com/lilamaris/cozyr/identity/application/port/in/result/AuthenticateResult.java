package com.lilamaris.cozyr.identity.application.port.in.result;

import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record AuthenticateResult(
        boolean isSuccess,
        @Nullable UUID userId
) {
    public static AuthenticateResult success(UUID userId) {
        return new AuthenticateResult(true, userId);
    }

    public static AuthenticateResult fail() {
        return new AuthenticateResult(false, null);
    }
}
