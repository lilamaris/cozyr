package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record FindBoardDetailQuery(UUID boardId) {
    public FindBoardDetailQuery {
        ObjectPrecondition.requireNonNull(boardId, "boardId");
    }

    public static FindBoardDetailQuery of(UUID boardId) {
        return new FindBoardDetailQuery(boardId);
    }
}
