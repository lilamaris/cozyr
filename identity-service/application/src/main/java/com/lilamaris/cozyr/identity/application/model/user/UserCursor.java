package com.lilamaris.cozyr.identity.application.model.user;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;
import java.util.UUID;

public record UserCursor(
        Instant createdAt,
        UUID userId
) {
    public UserCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(userId, "userId");
    }

    public static UserCursor of(Instant createdAt, UUID userId) {
        return new UserCursor(createdAt, userId);
    }
}
