package com.lilamaris.cozyr.identity.jpa.sql;

public class UserSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                id AS userId,
                display_name AS displayName,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM cozyr_user
            WHERE id = :userId
            """;
}
