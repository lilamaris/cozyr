package com.lilamaris.cozyr.reservation.jdbc.sql;

public class RoomScheduleSlotSql {
    public static final String EXISTS_BY_SLOT_IDS = """
            SELECT COUNT(DISTINCT id) = :slotCount
            FROM room_schedule_slot
            WHERE room_id = :roomId
                AND id IN (:scheduleSlotIds);
            """;
}
