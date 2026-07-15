package com.lilamaris.cozyr.identity.application.port.in.result;

import java.util.UUID;

public record AuthenticatedResult(
        UUID userId,
        String displayName
) {
    public static AuthenticatedResult of(UUID userId, String displayName) {
        return new AuthenticatedResult(userId, displayName);
    }
}
