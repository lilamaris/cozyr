package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;

public record FindPostDetailQuery(long postId) {
    public FindPostDetailQuery {
        NumberPrecondition.requireNonNegative(postId, "postId");
    }

    public static FindPostDetailQuery of(long postId) {
        return new FindPostDetailQuery(postId);
    }
}
