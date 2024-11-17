CREATE TABLE recipe (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL,
    ingredients TEXT,
    preparation TEXT,
    img_url VARCHAR(255),
    prep_time INT,
    cook_time INT,
    category VARCHAR(255),
    difficulty VARCHAR(255)
);