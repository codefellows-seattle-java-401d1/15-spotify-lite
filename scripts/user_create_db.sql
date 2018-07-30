psql

# \c -- see connection
# \c tablename -- connect to table
# \l -- list all databases

# this create a database in all lowercase "spotifyjavaauth"
CREATE DATABASE spotifyjavaauth;
\connect spotifyjavaauth

DROP TABLE IF EXISTS users;

# BCrypt returns password hashes of length 60, so I'm making a bit bigger.
CREATE TABLE users (
  userid serial,
  username char(20),
  passhash char(80),
  artist char(80),
  song char(80),
  uploadlocation char(250)
);

# add a few default users with pre-computed hashed passwords
# ugh, Postgres wants single quotes, not double quotes
INSERT INTO users(username, passhash)
VALUES('gooseberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se', 'unknown', 'china doll', '/uploads/China Doll.wav'),
      ('huckleberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se', 'unknown', 'fallin', '/uploads/Fallin-extended-mix.mp3');

SELECT * FROM users;