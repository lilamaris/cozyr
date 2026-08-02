package com.lilamaris.cozyr.board.persistence.jpa.sql;

public class PostReactionSql {
    public static final String FIND_SUMMARIES = """
            SELECT
                r.post_id AS postId,
                r.reaction_type AS reactionType,
                r.created_at AS createdAt,
                u.user_id AS userId,
                u.display_name AS displayName
            FROM post_reaction r
            JOIN user_snapshot u
                ON u.user_id = r.user_id
            WHERE 1 = 1
                %s
            """;

    public static final String LIST_ACTIVITIES = """
            WITH grouped AS (
                SELECT
                    r.post_id,
                    MAX(r.created_at) AS last_reacted_at
                FROM post_reaction r
                JOIN post p
                    ON p.id = r.post_id
                    AND p.deleted = false
                WHERE r.user_id = :userId
                GROUP BY r.post_id
            ),
            paged AS (
                SELECT *
                FROM grouped
                WHERE :cursorReactedAt::timestamp IS NULL
                    OR (last_reacted_at, post_id) < (:cursorReactedAt::timestamp, :cursorPostId::bigint)
                ORDER BY last_reacted_at DESC, post_id DESC
                LIMIT :limit
            )
            SELECT
                p.id AS postId,
                p.title,
                paged.last_reacted_at AS lastReactedAt,
                r.id AS reactionId,
                r.reaction_type AS reactionType,
                r.created_at AS reactedAt
            FROM paged
            JOIN post p
                ON p.id = paged.post_id
            JOIN post_reaction r
                ON r.post_id = paged.post_id
                AND r.user_id = :userId
            ORDER BY paged.last_reacted_at DESC, paged.post_id DESC, r.created_at DESC
            """;
}
