package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionSummary;
import com.lilamaris.cozyr.board.application.model.user.UserProjection;
import com.lilamaris.cozyr.board.domain.ReactionType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PostReactionRow {
    public record Summary(
            long postId,
            UUID userId,
            String displayName,
            ReactionType reactionType
    ) {
        public Optional<UserProjection> toUserProjection() {
            if (userId == null || displayName == null) return Optional.empty();
            return Optional.of(UserProjection.of(userId, displayName));
        }

        public PostReactionSummary toSummary(Map<ReactionType, List<UserProjection>> reactionByUsers) {
            return PostReactionSummary.of(postId, reactionByUsers);
        }
    }
}
