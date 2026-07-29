package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.cozyr.board.domain.Post;
import com.lilamaris.cozyr.board.persistence.jpa.repository.PostRepository;
import com.lilamaris.cozyr.board.persistence.jpa.row.PostRow;
import com.lilamaris.cozyr.board.persistence.jpa.sql.PostSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostReaderJpaAdapter implements PostReader {
    private final PostRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Post> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<PostDetail> findDetailById(long id) {
        return jdbcClient.sql(PostSql.FIND_DETAIL_BY_ID)
                .param("id", id)
                .query(PostRow.Detail.class)
                .optional()
                .map(PostRow.Detail::toDetail);
    }

    @Override
    public CursorResult<PostSummary, PostCursor> findSummaries(UUID boardId, PostFilter filter, CursorRequest<PostCursor> request) {
        var sql = new StringBuilder(PostSql.LIST_SUMMARIES);
        var params = new MapSqlParameterSource();

        appendFilterCondition(sql, params, filter);
        appendCursorCondition(sql, params, request.cursor());

        sql.append("""
                ORDER BY created_at DESC, id DESC
                LIMIT :limit
                """);
        params.addValue("boardId", boardId);
        params.addValue("limit", request.size() + 1);

        var rows = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query(PostRow.Summary.class)
                .list();

        boolean hasNext = rows.size() > request.size();

        var content = rows.stream()
                .limit(request.size())
                .filter(Objects::nonNull)
                .map(PostRow.Summary::toSummary)
                .toList();

        PostCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> PostCursor.of(last.createdAt(), last.postId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }

    private void appendFilterCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            PostFilter filter
    ) {
        Optional.ofNullable(filter.title())
                .ifPresent(title -> {
                    sql.append("""
                            AND title ILIKE :title ESCAPE '\\'
                            """);
                    params.addValue("title", "%" + escapeLike(title) + "%");
                });

        Optional.ofNullable(filter.content())
                .ifPresent(content -> {
                    sql.append("""
                            AND content ILIKE :content ESCAPE '\\'
                            """);
                    params.addValue("content", "%" + escapeLike(content) + "%");
                });
    }

    private void appendCursorCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            @Nullable PostCursor cursor
    ) {
        Optional.ofNullable(cursor)
                .ifPresent(title -> {
                    sql.append("""
                            AND (
                                created_at <: cursorCreatedAt
                                OR (created_at = :cursorCreatedAt AND id < :cursorId)
                            )
                            """);

                    params.addValue("cursorCreatedAt", cursor.createdAt());
                    params.addValue("cursorId", cursor.postId());
                });
    }

    private String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
