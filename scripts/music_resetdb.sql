-- run this script with
-- psql -f music_resetdb.sql spotifylite

-- useful psql commands:
-- \c see connection
-- \l list all tables
-- \connect tablename
-- Postgres converts all tablenames to lowercase.
-- Postgres seems to want singlequoted strings, never doublequoted.

\x auto
\pset format wrapped

DROP DATABASE IF EXISTS  spotifylite;
CREATE DATABASE spotifylite;

DROP TABLE IF EXISTS music;

CREATE TABLE music (
  musicid serial,
  username text,
  artist text,
  song text,
  uploadlocation text
);

INSERT INTO music(username, artist, song, uploadlocation)
VALUES('gooseberries', 'unknown', 'china doll', '/uploads/China Doll.wav'),
      ('huckleberries', 'unknown', 'fallin', '/uploads/Fallin-extended-mix.mp3');

SELECT * FROM music;