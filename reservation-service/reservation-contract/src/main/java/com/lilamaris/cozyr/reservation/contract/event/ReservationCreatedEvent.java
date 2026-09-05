package com.lilamaris.cozyr.reservation.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ReservationCreatedEvent(
        UUID reservationId,
        LocalDate reservationDate,
        Long roomId,
        String seatId,
        UUID requestedUserId
) implements MessagePayload {
    public ReservationCreatedEvent {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(reservationDate, "reservationDate");
        NumberPrecondition.requireNonNegative(roomId, "roomId");
        StringPrecondition.requireNonBlank(seatId, "seatId");
        ObjectPrecondition.requireNonNull(requestedUserId, "requestedUserId");
    }

    public static ReservationCreatedEvent of(UUID reservationId, LocalDate reservationDate, Long roomId, String seatId, UUID requestedUserId) {
        return new ReservationCreatedEvent(reservationId, reservationDate, roomId, seatId, requestedUserId);
    }

    @Override
    public MessageKind kind() {
        return ReservationServiceMessageKind.RESERVATION_CREATED;
    }

    public MessageEnvelope<ReservationCreatedEvent> toMessage(Instant now) {
        return MessageEnvelope.of(keyOf(this), this, now);
    }

    private String keyOf(ReservationCreatedEvent event) {
        return event.reservationId.toString();
    }
}
