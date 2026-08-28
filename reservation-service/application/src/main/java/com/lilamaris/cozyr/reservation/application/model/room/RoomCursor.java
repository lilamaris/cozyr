package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;

public record RoomCursor(
        Instant createdAt,
        long roomId
) {
    public RoomCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        NumberPrecondition.requireNonNegative(roomId, "roomId");
    }

    public static RoomCursor of(Instant createdAt, long roomId) {
        return new RoomCursor(createdAt, roomId);
    }
}
