package com.lilamaris.cozyr.identity.application.port.in.result;

import com.lilamaris.cozyr.identity.domain.User;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record UpdatedDisplayNameResult(
        UUID userId,
        String displayName,
        Instant updatedAt
) {
    public UpdatedDisplayNameResult {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static UpdatedDisplayNameResult of(UUID userId, String displayName, Instant updatedAt) {
        return new UpdatedDisplayNameResult(userId, displayName, updatedAt);
    }

    public static UpdatedDisplayNameResult from(User user) {
        ObjectPrecondition.requireNonNull(user, "user");
        return new UpdatedDisplayNameResult(user.getId(), user.getDisplayName(), user.getUpdatedAt());
    }
}
