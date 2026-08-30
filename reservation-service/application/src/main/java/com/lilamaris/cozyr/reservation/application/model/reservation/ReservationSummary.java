package com.lilamaris.cozyr.reservation.application.model.reservation;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;
import java.util.UUID;

public record ReservationSummary(
        UUID reservationId,
        ReservationStatus status,
        SeatId seatId,
        Instant createdAt,
        Instant updatedAt,
        UserProjection reserveUser,
        int occupiedSlotCount
) {
    public ReservationSummary {
        ObjectPrecondition.requireNonNull(reservationId, "reservationId");
        ObjectPrecondition.requireNonNull(status, "status");
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(reserveUser, "reserveUser");
        NumberPrecondition.requirePositive(occupiedSlotCount, "occupiedSlotCount");
    }

    public static ReservationSummary of(UUID reservationId, ReservationStatus status, SeatId seatId, Instant createdAt, Instant updatedAt, UserProjection reserveUser, int occupiedSlotCount) {
        return new ReservationSummary(reservationId, status, seatId, createdAt, updatedAt, reserveUser, occupiedSlotCount);
    }
}
