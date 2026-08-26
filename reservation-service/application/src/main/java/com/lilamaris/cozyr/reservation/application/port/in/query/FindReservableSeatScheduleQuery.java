package com.lilamaris.cozyr.reservation.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.LocalDate;

public record FindReservableSeatScheduleQuery(
        LocalDate targetDate,
        SeatId seatId
) {
    public FindReservableSeatScheduleQuery {
        ObjectPrecondition.requireNonNull(targetDate, "targetDate");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
    }

    public static FindReservableSeatScheduleQuery of(LocalDate targetDate, SeatId seatId) {
        return new FindReservableSeatScheduleQuery(targetDate, seatId);
    }
}
