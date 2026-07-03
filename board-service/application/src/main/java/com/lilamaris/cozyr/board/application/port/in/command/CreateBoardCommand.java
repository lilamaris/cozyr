package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record CreateBoardCommand(
        String name,
        String description
) {
    public CreateBoardCommand {
        StringPrecondition.requireNonBlank(name, "name");
        ObjectPrecondition.requireNonNull(description, "description");
    }

    public static CreateBoardCommand of(String name, String description) {
        return new CreateBoardCommand(name, description);
    }
}
