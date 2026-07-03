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
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
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
        var sql = """
                SELECT
                    id AS boardId,
                    name AS name,
                    description AS description,
                    created_at AS createdAt,
                    updated_at AS updatedAt
                FROM board
                WHERE id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(BoardDetail.class)
                .optional();
    }

    @Override
    public CursorResult<BoardSummary, BoardCursor> findSummaries(BoardFilter filter, CursorRequest<BoardCursor> request) {
        var cursor = request.cursor();

        var sql = cursor == null
                ? boardFirstPageSql()
                : boardNextPageSql();

        var rows = jdbcClient.sql(sql)
                .param("lastCreatedAt", cursor == null ? null : cursor.createdAt().atOffset(ZoneOffset.UTC))
                .param("lastBoardId", cursor == null ? null : cursor.boardId())
                .param("limit", request.size() + 1)
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

    private String boardFirstPageSql() {
        return """
                SELECT
                    id AS boardId,
                    name AS name,
                    description AS description,
                    created_at AS createdAt
                FROM board
                ORDER BY created_at DESC, id DESC
                LIMIT :limit
                """;
    }

    private String boardNextPageSql() {
        return """
                SELECT
                    id AS boardId,
                    name AS name,
                    description AS description,
                    created_at AS createdAt
                FROM board
                WHERE
                    (
                        :lastCreatedAt IS NULL
                        OR created_at < :lastCreatedAt
                        OR (created_at = :lastCreatedAt AND id < :lastBoardId)
                    )
                ORDER BY created_at DESC, id DESC
                LIMIT :limit
                """;
    }
}
