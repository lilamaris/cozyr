package com.lilamaris.cozyr.reservation.application.model.room;

import jakarta.annotation.Nullable;

public record RoomFilter(
        @Nullable String name,
        @Nullable String description
) {
    public static RoomFilter empty() {
        return new RoomFilter(null, null);
    }

    public RoomFilter withName(String name) {
        return new RoomFilter(name, description);
    }

    public RoomFilter withDescription(String description) {
        return new RoomFilter(name, description);
    }
}
