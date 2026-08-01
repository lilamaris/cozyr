package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class PostViewSql {
    public static final String INCREASE = """
            INSERT INTO post_view (post_id, count, last_viewed_at)
            VALUES (:postId, 1, :viewedAt)
            ON CONFLICT (post_id)
            DO UPDATE SET
                count = post_view.count + 1,
                last_viewed_at = EXCLUDED.last_viewed_at;
            """;
}
