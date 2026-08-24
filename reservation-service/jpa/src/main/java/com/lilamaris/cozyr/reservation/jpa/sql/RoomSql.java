package com.lilamaris.cozyr.reservation.jpa.sql;

public class RoomSql {
    public static String LIST_SUMMARIES = """
            SELECT
                r.id AS roomId,
                r.name AS name,
                r.description AS description,
                r.created_at AS createdAt,
                r.updated_at AS updatedAt
            FROM room r
            WHERE 1 = 1
                %s
            ORDER BY r.created_at DESC, r.id DESC
            LIMIT :limit
            """;

    public static String FIND_DETAIL_BY_ID = """
            SELECT
                r.id AS roomId,
                r.name AS name,
                r.description AS description,
                r.created_at AS createdAt,
                r.updated_at AS updatedAt,
                rss.id AS slotId,
                rss.start_at AS startAt,
                rss.end_at AS endAt
            FROM room r
            LEFT JOIN room_schedule_slot rss
                ON rss.room_id = r.id
            WHERE r.id = :roomId
            """;
}
