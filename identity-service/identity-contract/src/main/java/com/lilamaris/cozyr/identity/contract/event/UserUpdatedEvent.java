package com.lilamaris.cozyr.identity.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;
import java.util.UUID;

public record UserUpdatedEvent(
        UUID userId,
        String displayName,
        Instant updatedAt
) implements MessagePayload {
    @Override
    public MessageKind kind() {
        return IdentityServiceMessageKind.USER_UPDATED;
    }

    public UserUpdatedEvent {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static UserUpdatedEvent of(UUID userId, String displayName, Instant updatedAt) {
        return new UserUpdatedEvent(userId, displayName, updatedAt);
    }

    public MessageEnvelope<UserUpdatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(userId.toString(), this, now);
    }
}
