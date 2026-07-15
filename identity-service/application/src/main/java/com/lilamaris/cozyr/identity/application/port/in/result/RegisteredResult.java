package com.lilamaris.cozyr.identity.application.port.in.result;

import java.util.UUID;

public record RegisteredResult(
        UUID userId,
        String displayName
) {
    public static RegisteredResult of(UUID userId, String displayName) {
        return new RegisteredResult(userId, displayName);
    }
}
