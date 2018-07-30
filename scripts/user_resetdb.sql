-- run this script with
-- psql -f user_resetdb.sql spotifyjavaauth

-- useful psql commands:
-- \c see connection
-- \l list all tables
-- \connect tablename
-- Postgres converts all tablenames to lowercase.
-- Postgres seems to want singlequoted strings, never doublequoted.

\x auto
\pset format wrapped

DROP DATABASE IF EXISTS  spotifyjavaauth;
CREATE DATABASE spotifyjavaauth;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  userid serial,
  username text,
  passhash text,
  artist text,
  song text,
  uploadlocation text
);

INSERT INTO users(username, passhash, artist, song, uploadlocation)
VALUES('gooseberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se', 'unknown', 'china doll', '/uploads/China Doll.wav'),
      ('huckleberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se', 'unknown', 'fallin', '/uploads/Fallin-extended-mix.mp3');

SELECT * FROM users;