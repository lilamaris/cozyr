package com.lilamaris.cozyr.reservation.application.model.room;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoomDetail(
        UUID roomId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        List<RoomSchedule> schedules
) {
    public RoomDetail {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
        ObjectPrecondition.requireNonNull(schedules, "schedules");
    }

    public static RoomDetail of(UUID roomId, String name, String description, Instant createdAt, Instant updatedAt, List<RoomSchedule> schedules) {
        return new RoomDetail(roomId, name, description, createdAt, updatedAt, schedules);
    }
}
