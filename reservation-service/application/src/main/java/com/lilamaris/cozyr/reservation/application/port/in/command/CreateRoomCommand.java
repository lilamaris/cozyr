package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record CreateRoomCommand(
        String name,
        String description
) {
    public CreateRoomCommand {
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
    }

    public static CreateRoomCommand of(String name, String description) {
        return new CreateRoomCommand(name, description);
    }
}
