package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;
import java.util.UUID;

public record SeatDetail(
        SeatLocator seatLocator,
        Instant createdAt,
        Instant updatedAt
) {
    public SeatDetail {
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatDetail of(SeatLocator seatLocator, Instant createdAt, Instant updatedAt) {
        return new SeatDetail(seatLocator, createdAt, updatedAt);
    }

    public static SeatDetail of(UUID roomId, UUID seatId, Instant createdAt, Instant updatedAt) {
        return new SeatDetail(SeatLocator.of(roomId, seatId), createdAt, updatedAt);
    }
}
