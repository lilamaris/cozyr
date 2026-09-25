CREATE TABLE room (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name                VARCHAR(100) NOT NULL,
    description         VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_room PRIMARY KEY (id)
);

CREATE TABLE seat (
    room_id             BIGINT NOT NULL,
    seat_id             VARCHAR(10) NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_seat PRIMARY KEY (room_id, seat_id)
);

CREATE TABLE room_schedule_slot (
    id                  UUID DEFAULT gen_random_uuid() NOT NULL,
    room_id             BIGINT NOT NULL,
    start_at            TIME NOT NULL,
    end_at              TIME NOT NULL,

    CONSTRAINT pk_room_schedule_slot PRIMARY KEY (id),
    CONSTRAINT uk_room_schedule_slot_room_id_start_at_end_at UNIQUE (room_id, start_at, end_at)
);

CREATE TABLE seat_occupancy (
    id                  UUID DEFAULT gen_random_uuid() NOT NULL,
    reservation_id      UUID NOT NULL,
    room_id             BIGINT NOT NULL,
    seat_id             VARCHAR(10) NOT NULL,
    occupancy_date      DATE NOT NULL,
    schedule_slot_id    UUID NOT NULL,

    CONSTRAINT pk_seat_occupancy PRIMARY KEY(id),
    CONSTRAINT uk_seat_occupancy_room_id_seat_id_occupancy_date_schedule_slot_id UNIQUE (room_id, seat_id, occupancy_date, schedule_slot_id)
);

CREATE TABLE reservation (
    id                  UUID DEFAULT gen_random_uuid() NOT NULL,
    reserved_user_id    UUID NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_reservation PRIMARY KEY (id)
);

CREATE TABLE user_snapshot (
    user_id             UUID NOT NULL,
    display_name        VARCHAR(50) NOT NULL,
    last_updated_at     TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_user_snapshot PRIMARY KEY(user_id)
);
