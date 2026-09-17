package com.lilamaris.cozyr.reservation.application.model.seat;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record SeatDetail(
        SeatLocator seatLocator,
        String code,
        Instant createdAt,
        Instant updatedAt
) {
    public SeatDetail {
        ObjectPrecondition.requireNonNull(seatLocator, "seatLocator");
        StringPrecondition.requireNonBlank(code, "code");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static SeatDetail of(SeatLocator seatLocator, String code, Instant createdAt, Instant updatedAt) {
        return new SeatDetail(seatLocator, code, createdAt, updatedAt);
    }

    public static SeatDetail of(UUID roomId, UUID seatId, String code, Instant createdAt, Instant updatedAt) {
        return new SeatDetail(SeatLocator.of(roomId, seatId), code, createdAt, updatedAt);
    }
}
