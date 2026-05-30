CREATE TABLE IF NOT EXISTS adventure_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS adventure (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    difficulty VARCHAR(20) NOT NULL,
    base_price NUMERIC(10, 2) NOT NULL,
    category_id BIGINT REFERENCES adventure_category(id)
);

CREATE TABLE IF NOT EXISTS time_slot (
    id BIGSERIAL PRIMARY KEY,
    adventure_id BIGINT NOT NULL REFERENCES adventure(id) ON DELETE CASCADE,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    max_participants INT NOT NULL,
    current_participants INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS gear_requirement (
    id BIGSERIAL PRIMARY KEY,
    adventure_id BIGINT NOT NULL REFERENCES adventure(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    is_mandatory BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS adventure_review (
    id BIGSERIAL PRIMARY KEY,
    adventure_id BIGINT NOT NULL REFERENCES adventure(id) ON DELETE CASCADE,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE,
    reviewer_name VARCHAR(100)
);
