-- Development databases only: existing IDs cannot be converted without migrating references.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM room) OR EXISTS (SELECT 1 FROM seat) THEN
        RAISE EXCEPTION
            'Cannot change room and seat IDs to UUID: room and seat must be empty';
    END IF;
END $$;

ALTER TABLE room
    ALTER COLUMN id DROP IDENTITY;

ALTER TABLE room
    ALTER COLUMN id TYPE UUID USING uuidv7(),
    ALTER COLUMN id SET DEFAULT uuidv7();

ALTER TABLE seat
    ADD COLUMN id UUID DEFAULT uuidv7() NOT NULL,
    ADD COLUMN code VARCHAR(20) NOT NULL;

ALTER TABLE seat
    DROP CONSTRAINT pk_seat,
    ADD CONSTRAINT pk_seat PRIMARY KEY (id),
    ADD CONSTRAINT uk_seat_room_id_seat_id UNIQUE (room_id, seat_id);

ALTER TABLE room_schedule_slot
    ALTER COLUMN room_id TYPE UUID USING NULL::uuid;

ALTER TABLE room_op_policy
    ALTER COLUMN room_id TYPE UUID USING NULL::uuid;

ALTER TABLE daily_reservation_usage
    ALTER COLUMN room_id TYPE UUID USING NULL::uuid;

ALTER TABLE seat_occupancy
    ALTER COLUMN room_id TYPE UUID USING NULL::uuid,
    ALTER COLUMN seat_id TYPE UUID USING NULL::uuid;

ALTER TABLE reservation
    ALTER COLUMN room_id TYPE UUID USING NULL::uuid,
    ALTER COLUMN seat_id TYPE UUID USING NULL::uuid;
