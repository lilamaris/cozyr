package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record CancelReserveCommand(
        UUID reservationId
) {
    public CancelReserveCommand {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
    }

    public static CancelReserveCommand of(UUID reservationId) {
        return new CancelReserveCommand(reservationId);
    }
}
