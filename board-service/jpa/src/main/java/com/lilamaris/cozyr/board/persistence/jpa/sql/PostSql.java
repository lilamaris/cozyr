package com.lilamaris.cozyr.board.persistence.jpa.sql;

public final class PostSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                id AS postId,
                board_id AS boardId,
                title AS title,
                content AS content,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM post
            WHERE id = :id
                AND deleted = false
            """;

    public static final String LIST_SUMMARIES = """
            SELECT
                id AS postId,
                title AS title,
                content AS content,
                created_at AS createdAt
            FROM post
            WHERE board_id = :boardId
                AND deleted = false
            """;
}
