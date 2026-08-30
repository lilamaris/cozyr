package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;

public record SeatSummary(
        SeatId seatId,
        Instant createdAt,
        Instant updatedAt
) {
    public SeatSummary {
        ObjectPrecondition.requireNonNull(seatId, "seatId");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatSummary of(SeatId seatId, Instant createdAt, Instant updatedAt) {
        return new SeatSummary(seatId, createdAt, updatedAt);
    }
}
