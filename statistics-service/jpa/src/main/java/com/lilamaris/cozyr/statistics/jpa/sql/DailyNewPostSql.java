package com.lilamaris.cozyr.statistics.jpa.sql;

public class DailyNewPostSql {
    public static final String UPSERT = """
            INSERT INTO daily_new_post (board_id, created_date, created_count, created_at, updated_at)
            VALUES (:boardId, :createdDate, :createdCount, :createdAt, :updatedAt)
            ON CONFLICT (board_id, created_date)
            DO UPDATE SET
                created_count = daily_new_post.created_count + EXCLUDED.created_count,
                updated_at = EXCLUDED.updated_at
            """;

}
