package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;

import java.time.Instant;
import java.util.UUID;

public record ReservationSummary(
        UUID reservationId,
        ReservationStatus status,
        SeatLocator seatLocator,
        Instant createdAt,
        Instant updatedAt,
        UserProjection reserveUser,
        int occupiedSlotCount
) {
    public ReservationSummary {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(status, "status");
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(reserveUser, "reserveUser");
        NumberPrecondition.requirePositive(occupiedSlotCount, "occupiedSlotCount");
    }

    public static ReservationSummary of(UUID reservationId, ReservationStatus status, SeatLocator seatLocator, Instant createdAt, Instant updatedAt, UserProjection reserveUser, int occupiedSlotCount) {
        return new ReservationSummary(reservationId, status, seatLocator, createdAt, updatedAt, reserveUser, occupiedSlotCount);
    }

    public static ReservationSummary of(UUID reservationId, ReservationStatus status, UUID roomId, UUID seatId, Instant createdAt, Instant updatedAt, UserProjection reserveUser, int occupiedSlotCount) {
        return new ReservationSummary(reservationId, status, SeatLocator.of(roomId, seatId), createdAt, updatedAt, reserveUser, occupiedSlotCount);
    }
}
