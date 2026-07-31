package com.lilamaris.cozyr.board.domain;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "post_reaction",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_post_id_user_id_reaction_type",
                columnNames = {"post_id", "user_id", "reaction_type"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "post_id", nullable = false)
    private long postId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    private PostReaction(long postId, UUID userId, ReactionType reactionType, Instant createdAt) {
        this.postId = NumberPrecondition.requireNonNegative(postId, "postId");
        this.userId = ObjectPrecondition.requireNonNull(userId, "userId");
        this.reactionType = ObjectPrecondition.requireNonNull(reactionType, "reactionType");
        this.createdAt = ObjectPrecondition.requireNonNull(createdAt, "createdAt");
    }

    public static PostReaction of(long postId, UUID userId, ReactionType reactionType, Instant createdAt) {
        return new PostReaction(postId, userId, reactionType, createdAt);
    }
}
