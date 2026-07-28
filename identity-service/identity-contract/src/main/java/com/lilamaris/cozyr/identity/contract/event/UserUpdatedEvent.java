package com.lilamaris.cozyr.identity.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record UserUpdatedEvent(
        UUID userId,
        String displayName,
        Instant updatedAt
) implements EventPayload<UUID> {
    public UserUpdatedEvent {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static UserUpdatedEvent of(UUID userId, String displayName, Instant updatedAt) {
        return new UserUpdatedEvent(userId, displayName, updatedAt);
    }

    @Override
    public UUID id() {
        return userId;
    }
}
