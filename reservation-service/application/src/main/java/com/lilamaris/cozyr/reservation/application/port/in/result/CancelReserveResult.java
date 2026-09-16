package com.lilamaris.cozyr.reservation.application.port.in.result;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.time.Instant;
import java.util.UUID;

public record CancelReserveResult(
        UUID reservationId,
        Instant canceledAt
) {
    public CancelReserveResult {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(canceledAt, "canceledAt");
    }

    public static CancelReserveResult of(ReservationId reservationId, Instant canceledAt) {
        return new CancelReserveResult(reservationId.getValue(), canceledAt);
    }
}
