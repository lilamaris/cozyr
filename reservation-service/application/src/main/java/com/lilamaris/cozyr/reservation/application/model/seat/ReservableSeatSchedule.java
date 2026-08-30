package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.LocalDate;
import java.util.List;

public record ReservableSeatSchedule(
        LocalDate targetDate,
        SeatId seatId,
        List<RoomSchedule> schedules
) {
    public ReservableSeatSchedule {
        ObjectPrecondition.requireNonNull(targetDate, "targetDate");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        ObjectPrecondition.requireNonNull(schedules, "schedules");
    }

    public static ReservableSeatSchedule of(LocalDate targetDate, SeatId seatId, List<RoomSchedule> schedules) {
        return new ReservableSeatSchedule(targetDate, seatId, schedules);
    }
}
