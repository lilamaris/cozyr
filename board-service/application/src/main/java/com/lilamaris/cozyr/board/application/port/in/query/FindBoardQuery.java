package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record FindBoardQuery(UUID boardId) {
    public FindBoardQuery {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
    }

    public static FindBoardQuery of(UUID boardId) {
        return new FindBoardQuery(boardId);
    }
}
