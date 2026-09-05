package com.lilamaris.cozyr.reservation.jdbc.sql;

public class RoomScheduleSlotSql {
    public static final String FIND_ALL_BY_SLOT_IDS = """
            SELECT
                rs.id AS slotId,
                rs.start_at AS startAt,
                rs.end_at AS endAt
            FROM room_schedule_slot rs
            WHERE rs.room_id = :roomId
                AND rs.id IN (:scheduleSlotIds)
            ORDER BY rs.start_at, rs.end_at, rs.id
            """;
}
