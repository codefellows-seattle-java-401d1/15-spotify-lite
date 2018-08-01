psql

-- # \c -- see connection
-- # \c tablename -- connect to table
-- # \l -- list all databases

-- # this create a database in all lowercase "spotifyjavaauth"
CREATE DATABASE spotifyjavaauth;
\connect spotifyjavaauth

DROP TABLE IF EXISTS users;

-- # BCrypt returns password hashes of length 60, so I'm making a bit bigger.
CREATE TABLE users (
  userid serial,
  username char(20),
  passhash char(80)
);

-- # add a few default users with pre-computed hashed passwords
-- # ugh, Postgres wants single quotes, not double quotes
INSERT INTO users(username, passhash, artist, song, uploadlocation)
VALUES
('gooseberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se'),

('huckleberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se');

SELECT * FROM users;