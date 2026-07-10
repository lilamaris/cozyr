package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.comment.CommentCursor;
import com.lilamaris.cozyr.board.application.model.comment.CommentDetail;
import com.lilamaris.cozyr.board.application.model.cursor.CursorRequest;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.out.CommentReader;
import com.lilamaris.cozyr.board.domain.Comment;
import com.lilamaris.cozyr.board.persistence.jpa.repository.CommentRepository;
import com.lilamaris.cozyr.board.persistence.jpa.sql.CommentSql;
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
    public CursorResult<CommentDetail, CommentCursor> findByPostId(Long postId, CursorRequest<CommentCursor> request) {
        var sql = new StringBuilder(CommentSql.LIST_ROOT_BY_POST_ID);
        var params = new MapSqlParameterSource();
        params.addValue("postId", postId);

        appendCursorCondition(sql, params, request);

        var rows = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query(CommentDetail.class)
                .list();

        return buildCursorResult(rows, request);
    }

    @Override
    public CursorResult<CommentDetail, CommentCursor> findReplies(Long parentId, CursorRequest<CommentCursor> request) {
        var sql = new StringBuilder(CommentSql.LIST_REPLIES_BY_PARENT_ID);
        var params = new MapSqlParameterSource();
        params.addValue("parentId", parentId);

        appendCursorCondition(sql, params, request);

        var rows = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query(CommentDetail.class)
                .list();

        return buildCursorResult(rows, request);
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
        params.addValue("limit", request.size());
    }

    private CursorResult<CommentDetail, CommentCursor> buildCursorResult(List<CommentDetail> rows, CursorRequest<CommentCursor> request) {
        var hasNext = rows.size() > request.size();
        var content = rows.stream().limit(request.size()).toList();

        CommentCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> CommentCursor.of(last.createdAt(), last.commentId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }
}
