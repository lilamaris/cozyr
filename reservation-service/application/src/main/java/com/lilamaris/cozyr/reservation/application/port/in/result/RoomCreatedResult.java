package com.lilamaris.cozyr.reservation.application.port.in.result;

import com.lilamaris.cozyr.reservation.domain.Room;

import java.time.Instant;
import java.util.UUID;

public record RoomCreatedResult(
        UUID roomId,
        String name,
        String description,
        Instant createdAt
) {
    public static RoomCreatedResult from(Room room) {
        return new RoomCreatedResult(
                room.getId().getValue(),
                room.getName(),
                room.getDescription(),
                room.getCreatedAt()
        );
    }
}
