package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record FindReservationDetailQuery(
        UUID reservationId
) {
    public FindReservationDetailQuery {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
    }

    public static FindReservationDetailQuery of(UUID reservationId) {
        return new FindReservationDetailQuery(reservationId);
    }
}
