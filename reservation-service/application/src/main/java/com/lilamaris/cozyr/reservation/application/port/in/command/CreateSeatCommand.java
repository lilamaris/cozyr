package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.UUID;

public record CreateSeatCommand(
        RoomId roomId,
        String code
) {
    public CreateSeatCommand {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        StringPrecondition.requireNonBlank(code, "code");
    }

    public static CreateSeatCommand of(RoomId roomId, String code) {
        return new CreateSeatCommand(roomId, code);
    }

    public static CreateSeatCommand of(UUID roomId, String code) {
        return new CreateSeatCommand(RoomId.of(roomId), code);
    }
}
