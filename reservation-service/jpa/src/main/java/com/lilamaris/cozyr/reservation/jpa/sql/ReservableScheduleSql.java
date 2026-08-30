package com.lilamaris.cozyr.reservation.jpa.sql;

public class ReservableScheduleSql {
    public static final String FIND_BY_SEAT = """
            SELECT
                s.id,
                s.start_at,
                s.end_at
            FROM room_schedule_slot s
            WHERE s.room_id = :roomId
                AND (:occupancyDate::date + s.start_at) > :now
                AND NOT EXISTS (
                    SELECT 1
                    FROM seat_occupancy o
                    WHERE o.room_id = :roomId
                        AND o.seat_id = :seatId
                        AND o.occupancy_date = :occupancyDate
                        AND o.schedule_slot_id = s.id
                )
            """;
}
