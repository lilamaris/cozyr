package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.cozyr.board.application.port.out.PostReactionStore;
import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.cozyr.board.persistence.jpa.repository.PostReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostReactionJpaAdapter implements PostReactionReader, PostReactionStore {
    private final PostReactionRepository repository;

    @Override
    public Optional<PostReaction> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public boolean existsReaction(long postId, UUID userId, ReactionType type) {
        return repository.existsByPostIdAndUserIdAndReactionType(postId, userId, type);
    }

    @Override
    public PostReaction save(PostReaction postReaction) {
        return repository.save(postReaction);
    }

    @Override
    public void delete(PostReaction postReaction) {
        repository.delete(postReaction);
    }
}
