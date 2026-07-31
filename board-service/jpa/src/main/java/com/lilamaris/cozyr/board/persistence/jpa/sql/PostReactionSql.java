package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class PostReactionSql {
    public static final String FIND_SUMMARIES = """
            SELECT
                p.id AS postId,
                r.reaction_type AS reactionType,
                u.user_id AS userId,
                u.display_name AS displayName
            FROM post p
            LEFT JOIN post_reaction r
                ON r.post_id = p.id
            LEFT JOIN user_snapshot u
                ON u.user_id = r.user_id
            WHERE p.id = :postId
                AND p.deleted = false
            """;
}
