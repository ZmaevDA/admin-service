CREATE TABLE IF NOT EXISTS complaint
(

    id               BIGSERIAL     NOT NULL PRIMARY KEY,
    build_id         BIGINT,
    user_id          VARCHAR(255),
    resolver_user_id VARCHAR(255),
    reason           TEXT,
    description      TEXT,
    status           VARCHAR(255),
    created_at       TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ   DEFAULT CURRENT_TIMESTAMP
);
