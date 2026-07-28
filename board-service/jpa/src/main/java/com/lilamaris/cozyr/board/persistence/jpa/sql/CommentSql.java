package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class CommentSql {
    public static final String LIST_ROOT_BY_POST_ID = """
            SELECT
                c.id AS commentId,
                c.post_id AS postId,
                c.parent_id AS parentId,
                CASE
                    WHEN c.deleted = true THEN 'Deleted'
                    ELSE c.content
                END AS content,
                c.created_at AS createdAt,
                c.updated_at AS updatedAt,
                u.user_id AS authorUserId,
                u.display_name AS displayName
            FROM comment c
            JOIN user_snapshot u
                ON u.user_id = c.author_user_id
            WHERE post_id = :postId
                AND parent_id IS NULL
            """;

    public static final String LIST_REPLIES_BY_PARENT_ID = """
            SELECT
                c.id AS commentId,
                c.parent_id AS parentId,
                c.post_id AS postId,
                CASE
                    WHEN c.deleted = true THEN 'Deleted'
                    ELSE c.content
                END AS content,
                c.created_at AS createdAt,
                c.updated_at AS updatedAt,
                u.user_id AS authorUserId,
                u.display_name AS displayName
            FROM comment c
            JOIN user_snapshot u
                ON u.user_id = c.author_user_id
            WHERE parent_id = :parentId
            """;
}
