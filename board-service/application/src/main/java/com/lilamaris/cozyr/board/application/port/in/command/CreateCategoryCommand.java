package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record CreateCategoryCommand(
        UUID boardId,
        String name,
        String description,
        UUID actorUserId
) {
    public CreateCategoryCommand {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static CreateCategoryCommand of(UUID boardId, String name, String description, UUID actorUserId) {
        return new CreateCategoryCommand(boardId, name, description, actorUserId);
    }
}
