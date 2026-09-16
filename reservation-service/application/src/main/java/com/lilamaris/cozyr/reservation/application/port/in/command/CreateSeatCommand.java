package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.UUID;

public record CreateSeatCommand(
        RoomId roomId,
        String seatCode
) {
    public CreateSeatCommand {
        ObjectPrecondition.requireNonNull(roomId, "roomId");
        StringPrecondition.requireNonBlank(seatCode, "seatCode");
    }

    public static CreateSeatCommand of(RoomId roomId, String seatCode) {
        return new CreateSeatCommand(roomId, seatCode);
    }

    public static CreateSeatCommand of(UUID roomId, String seatCode) {
        return new CreateSeatCommand(RoomId.of(roomId), seatCode);
    }
}
