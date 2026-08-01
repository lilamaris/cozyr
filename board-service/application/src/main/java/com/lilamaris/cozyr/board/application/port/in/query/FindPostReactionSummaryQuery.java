package com.lilamaris.cozyr.board.application.port.in.query;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionFilter;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record FindPostReactionSummaryQuery(
        long postId,
        @Nullable UUID userId
) {
    public FindPostReactionSummaryQuery {
        NumberPrecondition.requireNonNegative(postId, "postId");
    }

    public static FindPostReactionSummaryQuery of(long postId, @Nullable UUID userId) {
        return new FindPostReactionSummaryQuery(postId, userId);
    }

    public PostReactionFilter toFilter() {
        return PostReactionFilter.empty()
                .withPostId(postId)
                .withUserId(userId);
    }
}
