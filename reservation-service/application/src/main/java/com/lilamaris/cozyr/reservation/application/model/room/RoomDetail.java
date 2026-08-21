package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;

public record RoomDetail(
        long roomId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public RoomDetail {
        NumberPrecondition.requireNonNegative(roomId, "roomId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static RoomDetail of(long roomId, String name, String description, Instant createdAt, Instant updatedAt) {
        return new RoomDetail(roomId, name, description, createdAt, updatedAt);
    }
}
