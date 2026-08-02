package com.lilamaris.cozyr.board.application.model.reaction;

import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PostReactionActivity(
        long postId,
        String title,
        Instant lastReactedAt,
        List<Item> reactions
) {
    public PostReactionActivity {
        NumberPrecondition.requireNonNegative(postId, "postId");
        StringPrecondition.requireNonBlank(title, "title");
        ObjectPrecondition.requireNonNull(lastReactedAt, "lastReactedAt");
        reactions = List.copyOf(ObjectPrecondition.requireNonNull(reactions, "reactions"));
    }

    public static PostReactionActivity of(long postId, String title, Instant lastReactedAt, List<Item> reactions) {
        return new PostReactionActivity(postId, title, lastReactedAt, reactions);
    }

    public record Item(
            UUID reactionId,
            ReactionType reactionType,
            Instant reactedAt
    ) {
        public Item {
            ObjectPrecondition.requireNonNull(reactionId, "reactionId");
            ObjectPrecondition.requireNonNull(reactionType, "reactionType");
            ObjectPrecondition.requireNonNull(reactedAt, "reactedAt");
        }

        public static Item of(UUID reactionId, ReactionType reactionType, Instant reactedAt) {
            return new Item(reactionId, reactionType, reactedAt);
        }
    }
}
