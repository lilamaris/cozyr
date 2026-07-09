package com.lilamaris.cozyr.board.application.model.post;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;

public record PostCursor(
        Instant createdAt,
        long postId
) {
    public PostCursor {
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");
        NumberPrecondition.requireNonNegative(postId, "postId");
    }

    public static PostCursor of(Instant createdAt, long postId) {
        return new PostCursor(createdAt, postId);
    }
}
