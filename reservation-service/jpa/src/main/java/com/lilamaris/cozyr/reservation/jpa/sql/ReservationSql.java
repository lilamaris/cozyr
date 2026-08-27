package com.lilamaris.cozyr.reservation.jpa.sql;

public class ReservationSql {
    public static final String FIND_DETAIL_BY_ID = """
            SELECT
                r.id AS reservationId,
                r.status AS status,
                r.created_at AS createdAt,
                r.updated_at AS updatedAt,
                o.room_id AS roomId,
                o.seat_id AS seatId,
                rs.id AS slotId,
                rs.start_at AS startAt,
                rs.end_at AS endAt,
                u.user_id AS userId,
                u.display_name AS displayName
            FROM reservation r
            JOIN seat_occupancy o
                ON o.reservation_id = r.id
            JOIN room_schedule_slot rs
                ON rs.id = o.schedule_slot_id
            JOIN user_snapshot u
                ON u.user_id = r.reserved_user_id
            WHERE r.id = :reservationId
            """;
}
