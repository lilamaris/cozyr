package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.UUID;

public record ActivateRoomCommand(
        RoomId roomId,
        UUID userId
) {
    public ActivateRoomCommand {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        ObjectPrecondition.requireNonNull(userId, "userId");
    }

    public static ActivateRoomCommand of(RoomId roomId, UUID userId) {
        return new ActivateRoomCommand(roomId, userId);
    }
}
