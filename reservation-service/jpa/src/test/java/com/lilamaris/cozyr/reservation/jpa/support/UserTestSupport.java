package com.lilamaris.cozyr.reservation.jpa.support;

import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class UserTestSupport {
    private static final UUID FIRST_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID SECOND_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final String INSERT_USER = """
            INSERT INTO user_snapshot (user_id, display_name, last_updated_at)
            VALUES (:userId, :displayName, :now)
            RETURNING user_id
            """;

    private static final String CLEANUP_USER = """
            DELETE FROM user_snapshot WHERE user_id = :userId
            """;

    public static TestContext createContext(JdbcClient jdbcClient, Instant now) {
        var firstUserId = jdbcClient.sql(INSERT_USER)
                .param("userId", FIRST_USER_ID)
                .param("displayName", "First-user")
                .param("now", Timestamp.from(now))
                .query(UUID.class)
                .single();

        var secondUserId = jdbcClient.sql(INSERT_USER)
                .param("userId", SECOND_USER_ID)
                .param("displayName", "Second-user")
                .param("now", Timestamp.from(now))
                .query(UUID.class)
                .single();

        return new TestContext(firstUserId, secondUserId);
    }

    public static void cleanup(JdbcClient jdbcClient, TestContext userContext) {
        jdbcClient.sql(CLEANUP_USER)
                .param("userId", userContext.firstUserId)
                .update();

        jdbcClient.sql(CLEANUP_USER)
                .param("userId", userContext.secondUserId)
                .update();
    }

    public record TestContext(UUID firstUserId, UUID secondUserId) {
    }
}
