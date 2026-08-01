package com.lilamaris.cozyr.board.application.model.reaction;

import com.lilamaris.cozyr.board.application.model.user.UserProjection;
import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.List;
import java.util.Map;

public record PostReactionSummary(
        long postId,
        Map<ReactionType, List<UserProjection>> reactions
) {
    public PostReactionSummary {
        NumberPrecondition.requireNonNegative(postId, "postId");
        reactions = Map.copyOf(ObjectPrecondition.requireNonNull(reactions, "reactions"));
    }

    public static PostReactionSummary of(long postId, Map<ReactionType, List<UserProjection>> reactions) {
        return new PostReactionSummary(postId, reactions);
    }
}
