package com.lilamaris.cozyr.identity.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String displayName,
        Instant createdAt
) implements EventPayload<UUID> {
    public UserCreatedEvent {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static UserCreatedEvent of(UUID userId, String displayName, Instant createdAt) {
        return new UserCreatedEvent(userId, displayName, createdAt);
    }

    @Override
    public UUID id() {
        return userId;
    }
}
