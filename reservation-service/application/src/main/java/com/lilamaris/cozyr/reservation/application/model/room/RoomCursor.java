package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;
import java.util.UUID;

public record RoomCursor(
        Instant createdAt,
        UUID roomId
) {
    public RoomCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(roomId, "roomId");
    }

    public static RoomCursor of(Instant createdAt, UUID roomId) {
        return new RoomCursor(createdAt, roomId);
    }
}
