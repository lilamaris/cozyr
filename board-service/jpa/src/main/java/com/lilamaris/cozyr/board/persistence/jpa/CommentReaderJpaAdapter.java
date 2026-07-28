package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.comment.CommentFilter;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.cozyr.board.domain.Comment;
import com.lilamaris.cozyr.board.persistence.jpa.repository.CommentRepository;
import com.lilamaris.cozyr.board.persistence.jpa.row.CommentRow;
import com.lilamaris.cozyr.board.persistence.jpa.sql.CommentSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentReaderJpaAdapter implements CommentReader {
    private final CommentRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public Optional<Comment> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CursorResult<CommentDetail, CommentCursor> findByPostId(CommentFilter filter, CursorRequest<CommentCursor> request) {
        var sql = new StringBuilder(CommentSql.LIST_DETAILS);
        var params = new MapSqlParameterSource();

        appendFilterCondition(sql, params, filter);
        appendCursorCondition(sql, params, request);

        var rows = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query(CommentRow.Detail.class)
                .list();

        return buildCursorResult(rows, request);
    }

    private void appendFilterCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            CommentFilter filter
    ) {
        Optional.ofNullable(filter.postId())
                .ifPresent(postId -> {
                    sql.append("""
                            AND post_id = :postId
                            """);
                    params.addValue("postId", postId);
                });

        Optional.ofNullable(filter.parentId())
                .ifPresentOrElse(
                        parentId -> {
                            sql.append("""
                                    AND parent_id = :parentId
                                    """);
                            params.addValue("parentId", parentId);
                        },
                        () -> sql.append("""
                                AND parent_id IS NULL
                                """)
                );


        Optional.ofNullable(filter.authorUserId())
                .ifPresent(authorUserId -> {
                    sql.append("""
                            AND author_user_id = :authorUserId
                            """);
                    params.addValue("authorUserId", authorUserId);
                });
    }

    private void appendCursorCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            CursorRequest<CommentCursor> request
    ) {
        var cursor = request.cursor();

        Optional.ofNullable(cursor)
                .ifPresent(c -> {
                    sql.append("""
                            AND (
                                created_at < :cursorCreatedAt
                                OR (created_at = :cursorCreatedAt AND id < :cursorId)
                            )
                            """);

                    params.addValue("cursorCreatedAt", cursor.createdAt());
                    params.addValue("cursorId", cursor.commentId());
                });

        sql.append("""
                ORDER BY created_at DESC, id DESC
                LIMIT :limit
                """);
        params.addValue("limit", request.size() + 1);
    }

    private CursorResult<CommentDetail, CommentCursor> buildCursorResult(List<CommentRow.Detail> rows, CursorRequest<CommentCursor> request) {
        var hasNext = rows.size() > request.size();
        var content = rows.stream()
                .limit(request.size())
                .map(CommentRow.Detail::toDetail)
                .toList();

        CommentCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> CommentCursor.of(last.createdAt(), last.commentId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }
}
