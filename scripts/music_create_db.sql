psql

# \c -- see connection
# \c tablename -- connect to table
# \l -- list all databases

# this create a database in all lowercase "spotifylite"
CREATE DATABASE spotifylite;
\connect spotifylite

DROP TABLE IF EXISTS users;

# BCrypt returns password hashes of length 60, so I'm making a bit bigger.
CREATE TABLE users (
  musicid serial,
  username char(20),
  artist char(80),
  song char(80)
);

# add a few default users with pre-computed hashed passwords
# ugh, Postgres wants single quotes, not double quotes
INSERT INTO users(username, passhash)
VALUES ('gooseberries', 'unknown', 'china doll', '/uploads/China Doll.wav'),
      ('huckleberries', 'unknown', 'fallin', '/uploads/Fallin-extended-mix.mp3');

SELECT * FROM users;