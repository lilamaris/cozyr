package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class CommentSql {
    public static final String LIST_DETAILS = """
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
            WHERE 1 = 1
            """;
}
