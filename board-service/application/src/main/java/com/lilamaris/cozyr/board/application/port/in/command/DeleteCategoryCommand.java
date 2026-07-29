package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record DeleteCategoryCommand(
        UUID categoryId,
        UUID actorUserId
) {
    public DeleteCategoryCommand {
        ObjectPrecondition.requireNonNull(categoryId, "categoryId");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
    }

    public static DeleteCategoryCommand of(UUID categoryId, UUID actorUserId) {
        return new DeleteCategoryCommand(categoryId, actorUserId);
    }
}
