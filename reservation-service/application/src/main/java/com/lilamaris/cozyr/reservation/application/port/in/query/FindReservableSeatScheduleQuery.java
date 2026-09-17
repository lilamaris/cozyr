package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;

import java.time.LocalDate;
import java.util.UUID;

public record FindReservableSeatScheduleQuery(
        LocalDate targetDate,
        SeatLocator seatLocator
) {
    public FindReservableSeatScheduleQuery {
        ObjectPrecondition.requireNonNull(targetDate, "targetDate");
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
    }

    public static FindReservableSeatScheduleQuery of(LocalDate targetDate, SeatLocator seatLocator) {
        return new FindReservableSeatScheduleQuery(targetDate, seatLocator);
    }

    public static FindReservableSeatScheduleQuery of(LocalDate targetDate, UUID roomId, UUID seatId) {
        return new FindReservableSeatScheduleQuery(targetDate, SeatLocator.of(roomId, seatId));
    }
}
