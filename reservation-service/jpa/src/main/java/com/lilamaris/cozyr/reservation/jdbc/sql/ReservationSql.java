package com.lilamaris.cozyr.reservation.jdbc.sql;

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

    public static final String LIST_SUMMARIES = """
            WITH filtered AS (
                SELECT
                    r.id,
                    r.reserved_user_id,
                    r.status,
                    r.created_at,
                    r.updated_at
                FROM reservation r
                WHERE 1 = 1
                    %s
                ORDER BY r.created_at DESC, r.id DESC
                LIMIT :limit
            ),
            occupancy_summary AS (
                SELECT
                    o.reservation_id,
                    o.room_id,
                    o.seat_id,
                    COUNT(*) AS occupied_slot_count
                FROM seat_occupancy o
                JOIN filtered f
                    ON f.id = o.reservation_id
                GROUP BY o.reservation_id, o.room_id, o.seat_id
            )
            SELECT
                f.id AS reservationId,
                f.status AS status,
                f.created_at AS createdAt,
                f.updated_at AS updatedAt,
                os.room_id AS roomId,
                os.seat_id AS seatId,
                COALESCE(os.occupied_slot_count, 0) AS occupiedSlotCount,
                u.user_id AS userId,
                u.display_name AS displayName
            FROM filtered f
            LEFT JOIN occupancy_summary os
                ON os.reservation_id = f.id
            JOIN user_snapshot u
                ON u.user_id = f.reserved_user_id
            ORDER BY f.created_at DESC, f.id DESC
            """;

    public static final String CANCEL_BY_ID = """
            UPDATE reservation
            SET status = 'CANCELED',
                updated_at = :canceledAt
            WHERE id = :reservationId
                AND status = 'RESERVED'
            """;
}