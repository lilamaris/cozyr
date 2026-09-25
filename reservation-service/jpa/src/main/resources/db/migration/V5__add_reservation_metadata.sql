ALTER TABLE reservation
    ADD COLUMN room_id BIGINT,
    ADD COLUMN seat_id VARCHAR(10),
    ADD COLUMN occupancy_date DATE;

-- Abort instead of choosing an arbitrary occupancy when legacy data is inconsistent.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM reservation r
        LEFT JOIN seat_occupancy o ON o.reservation_id = r.id
        GROUP BY r.id
        HAVING COUNT(o.id) = 0
            OR COUNT(DISTINCT (o.room_id, o.seat_id, o.occupancy_date)) <> 1
    ) THEN
        RAISE EXCEPTION
            'Cannot backfill reservation metadata: a reservation has no occupancy or has inconsistent seat/date values';
    END IF;
END $$;

UPDATE reservation r
SET room_id = source.room_id,
    seat_id = source.seat_id,
    occupancy_date = source.occupancy_date
FROM (
    SELECT reservation_id,
           MIN(room_id) AS room_id,
           MIN(seat_id) AS seat_id,
           MIN(occupancy_date) AS occupancy_date
    FROM seat_occupancy
    GROUP BY reservation_id
) source
WHERE source.reservation_id = r.id;

ALTER TABLE reservation
    ALTER COLUMN room_id SET NOT NULL,
    ALTER COLUMN seat_id SET NOT NULL,
    ALTER COLUMN occupancy_date SET NOT NULL;