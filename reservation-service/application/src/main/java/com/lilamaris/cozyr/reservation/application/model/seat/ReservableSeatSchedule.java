package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReservableSeatSchedule(
        LocalDate targetDate,
        RoomId roomId,
        SeatId seatId,
        List<RoomSchedule> schedules
) {
    public ReservableSeatSchedule {
        ObjectPrecondition.requireNonNull(targetDate, "targetDate");
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        ObjectPrecondition.requireNonNull(schedules, "schedules");
    }

    public static ReservableSeatSchedule of(LocalDate targetDate, RoomId roomId, SeatId seatId, List<RoomSchedule> schedules) {
        return new ReservableSeatSchedule(targetDate, roomId, seatId, schedules);
    }

    public static ReservableSeatSchedule of(LocalDate targetDate, UUID roomId, UUID seatId, List<RoomSchedule> schedules) {
        return new ReservableSeatSchedule(targetDate, RoomId.of(roomId), SeatId.of(seatId), schedules);
    }
}
