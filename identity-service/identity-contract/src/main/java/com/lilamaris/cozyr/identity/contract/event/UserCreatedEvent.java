package com.lilamaris.cozyr.identity.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;
import java.util.UUID;

public record UserCreatedEvent(
        UUID userId,
        String displayName,
        Instant createdAt
) implements MessagePayload {
    @Override
    public MessageKind kind() {
        return IdentityServiceMessageKind.USER_CREATED;
    }

    public UserCreatedEvent {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static UserCreatedEvent of(UUID userId, String displayName, Instant createdAt) {
        return new UserCreatedEvent(userId, displayName, createdAt);
    }

    public MessageEnvelope<UserCreatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(userId.toString(), this, now);
    }
}
