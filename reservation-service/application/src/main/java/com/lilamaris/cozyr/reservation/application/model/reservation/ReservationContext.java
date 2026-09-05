package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ReservationContext(
        UUID reservationId,
        SeatId seatId,
        LocalDate reservationDate,
        ReservationStatus status,
        List<RoomSchedule> schedules,
        UserProjection reservedUser
) {
    public ReservationContext {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        ObjectPrecondition.requireNonNull(reservationDate, "reservationDate");
        ObjectPrecondition.requireNonNull(status, "status");
        ObjectPrecondition.requireNonNull(schedules, "schedules");
        ObjectPrecondition.requireNonNull(reservedUser, "reservedUser");
    }

    public static ReservationContext of(UUID reservationId, SeatId seatId, LocalDate reservationDate, ReservationStatus status, List<RoomSchedule> schedules, UserProjection reservedUser) {
        return new ReservationContext(reservationId, seatId, reservationDate, status, schedules, reservedUser);
    }
}
