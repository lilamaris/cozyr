package com.lilamaris.cozyr.board.application.port.in.result;

import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.board.domain.ReactionType;

import java.time.Instant;
import java.util.UUID;

public record ReactedPostResult(
        UUID reactionId,
        long postId,
        UUID userId,
        ReactionType reactionType,
        Instant createdAt
) {
    public static ReactedPostResult from(PostReaction postReaction) {
        return new ReactedPostResult(
                postReaction.getId(),
                postReaction.getPostId(),
                postReaction.getUserId(),
                postReaction.getReactionType(),
                postReaction.getCreatedAt()
        );
    }
}
