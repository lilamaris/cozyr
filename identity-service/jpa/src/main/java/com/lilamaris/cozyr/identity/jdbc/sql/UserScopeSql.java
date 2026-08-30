package com.lilamaris.cozyr.identity.jdbc.sql;

public class UserScopeSql {
    public static final String INSERT_SCOPE = """
            INSERT INTO user_scope (
                user_id,
                service,
                role,
                created_at
            ) VALUES (
                :userId,
                :service,
                :role,
                :createdAt
            )
            """;
}
