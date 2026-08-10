package com.lilamaris.cozyr.statistics.jpa.sql;

public class DailyNewCommentSql {
    public static final String UPSERT = """
            INSERT INTO daily_new_comment (post_id, created_date, created_count, created_at, updated_at)
            VALUES (:postId, :createdDate, :createdCount, :createdAt, :updatedAt)
            ON CONFLICT (post_id, created_date)
            DO UPDATE SET
                created_count = daily_new_comment.created_count + EXCLUDED.created_count,
                updated_at = EXCLUDED.updated_at
            """;
}
