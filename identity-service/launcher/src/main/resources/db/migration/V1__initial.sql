CREATE TABLE cozyr_user (
    id                  UUID DEFAULT gen_random_uuid() NOT NULL,
    display_name        VARCHAR(50) NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,
    version             BIGINT NOT NULL,

    CONSTRAINT pk_cozyr_user PRIMARY KEY (id)
);

CREATE TABLE credential (
    id                  UUID DEFAULT gen_random_uuid() NOT NULL,
    user_id             UUID NOT NULL,
    email               VARCHAR(100) NOT NULL,
    password_hash       VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_credential PRIMARY KEY (id),
    CONSTRAINT uk_credential_user_id_email UNIQUE (user_id, email)
);

CREATE TABLE user_scope (
    id                  UUID DEFAULT gen_random_uuid() NOT NULL,
    user_id             UUID NOT NULL,
    service             VARCHAR(50) NOT NULL,
    role                VARCHAR(20) NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_user_scope PRIMARY KEY (id),
    CONSTRAINT uk_user_scope_user_id_service_role UNIQUE (user_id, service, role)
);