package com.lilamaris.cozyr.board.application.model.board;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;
import java.util.UUID;

public record BoardCursor(
        Instant createdAt,
        UUID boardId
) {
    public BoardCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        ObjectPrecondition.requireNonNull(boardId, "boardId");
    }

    public static BoardCursor of(Instant createdAt, UUID boardId) {
        return new BoardCursor(createdAt, boardId);
    }
}
