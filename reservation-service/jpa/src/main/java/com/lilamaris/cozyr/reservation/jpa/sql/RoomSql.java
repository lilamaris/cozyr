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
                r.updated_at AS updatedAt
            FROM room r
            WHERE r.id = :roomId
            """;
}
