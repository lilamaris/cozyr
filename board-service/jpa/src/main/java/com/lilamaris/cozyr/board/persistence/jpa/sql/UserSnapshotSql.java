package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class UserSnapshotSql {
    public static final String UPSERT = """
            INSERT INTO user_snapshot (user_id, display_name, last_updated_at)
            VALUES (:userId, :displayName, :lastUpdatedAt)
            ON CONFLICT (user_id)
            DO UPDATE SET
                display_name = EXCLUDED.display_name,
                last_updated_at = EXCLUDED.last_updated_at
            RETURNING user_id, display_name, last_updated_at
            """;
}
