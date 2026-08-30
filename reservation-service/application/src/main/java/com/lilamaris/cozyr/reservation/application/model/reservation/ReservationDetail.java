package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.kernel.core.condition.CollectionPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReservationDetail(
        UUID reservationId,
        SeatId seatId,
        List<RoomSchedule> schedules,
        Instant createdAt,
        Instant updatedAt,
        UserProjection reserveUser
) {
    public ReservationDetail {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        CollectionPrecondition.requireNonNullElements(schedules, "schedules");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static ReservationDetail of(UUID reservationId, SeatId seatId, List<RoomSchedule> schedules, Instant createdAt, Instant updatedAt, UserProjection reserveUser) {
        return new ReservationDetail(reservationId, seatId, schedules, createdAt, updatedAt, reserveUser);
    }
}
