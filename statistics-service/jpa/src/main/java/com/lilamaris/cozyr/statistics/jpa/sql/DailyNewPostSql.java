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

    public static final String FIND_STATISTICS_BY_BOARD_ID = """
            SELECT
                schedule.date AS date,
                COALESCE(stat.created_count, 0) AS count
            FROM (
                SELECT generate_series(
                    :from::date,
                    :to::date,
                    interval '1 day'
                )::date AS date
            ) schedule
            LEFT JOIN daily_new_post stat
                ON stat.created_date = schedule.date
                AND stat.board_id = :boardId
            ORDER BY schedule.date
            """;
}
