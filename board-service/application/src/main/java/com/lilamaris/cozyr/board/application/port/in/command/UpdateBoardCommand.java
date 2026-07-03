package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdateBoardCommand(
        UUID boardId,
        String name,
        String description
) {
    public UpdateBoardCommand {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
        StringPrecondition.requireNonBlank(name, "name");
        ObjectPrecondition.requireNonNull(description, "description");
    }

    public static UpdateBoardCommand of(UUID boardId, String name, String description) {
        return new UpdateBoardCommand(boardId, name, description);
    }
}
