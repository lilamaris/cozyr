package com.lilamaris.cozyr.reservation.jdbc.sql;

public class DailyUsageSql {
    public static final String INCREASE_RESERVATION_COUNT = """
            INSERT INTO daily_reservation_usage (
                user_id,
                room_id,
                reservation_date,
                reservation_count
            ) VALUES (
                :userId,
                :roomId,
                :reservationDate,
                1
            )
            ON CONFLICT (user_id, room_id, reservation_date)
            DO UPDATE
                SET reservation_count = daily_reservation_usage.reservation_count + 1
            WHERE daily_reservation_usage.reservation_count < :maxCount
            """;

    public static final String DECREASE_RESERVATION_COUNT = """
            UPDATE daily_reservation_usage
            SET reservation_count = reservation_count - 1
            WHERE user_id = :userId
                AND room_id = :roomId
                AND reservation_date = :reservationDate
                AND reservation_count > 0
            """;
}
