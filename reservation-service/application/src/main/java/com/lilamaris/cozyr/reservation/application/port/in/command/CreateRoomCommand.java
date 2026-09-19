package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record CreateRoomCommand(
        String name,
        String description,
        UUID userId
) {
    public CreateRoomCommand {
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(userId, "userId");
    }

    public static CreateRoomCommand of(String name, String description, UUID userId) {
        return new CreateRoomCommand(name, description, userId);
    }
}
