package com.lilamaris.cozyr.board.persistence.jpa.repository;

import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.board.domain.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostReactionRepository extends JpaRepository<PostReaction, UUID> {
    boolean existsByPostIdAndUserIdAndReactionType(long postId, UUID userId, ReactionType reactionType);
}
