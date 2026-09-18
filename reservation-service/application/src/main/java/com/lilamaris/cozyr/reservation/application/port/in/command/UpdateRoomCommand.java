package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateParams;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import jakarta.annotation.Nullable;

import java.util.UUID;

public record UpdateRoomCommand(
        RoomId roomId,
        RoomUpdateParams params,
        UUID userId
) {
    public UpdateRoomCommand {
        ObjectPrecondition.requireNonNull(userId, "userId");
        ObjectPrecondition.requireNonNull(params, "params");
        ObjectPrecondition.requireNonNull(userId, "userId");
    }

    public static UpdateRoomCommand of(UUID roomId, @Nullable String name, @Nullable String description, UUID userId) {
        var params = RoomUpdateParams.of(name, description);
        return new UpdateRoomCommand(RoomId.of(roomId), params, userId);
    }
}
