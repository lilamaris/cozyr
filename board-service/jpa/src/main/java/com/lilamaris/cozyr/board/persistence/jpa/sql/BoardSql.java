package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class BoardSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                b.id AS boardId,
                b.name AS name,
                b.description AS description,
                b.created_at AS createdAt,
                b.updated_at AS updatedAt,
                c.id AS categoryId,
                c.name AS categoryName
            FROM board b
            LEFT JOIN category c
                ON c.board_id = b.id
            WHERE b.id = :id
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
