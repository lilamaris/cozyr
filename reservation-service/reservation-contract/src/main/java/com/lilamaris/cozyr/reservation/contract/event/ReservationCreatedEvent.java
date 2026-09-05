package com.lilamaris.cozyr.reservation.contract.event;

import com.lilamaris.cozyr.kernel.core.condition.CollectionPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.kernel.message.MessageKind;
import com.lilamaris.cozyr.kernel.message.MessagePayload;
import com.lilamaris.cozyr.reservation.contract.model.LocalTimeSchedule;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReservationCreatedEvent(
        UUID reservationId,
        LocalDate reservationDate,
        Long roomId,
        String seatId,
        UUID requestedUserId,
        List<LocalTimeSchedule> schedules
) implements MessagePayload {
    public ReservationCreatedEvent {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(reservationDate, "reservationDate");
        NumberPrecondition.requireNonNegative(roomId, "roomId");
        StringPrecondition.requireNonBlank(seatId, "seatId");
        ObjectPrecondition.requireNonNull(requestedUserId, "requestedUserId");
        CollectionPrecondition.requireNonNullElements(schedules, "schedules");
    }

    public static ReservationCreatedEvent of(UUID reservationId, LocalDate reservationDate, Long roomId, String seatId, UUID requestedUserId, List<LocalTimeSchedule> schedules) {
        return new ReservationCreatedEvent(reservationId, reservationDate, roomId, seatId, requestedUserId, schedules);
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
