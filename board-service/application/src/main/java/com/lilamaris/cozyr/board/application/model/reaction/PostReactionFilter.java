package com.lilamaris.cozyr.board.application.model.reaction;

import com.lilamaris.cozyr.board.domain.ReactionType;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public record PostReactionFilter(
        @Nullable Long postId,
        @Nullable ReactionType reactionType,
        @Nullable UUID userId
) {
    public static PostReactionFilter empty() {
        return new PostReactionFilter(null, null, null);
    }

    public PostReactionFilter withPostId(Long postId) {
        return new PostReactionFilter(postId, reactionType, userId);
    }

    public PostReactionFilter withReactionType(ReactionType reactionType) {
        return new PostReactionFilter(postId, reactionType, userId);
    }

    public PostReactionFilter withUserId(UUID userId) {
        return new PostReactionFilter(postId, reactionType, userId);
    }
}
