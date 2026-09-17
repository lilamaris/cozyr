package com.lilamaris.cozyr.reservation.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;
import java.util.UUID;

public record RoomCreatedEvent(
        UUID roomId,
        String name,
        String description,
        Instant createdAt
) implements MessagePayload {
    public RoomCreatedEvent {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static RoomCreatedEvent of(UUID roomId, String name, String description, Instant createdAt) {
        return new RoomCreatedEvent(roomId, name, description, createdAt);
    }

    @Override
    public MessageKind kind() {
        return ReservationServiceMessageKind.ROOM_CREATED;
    }

    public MessageEnvelope<RoomCreatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(roomId.toString(), this, now);
    }
}
