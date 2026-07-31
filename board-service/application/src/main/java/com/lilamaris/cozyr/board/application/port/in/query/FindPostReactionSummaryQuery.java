package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;

public record FindPostReactionSummaryQuery(long postId) {
    public FindPostReactionSummaryQuery {
        NumberPrecondition.requireNonNegative(postId, "postId");
    }

    public static FindPostReactionSummaryQuery of(long postId) {
        return new FindPostReactionSummaryQuery(postId);
    }
}
