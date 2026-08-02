package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionActivity;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionCursor;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionFilter;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionSummary;
import com.lilamaris.cozyr.board.application.model.user.UserProjection;
import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.cozyr.board.application.port.out.PostReactionStore;
import com.lilamaris.cozyr.board.domain.PostReaction;
import com.lilamaris.cozyr.board.domain.ReactionType;
import com.lilamaris.cozyr.board.persistence.jpa.repository.PostReactionRepository;
import com.lilamaris.cozyr.board.persistence.jpa.row.PostReactionRow;
import com.lilamaris.cozyr.board.persistence.jpa.sql.PostReactionSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;
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

    public Optional<PostReactionSummary> findSummaries(PostReactionFilter filter) {
        var conditions = new ArrayList<String>();
        var params = new MapSqlParameterSource();
        appendFilter(conditions, params, filter);

        var dynamicWhere = conditions.isEmpty()
                ? ""
                : "AND " + String.join(" AND ", conditions);

        var sql = PostReactionSql.FIND_SUMMARIES.formatted(dynamicWhere);

        var rows = jdbcClient.sql(sql)
                .paramSource(params)
                .query(PostReactionRow.Single.class)
                .list();

        if (rows.isEmpty()) return Optional.empty();

        var first = rows.getFirst();
        if (first == null) return Optional.empty();

        var reactionByUsers = rows.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        PostReactionRow.Single::reactionType,
                        Collectors.mapping(
                                r -> UserProjection.of(r.userId(), r.displayName()),
                                Collectors.toList()
                        )
                ));

        return Optional.of(
                PostReactionSummary.of(first.postId(), reactionByUsers)
        );
    }

    @Override
    public List<PostReactionActivity> findActivities(UUID userId, CursorRequest<PostReactionCursor> request) {
        var rows = jdbcClient.sql(PostReactionSql.LIST_ACTIVITIES)
                .param("userId", userId)
                .param("cursorReactedAt", request.cursor() == null ? null : request.cursor().lastReactedAt().atOffset(ZoneOffset.UTC), Types.TIMESTAMP_WITH_TIMEZONE)
                .param("cursorPostId", request.cursor() == null ? null : request.cursor().postId())
                .param("limit", request.size())
                .query(PostReactionRow.Activity.class)
                .list();

        return toActivities(rows);
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

    private List<PostReactionActivity> toActivities(List<PostReactionRow.Activity> rows) {
        return rows.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        row -> new ActivityKey(row.postId(), row.title(), row.lastReactedAt()),
                        LinkedHashMap::new,
                        Collectors.mapping(this::toActivityItem, Collectors.toList())
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    var key = entry.getKey();
                    return PostReactionActivity.of(key.postId(), key.title(), key.lastReactedAt(), entry.getValue());
                })
                .toList();
    }

    private PostReactionActivity.Item toActivityItem(PostReactionRow.Activity row) {
        return PostReactionActivity.Item.of(row.reactionId(), row.reactionType(), row.reactedAt());
    }

    private void appendFilter(List<String> conditions, MapSqlParameterSource params, PostReactionFilter filter) {
        Optional.ofNullable(filter.postId()).ifPresent(postId -> {
            conditions.add("r.post_id = :postId");
            params.addValue("postId", postId);
        });

        Optional.ofNullable(filter.userId()).ifPresent(userId -> {
            conditions.add("r.user_id = :userId");
            params.addValue("userId", userId);
        });

        Optional.ofNullable(filter.reactionType()).ifPresent(reactionType -> {
            conditions.add("r.reaction_type = :reactionType");
            params.addValue("reactionType", reactionType);
        });
    }

    private record ActivityKey(
            long postId,
            String title,
            Instant lastReactedAt
    ) {
    }
}
