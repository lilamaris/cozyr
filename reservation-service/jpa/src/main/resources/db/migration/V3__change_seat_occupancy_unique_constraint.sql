ALTER TABLE seat_occupancy
    ADD COLUMN released_at TIMESTAMP WITH TIME ZONE;

ALTER TABLE seat_occupancy
    DROP CONSTRAINT uk_seat_occupancy_room_id_seat_id_occupancy_date_schedule_slot_id;

CREATE UNIQUE INDEX uk_seat_occupancy_active
    ON seat_occupancy (
        room_id,
        seat_id,
        occupancy_date,
        schedule_slot_id
    )
    WHERE released_at IS NULL;