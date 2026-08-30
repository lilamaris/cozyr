package com.lilamaris.cozyr.reservation.jpa.sql;

public class SeatOccupancySql {
    public static final String INSERT_BY_SCHEDULE_SLOT_IDS = """
            WITH requested_slot_ids AS (
                SELECT unnest(:scheduleSlotIds) AS slot_id
            )
            INSERT INTO seat_occupancy (
                reservation_id,
                occupancy_date,
                room_id,
                seat_id,
                schedule_slot_id,
                released_at
            )
            SELECT
                :reservationId,
                :occupancyDate,
                :roomId,
                :seatId,
                r.slot_id,
                null
            FROM requested_slot_ids r
            WHERE NOT EXISTS (
                SELECT 1
                FROM seat_occupancy o
                WHERE o.room_id = :roomId
                    AND o.seat_id = :seatId
                    AND o.occupancy_date = :occupancyDate
                    AND o.released_at IS NULL
                    AND o.schedule_slot_id IN (
                        SELECT slot_id
                        FROM requested_slot_ids
                    )
            )
            """;

    public static final String RELEASE_BY_RESERVATION_ID = """
            UPDATE seat_occupancy
            SET released_at = :releasedAt
            WHERE reservation_id = :reservationId
                AND released_at IS NULL
            """;
}