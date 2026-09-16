package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record RoomSummary(
        UUID roomId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public RoomSummary {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static RoomSummary of(UUID roomId, String name, String description, Instant createdAt, Instant updatedAt) {
        return new RoomSummary(roomId, name, description, createdAt, updatedAt);
    }
}
