CREATE TABLE daily_new_post (
    board_id            UUID NOT NULL,
    created_date        DATE NOT NULL,
    created_count       BIGINT NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_daily_new_post PRIMARY KEY (board_id, created_date)
);

CREATE TABLE daily_new_comment (
    post_id             BIGINT NOT NULL,
    created_date        DATE NOT NULL,
    created_count       BIGINT NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_daily_new_comment PRIMARY KEY (post_id, created_date)
);
