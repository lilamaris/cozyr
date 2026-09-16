package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.kernel.core.condition.CollectionPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReservationDetail(
        UUID reservationId,
        SeatLocator seatLocator,
        ReservationStatus status,
        List<RoomSchedule> schedules,
        Instant createdAt,
        Instant updatedAt,
        UserProjection reserveUser
) {
    public ReservationDetail {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        ObjectPrecondition.requireNonNull(status, "status");
        CollectionPrecondition.requireNonNullElements(schedules, "schedules");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static ReservationDetail of(UUID reservationId, SeatLocator seatLocator, ReservationStatus status, List<RoomSchedule> schedules, Instant createdAt, Instant updatedAt, UserProjection reserveUser) {
        return new ReservationDetail(reservationId, seatLocator, status, schedules, createdAt, updatedAt, reserveUser);
    }

    public static ReservationDetail of(UUID reservationId, UUID roomId, UUID seatId, ReservationStatus status, List<RoomSchedule> schedules, Instant createdAt, Instant updatedAt, UserProjection reserveUser) {
        return new ReservationDetail(reservationId, SeatLocator.of(roomId, seatId), status, schedules, createdAt, updatedAt, reserveUser);
    }
}
