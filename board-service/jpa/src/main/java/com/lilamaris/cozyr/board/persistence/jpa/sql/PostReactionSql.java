package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class PostReactionSql {
    public static final String FIND_SUMMARIES = """
            SELECT
                r.post_id AS postId,
                r.reaction_type AS reactionType,
                r.created_at AS createdAt,
                u.user_id AS userId,
                u.display_name AS displayName
            FROM post_reaction r
            JOIN user_snapshot u
                ON u.user_id = r.user_id
            WHERE 1 = 1
                %s
            """;
}
