CREATE TABLE leaves (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    duration DOUBLE PRECISION NOT NULL,
    description VARCHAR(255),
    half_day VARCHAR(255),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);