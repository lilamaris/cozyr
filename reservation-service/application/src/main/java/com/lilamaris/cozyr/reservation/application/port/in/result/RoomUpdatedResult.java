package com.lilamaris.cozyr.reservation.application.port.in.result;

import com.lilamaris.cozyr.reservation.domain.Room;

import java.time.Instant;
import java.util.UUID;

public record RoomUpdatedResult(
        UUID roomId,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static RoomUpdatedResult from(Room room) {
        return new RoomUpdatedResult(
                room.getId().getValue(),
                room.getName(),
                room.getDescription(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
