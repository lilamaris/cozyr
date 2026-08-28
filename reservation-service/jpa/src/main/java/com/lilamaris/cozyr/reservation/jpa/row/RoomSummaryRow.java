package com.lilamaris.cozyr.reservation.jpa.row;

import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;

import java.time.Instant;

public record RoomSummaryRow(
        long roomId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public RoomSummary toSummary() {
        return RoomSummary.of(roomId, name, description, createdAt, updatedAt);
    }
}
