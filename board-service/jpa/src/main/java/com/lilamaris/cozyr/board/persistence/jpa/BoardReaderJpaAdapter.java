package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.model.board.BoardFilter;
import com.lilamaris.cozyr.board.application.model.board.BoardSummary;
import com.lilamaris.cozyr.board.application.model.cursor.CursorRequest;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.out.BoardReader;
import com.lilamaris.cozyr.board.domain.Board;
import com.lilamaris.cozyr.board.persistence.jpa.repository.BoardRepository;
import com.lilamaris.cozyr.board.persistence.jpa.sql.BoardSql;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardReaderJpaAdapter implements BoardReader {
    private final BoardRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public Optional<Board> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<BoardDetail> findDetailById(UUID id) {
        return jdbcClient.sql(BoardSql.FIND_DETAIL_BY_ID)
                .param("id", id)
                .query(BoardDetail.class)
                .optional();
    }

    @Override
    public CursorResult<BoardSummary, BoardCursor> findSummaries(BoardFilter filter, CursorRequest<BoardCursor> request) {
        var sql = new StringBuilder(BoardSql.LIST_SUMMARIES);
        var params = new MapSqlParameterSource();

        appendFilterCondition(sql, params, filter);
        appendCursorCondition(sql, params, request.cursor());

        sql.append("""
                ORDER BY created_at DESC, id DESC
                LIMIT :limit
                """);
        params.addValue("limit", request.size() + 1);

        var rows = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query(BoardSummary.class)
                .list();

        boolean hasNext = rows.size() > request.size();

        var content = rows.stream().limit(request.size()).toList();

        BoardCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> BoardCursor.of(last.createdAt(), last.boardId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }

    private void appendFilterCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            BoardFilter filter
    ) {
        Optional.ofNullable(filter.name())
                .ifPresent(name -> {
                    sql.append("""
                            AND name ILIKE :name ESCAPE '\\'
                            """);
                    params.addValue("name", "%" + escapeLike(name) + "%");
                });

        Optional.ofNullable(filter.description())
                .ifPresent(description -> {
                    sql.append("""
                            AND description ILIKE :description ESCAPE '\\'
                            """);
                    params.addValue("description", "%" + escapeLike(description) + "%");
                });
    }

    private void appendCursorCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            @Nullable BoardCursor cursor
    ) {
        Optional.ofNullable(cursor)
                .ifPresent(c -> {
                    sql.append("""
                            AND (
                                created_at < :cursorCreatedAt
                                OR (created_at = :cursorCreatedAt AND id < :cursorId)
                            )
                            """);

                    params.addValue("cursorCreatedAt", cursor.createdAt());
                    params.addValue("cursorId", cursor.boardId());
                });
    }

    private String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
