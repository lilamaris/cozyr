package com.lilamaris.cozyr.identity.application.model.user;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record UserSummary(
        UUID userId,
        String displayName,
        Instant createdAt
) {
    public UserSummary {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static UserSummary of(UUID userId, String displayName, Instant createdAt) {
        return new UserSummary(userId, displayName, createdAt);
    }
}
