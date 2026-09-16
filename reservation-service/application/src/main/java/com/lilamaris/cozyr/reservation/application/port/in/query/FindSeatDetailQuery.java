package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;

import java.util.UUID;

public record FindSeatDetailQuery(
        SeatLocator seatLocator
) {
    public FindSeatDetailQuery {
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
    }

    public static FindSeatDetailQuery of(SeatLocator seatLocator) {
        return new FindSeatDetailQuery(seatLocator);
    }

    public static FindSeatDetailQuery of(UUID roomId, UUID seatId) {
        return new FindSeatDetailQuery(SeatLocator.of(roomId, seatId));
    }
}
