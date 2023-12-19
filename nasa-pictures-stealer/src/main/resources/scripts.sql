CREATE DATABASE nasa_pictures;
USE nasa_pictures;

CREATE TABLE cameras (
                         id BIGSERIAL CONSTRAINT cameras_pk PRIMARY KEY,
                         nasa_id INT NOT NULL UNIQUE,
                         name TEXT NOT NULL,
                         created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE pictures (
                          id BIGSERIAL CONSTRAINT pictures_pk PRIMARY KEY,
                          nasa_id INT NOT NULL UNIQUE,
                          img_src TEXT NOT NULL,
                          camera_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT NOW(),
                          CONSTRAINT pictures_cameras_fk FOREIGN KEY (camera_id) REFERENCES cameras(id)
);