CREATE DATABASE IF NOT EXISTS colourhunt;
USE colourhunt;

-- PLAYER table: stores registered users
CREATE TABLE IF NOT EXISTS PLAYER (
    player_id INT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(50)  NOT NULL,
    email     VARCHAR(100) NOT NULL UNIQUE,   -- UNIQUE stops duplicate emails
    password  VARCHAR(100) NOT NULL
);

-- SCORE table: stores every finished game
CREATE TABLE IF NOT EXISTS SCORE (
    score_id    INT PRIMARY KEY AUTO_INCREMENT,
    player_id   INT NOT NULL,
    score       INT NOT NULL,
    level       INT NOT NULL,
    time_taken  INT NOT NULL,
    played_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES PLAYER(player_id)
);