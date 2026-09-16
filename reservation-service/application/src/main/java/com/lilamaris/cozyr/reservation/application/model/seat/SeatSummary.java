package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;
import java.util.UUID;

public record SeatSummary(
        SeatLocator seatLocator,
        Instant createdAt,
        Instant updatedAt
) {
    public SeatSummary {
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatSummary of(SeatLocator seatLocator, Instant createdAt, Instant updatedAt) {
        return new SeatSummary(seatLocator, createdAt, updatedAt);
    }

    public static SeatSummary of(UUID roomId, UUID seatId, Instant createdAt, Instant updatedAt) {
        return new SeatSummary(SeatLocator.of(roomId, seatId), createdAt, updatedAt);
    }
}
