package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.domain.ReactionType;

import java.time.Instant;
import java.util.UUID;

public class PostReactionRow {
    public record Single(
            long postId,
            ReactionType reactionType,
            Instant createdAt,
            UUID userId,
            String displayName
    ) {
    }

    public record Activity(
            long postId,
            String title,
            Instant lastReactedAt,
            UUID reactionId,
            ReactionType reactionType,
            Instant reactedAt
    ) {
    }
}
