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

    public static final String LIST_SUMMARIES = """
            SELECT
                id AS boardId,
                name AS name,
                description AS description,
                created_at AS createdAt
            FROM board
            WHERE 1 = 1
            """;
}
