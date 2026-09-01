package com.lilamaris.cozyr.reservation.jdbc.sql;

public class RoomContextSql {
    public static final String FIND_BY_ROOM_ID = """
            SELECT
                r.id AS roomId,
                p.id AS roomOpPolicyId,
                p.max_reservation_per_user_per_day AS maxReservationPerUserPerDay,
                p.max_schedule_per_reservation AS maxSchedulePerReservation,
                p.updated_at AS updatedAt
            FROM room r
            JOIN room_op_policy p
                ON r.id = p.room_id
            WHERE r.id = :roomId
            """;
}
