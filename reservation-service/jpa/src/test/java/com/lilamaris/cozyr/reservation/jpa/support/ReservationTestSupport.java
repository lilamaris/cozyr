package com.lilamaris.cozyr.reservation.jpa.support;

import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.Set;
import java.util.UUID;

public class ReservationTestSupport {
    private static final String DELETE_SEAT_OCCUPANCY = """
            DELETE FROM seat_occupancy
            WHERE reservation_id IN (
                SELECT r.id
                FROM reservation r
                WHERE r.reserved_user_id IN (:userIds)
            )
            """;

    private static final String DELETE_RESERVATION = """
            DELETE FROM reservation WHERE reserved_user_id IN (:userIds)
            """;

    private static final String DELETE_DAILY_RESERVATION_USAGE = """
            DELETE FROM daily_reservation_usage WHERE user_id IN (:userIds)
            """;

    public static void cleanup(JdbcClient jdbcClient, Set<UUID> userIds) {
        jdbcClient.sql(DELETE_SEAT_OCCUPANCY)
                .param("userIds", userIds)
                .update();

        jdbcClient.sql(DELETE_RESERVATION)
                .param("userIds", userIds)
                .update();

        jdbcClient.sql(DELETE_DAILY_RESERVATION_USAGE)
                .param("userIds", userIds)
                .update();
    }
}
