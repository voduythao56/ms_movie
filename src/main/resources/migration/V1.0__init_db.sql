
CREATE TABLE movie (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    category VARCHAR(200) NOT NULL,
    star_rating DECIMAL(2,1) NOT NULL,
    created_date DATETIME default CURRENT_TIMESTAMP,
    updated_date DATETIME default CURRENT_TIMESTAMP,
    version INT NOT NULL
);