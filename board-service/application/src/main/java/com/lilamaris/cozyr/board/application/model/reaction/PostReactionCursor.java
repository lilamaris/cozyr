package com.lilamaris.cozyr.board.application.model.reaction;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.time.Instant;

public record PostReactionCursor(
        Instant lastReactedAt,
        long postId
) {
    public PostReactionCursor {
        ObjectPrecondition.requireNonNull(lastReactedAt, "lastReactedAt");
        NumberPrecondition.requireNonNegative(postId, "postId");
    }

    public static PostReactionCursor of(Instant lastReactedAt, long postId) {
        return new PostReactionCursor(lastReactedAt, postId);
    }
}
