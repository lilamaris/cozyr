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
                u.user_id AS authorUserId,
                u.display_name AS displayName
            FROM post p
            JOIN user_snapshot u
                ON u.user_id = p.author_user_id
            WHERE id = :id
                AND deleted = false
            """;

    public static final String LIST_SUMMARIES = """
            SELECT
                p.id AS postId,
                p.title AS title,
                p.content AS content,
                p.created_at AS createdAt,
                u.user_id AS authorUserId,
                u.display_name AS displayName
            FROM post p
            JOIN user_snapshot u
                ON u.user_id = p.author_user_id 
            WHERE board_id = :boardId
                AND deleted = false
            """;
}
