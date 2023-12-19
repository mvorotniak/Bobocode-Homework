CREATE TABLE IF NOT EXISTS pictures
(
    id         BIGSERIAL CONSTRAINT pictures_pk PRIMARY KEY,
    nasa_id    INT UNIQUE               NOT NULL,
    img_src    VARCHAR(255)             NULL,
    camera_id  BIGINT                   NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version    int8                     NULL,

    CONSTRAINT pictures_cameras_fk FOREIGN KEY (camera_id) REFERENCES cameras (id)
);
