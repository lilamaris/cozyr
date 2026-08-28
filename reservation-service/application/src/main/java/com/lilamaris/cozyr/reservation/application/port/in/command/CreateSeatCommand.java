package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.reservation.domain.SeatId;

public record CreateSeatCommand(
        SeatId id
) {
    public CreateSeatCommand {
        ObjectPrecondition.requireNonNull(id, "id");
    }

    public static CreateSeatCommand of(SeatId id) {
        return new CreateSeatCommand(id);
    }
}
