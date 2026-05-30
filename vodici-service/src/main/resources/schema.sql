CREATE TABLE IF NOT EXISTS language (
    id BIGSERIAL PRIMARY KEY,
    language_name VARCHAR(100) NOT NULL UNIQUE,
    language_code VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS guide (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    bio TEXT,
    rating DOUBLE PRECISION
);

CREATE TABLE IF NOT EXISTS guide_language (
    guide_id BIGINT NOT NULL REFERENCES guide(id) ON DELETE CASCADE,
    language_id BIGINT NOT NULL REFERENCES language(id) ON DELETE CASCADE,
    PRIMARY KEY (guide_id, language_id)
);

CREATE TABLE IF NOT EXISTS certification (
    id BIGSERIAL PRIMARY KEY,
    guide_id BIGINT NOT NULL REFERENCES guide(id) ON DELETE CASCADE,
    name VARCHAR(200) NOT NULL,
    issuing_body VARCHAR(200),
    expiry_date DATE
);
