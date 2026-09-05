package com.lilamaris.cozyr.reservation.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;

public record SeatCreatedEvent(
        Long roomId,
        String seatId,
        Instant createdAt
) implements MessagePayload {
    public SeatCreatedEvent {
        NumberPrecondition.requireNonNegative(roomId, "roomId");
        StringPrecondition.requireNonBlank(seatId, "seatId");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatCreatedEvent of(Long roomId, String seatId, Instant createdAt) {
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
        return "%d-%s".formatted(event.roomId, event.seatId);
    }
}
