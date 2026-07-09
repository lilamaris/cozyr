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
            """;

    public static final String LIST_SUMMARIES = """
            SELECT
                id AS postId,
                title AS title,
                content AS content,
                created_at AS createdAt
            FROM post
            WHERE board_id = :boardId
            """;

    public static final String LIST_SUMMARY_FIRST_PAGE = """
            SELECT
                id AS postId,
                title AS title,
                content AS content,
                created_at AS createdAt
            FROM post
            WHERE board_id = :boardId
            ORDER BY created_at DESC, id DESC
            LIMIT :limit
            """;

    public static final String LIST_SUMMARY_NEXT_PAGE = """
            SELECT
                id AS postId,
                title AS title,
                content AS content,
                created_at AS createdAt
            FROM post
            WHERE board_id = :boardId
                AND (
                    :lastCreatedAt IS NULL
                    OR created_at < :lastCreatedAt
                    OR (created_at = :lastCreatedAt AND id < :lastPostId)
                )
            ORDER BY created_at DESC, id DESC
            LIMIT :limit
            """;
}
