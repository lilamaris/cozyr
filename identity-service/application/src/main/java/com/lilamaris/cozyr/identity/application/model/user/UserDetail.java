package com.lilamaris.cozyr.identity.application.model.user;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.TimePrecondition;

import java.time.Instant;
import java.util.UUID;

public record UserDetail(
        UUID userId,
        String displayName,
        Instant createdAt,
        Instant updatedAt
) {
    public UserDetail {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (updatedAt != null) {
            TimePrecondition.requireAfterOrEqual(updatedAt, createdAt, "updatedAt", "createdAt");
        }
    }

    public static UserDetail of(UUID userId, String displayName, Instant createdAt, Instant updatedAt) {
        return new UserDetail(userId, displayName, createdAt, updatedAt);
    }
}
