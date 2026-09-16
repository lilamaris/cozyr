package com.lilamaris.cozyr.reservation.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;
import java.util.UUID;

public record SeatCreatedEvent(
        UUID roomId,
        UUID seatId,
        Instant createdAt
) implements MessagePayload {
    public SeatCreatedEvent {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatCreatedEvent of(UUID roomId, UUID seatId, Instant createdAt) {
        return new SeatCreatedEvent(roomId, seatId, createdAt);
    }

    @Override
    public MessageKind kind() {
        return ReservationServiceMessageKind.SEAT_CREATED;
    }

    public MessageEnvelope<SeatCreatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(keyOf(this), this, now);
    }

    private String keyOf(SeatCreatedEvent event) {
        return "%s-%s".formatted(event.roomId, event.seatId);
    }
}
