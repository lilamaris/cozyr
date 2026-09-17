package com.lilamaris.cozyr.reservation.jdbc.sql;

public class SeatSql {
    public static final String LIST_SUMMARIES = """
            SELECT
                s.id AS seatId,
                s.room_id AS roomId,
                s.created_at AS createdAt,
                s.updated_at AS updatedAt
            FROM seat s
            WHERE s.room_id = :roomId
            ORDER BY s.room_id ASC, s.id ASC, s.created_at DESC
            """;

    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                s.id AS seatId,
                s.room_id AS roomId,
                s.created_at AS createdAt,
                s.updated_at AS updatedAt
            FROM seat s
            WHERE s.id = :seatId
                AND s.room_id = :roomId
            """;
}
