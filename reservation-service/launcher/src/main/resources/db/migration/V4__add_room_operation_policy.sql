CREATE TABLE room_op_policy (
    id                                  UUID DEFAULT uuidv7() NOT NULL,
    room_id                             BIGINT NOT NULL,
    max_reservation_per_user_per_day    INT NOT NULL,
    max_schedule_per_reservation        INT NOT NULL,
    updated_at                          TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_room_op_policy PRIMARY KEY (id),
    CONSTRAINT uk_room_op_policy_room_id
        UNIQUE (room_id)
);

CREATE TABLE daily_reservation_usage (
    id                  UUID DEFAULT uuidv7() NOT NULL,
    user_id UUID        NOT NULL,
    room_id BIGINT      NOT NULL,
    reservation_date    DATE NOT NULL,
    reservation_count   INT NOT NULL,

    CONSTRAINT pk_daily_reservation_usage PRIMARY KEY (id),
    CONSTRAINT uk_daily_reservation_usage
        UNIQUE (user_id, room_id, reservation_date)
);