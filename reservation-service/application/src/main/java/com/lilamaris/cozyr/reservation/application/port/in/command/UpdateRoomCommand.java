package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record UpdateRoomCommand(
        long roomId,
        String name,
        String description
) {
    public UpdateRoomCommand {
        NumberPrecondition.requireNonNegative(roomId, "roomId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
    }

    public static UpdateRoomCommand of(long roomId, String name, String description) {
        return new UpdateRoomCommand(roomId, name, description);
    }
}
