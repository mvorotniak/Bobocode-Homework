CREATE TABLE IF NOT EXISTS cameras
(
    id         BIGSERIAL CONSTRAINT cameras_pk PRIMARY KEY,
    nasa_id    INT UNIQUE               NOT NULL,
    "name"     VARCHAR(255)             NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version    int8                     NULL
);