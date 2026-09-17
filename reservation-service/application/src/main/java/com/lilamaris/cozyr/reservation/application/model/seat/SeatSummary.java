package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record SeatSummary(
        SeatLocator seatLocator,
        String code,
        Instant createdAt,
        Instant updatedAt
) {
    public SeatSummary {
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        StringPrecondition.requireNonBlank(code, "code");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatSummary of(SeatLocator seatLocator, String code, Instant createdAt, Instant updatedAt) {
        return new SeatSummary(seatLocator, code, createdAt, updatedAt);
    }

    public static SeatSummary of(UUID roomId, UUID seatId, String code, Instant createdAt, Instant updatedAt) {
        return new SeatSummary(SeatLocator.of(roomId, seatId), code, createdAt, updatedAt);
    }
}
