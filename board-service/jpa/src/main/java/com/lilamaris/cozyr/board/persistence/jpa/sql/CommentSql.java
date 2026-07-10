package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class CommentSql {
    public static final String LIST_ROOT_BY_POST_ID = """
            SELECT
                id AS commentId,
                post_id AS postId,
                parent_id AS parentId,
                content AS content,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM comment
            WHERE post_id = :postId
                AND parent_id IS NULL
            """;

    public static final String LIST_REPLIES_BY_PARENT_ID = """
            SELECT
                id AS commentId,
                parent_id AS parentId,
                post_id AS postId,
                content AS content,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM comment
            WHERE parent_id = :parentId
            """;
}
