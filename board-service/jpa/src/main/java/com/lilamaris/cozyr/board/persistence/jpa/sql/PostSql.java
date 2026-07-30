package com.lilamaris.cozyr.board.persistence.jpa.sql;

public final class PostSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                p.id AS postId,
                p.board_id AS boardId,
                p.title AS title,
                p.content AS content,
                p.created_at AS createdAt,
                p.updated_at AS updatedAt,
                c.id AS categoryId,
                c.name AS categoryName,
                u.user_id AS authorUserId,
                u.display_name AS displayName
            FROM post p
            JOIN user_snapshot u
                ON u.user_id = p.author_user_id
            JOIN category c
                ON c.id = p.category_id
            WHERE p.id = :id
                AND p.deleted = false
            """;

    public static final String LIST_SUMMARIES = """
            WITH paged_post AS (
                SELECT
                    p.id,
                    p.title,
                    p.content,
                    p.created_at,
                    p.category_id,
                    p.author_user_id
                FROM post p
                WHERE p.board_id = :boardId
                    AND p.deleted = false
                    %s
                ORDER BY p.created_at DESC, p.id DESC
                LIMIT :limit
            )
            SELECT
                p.id AS postId,
                p.title AS title,
                p.content AS content,
                p.created_at AS createdAt,
                c.id AS categoryId,
                c.name AS categoryName,
                u.user_id AS authorUserId,
                u.display_name AS displayName
            FROM paged_post p
            JOIN user_snapshot u
                ON u.user_id = p.author_user_id
            JOIN category c
                ON c.id = p.category_id
            ORDER BY p.created_at DESC, p.id DESC
            """;
}
