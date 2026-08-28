package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;
import java.util.UUID;

public record ReservationCursor(
        Instant createdAt,
        UUID reservationId
) {
    public ReservationCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
    }

    public static ReservationCursor of(Instant createdAt, UUID reservationId) {
        return new ReservationCursor(createdAt, reservationId);
    }
}
