package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdateCategoryCommand(
        UUID categoryId,
        String name,
        String description,
        UUID actorUserId
) {
    public UpdateCategoryCommand {
        ObjectPrecondition.requireNonNull(categoryId, "categoryId");
        StringPrecondition.requireNonBlank(name, "name");
        StringPrecondition.requireNonBlank(description, "description");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static UpdateCategoryCommand of(UUID categoryId, String name, String description, UUID actorUserId) {
        return new UpdateCategoryCommand(categoryId, name, description, actorUserId);
    }
}
