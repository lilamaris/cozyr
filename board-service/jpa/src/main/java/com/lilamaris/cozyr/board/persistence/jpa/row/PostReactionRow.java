package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.domain.ReactionType;

import java.time.Instant;
import java.util.UUID;

public record PostReactionRow(
        long postId,
        ReactionType reactionType,
        Instant createdAt,
        UUID userId,
        String displayName
) {
}
