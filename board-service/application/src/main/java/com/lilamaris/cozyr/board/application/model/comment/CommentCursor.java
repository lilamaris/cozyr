package com.lilamaris.cozyr.board.application.model.comment;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;

public record CommentCursor(
        Instant createdAt,
        Long commentId
) {
    public CommentCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        NumberPrecondition.requireNonNegative(commentId, "commentId");
    }

    public static CommentCursor of(Instant createdAt, Long commentId) {
        return new CommentCursor(createdAt, commentId);
    }
}
