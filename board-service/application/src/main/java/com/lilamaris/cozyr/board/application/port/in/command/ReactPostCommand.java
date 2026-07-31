package com.lilamaris.cozyr.board.application.port.in.command;

import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;

import java.util.UUID;

public record ReactPostCommand(
        long postId,
        UUID actorUserId,
        ReactionType reactionType
) {
    public ReactPostCommand {
        NumberPrecondition.requireNonNegative(postId, "postId");
        ObjectPrecondition.requireNonNull(actorUserId, "actorUserId");
        ObjectPrecondition.requireNonNull(reactionType, "reactionType");
    }

    public static ReactPostCommand of(long postId, UUID actorUserId, ReactionType reactionType) {
        return new ReactPostCommand(postId, actorUserId, reactionType);
    }
}