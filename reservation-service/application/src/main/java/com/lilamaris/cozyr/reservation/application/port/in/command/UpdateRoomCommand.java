package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.UUID;

public record UpdateRoomCommand(
        RoomId roomId,
        String name,
        String description
) {
    public UpdateRoomCommand {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
    }

    public static UpdateRoomCommand of(UUID roomId, String name, String description) {
        return new UpdateRoomCommand(RoomId.of(roomId), name, description);
    }
}
