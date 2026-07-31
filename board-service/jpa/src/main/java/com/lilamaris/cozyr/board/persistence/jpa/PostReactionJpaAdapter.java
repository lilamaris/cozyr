package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionSummary;
import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.cozyr.board.application.port.out.PostReactionStore;
import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.cozyr.board.persistence.jpa.repository.PostReactionRepository;
import com.lilamaris.cozyr.board.persistence.jpa.row.PostReactionRow;
import com.lilamaris.cozyr.board.persistence.jpa.sql.PostReactionSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostReactionJpaAdapter implements PostReactionReader, PostReactionStore {
    private final PostReactionRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public Optional<PostReaction> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<PostReactionSummary> findSummaries(long postId) {
        var rows = jdbcClient.sql(PostReactionSql.FIND_SUMMARIES)
                .param("postId", postId)
                .query(PostReactionRow.Summary.class)
                .list();

        if (rows.isEmpty()) return Optional.empty();

        var first = rows.getFirst();
        if (first == null) return Optional.empty();

        var reactionByUsers = rows.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        PostReactionRow.Summary::reactionType,
                        Collectors.flatMapping(
                                summary -> summary.toUserProjection().stream(),
                                Collectors.toList()
                        )
                ));

        return Optional.of(first.toSummary(reactionByUsers));
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
