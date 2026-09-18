package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.time.Instant;
import java.util.UUID;

public record DeactivateRoomCommand(
        RoomId roomId,
        Instant deactivatedAt,
        UUID userId
) {
    public DeactivateRoomCommand {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        ObjectPrecondition.requireNonNull(deactivatedAt, "deactivatedAt");
        ObjectPrecondition.requireNonNull(userId, "userId");
    }

    public static DeactivateRoomCommand of(RoomId roomId, Instant deactivatedAt, UUID userId) {
        return new DeactivateRoomCommand(roomId, deactivatedAt, userId);
    }
}
