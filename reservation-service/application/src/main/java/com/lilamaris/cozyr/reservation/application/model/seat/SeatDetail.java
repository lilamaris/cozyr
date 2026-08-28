package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;

public record SeatDetail(
        SeatId id,
        Instant createdAt,
        Instant updatedAt
) {
    public SeatDetail {
        ObjectPrecondition.requireNonNull(id, "id");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatDetail of(SeatId id, Instant createdAt, Instant updatedAt) {
        return new SeatDetail(id, createdAt, updatedAt);
    }
}
