package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class BoardSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                id AS boardId,
                name AS name,
                description AS description,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM board
            WHERE id = :id
            """;

    public static final String LIST_SUMMARY_FIRST_PAGE = """
            SELECT
                id AS boardId,
                name AS name,
                description AS description,
                created_at AS createdAt
            FROM board
            ORDER BY created_at DESC, id DESC
            LIMIT :limit
            """;

    public static final String LIST_SUMMARY_NEXT_PAGE = """
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
