package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.ReservationId;

import java.util.UUID;

public record CancelReserveCommand(
        ReservationId reservationId
) {
    public CancelReserveCommand {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
    }

    public static CancelReserveCommand of(UUID reservationId) {
        return new CancelReserveCommand(ReservationId.of(reservationId));
    }
}
