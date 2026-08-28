package com.lilamaris.cozyr.reservation.jdbc.sql;

public class SeatSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                s.room_id AS roomId,
                s.seat_id AS seatId,
                s.created_at AS createdAt,
                s.updated_at AS updatedAt
            FROM seat s
            WHERE s.room_id = :roomId
                AND s.seat_id = :seatId
            """;
}
