package com.lilamaris.cozyr.reservation.application.port.out.status;

import jakarta.annotation.Nullable;

public record RoomUpdateParams(
        @Nullable String name,
        @Nullable String description
) {
    public static RoomUpdateParams of(@Nullable String name, @Nullable String description) {
        return new RoomUpdateParams(
                name != null && !name.isBlank() ? name : null,
                description != null && !description.isBlank() ? description : null
        );
    }
}
