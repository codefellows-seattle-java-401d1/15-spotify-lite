package server.db;

import server.models.Song;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDB {
    public static final List<Song> songs = new ArrayList<>();

    private static Connection mConn;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/spotifylite";

            try {
                mConn = DriverManager.getConnection(url);
                ResultSet results = mConn.createStatement().executeQuery("SELECT * FROM songs");
                while (results.next()) {
                    int id = results.getInt("id");
                    String title = results.getString("title");
                    String artist = results.getString("artist");
                    String path = results.getString("path");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("DB error.");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Postgres driver not configured correctly.");
        }
    }

    public static void reset() {
        String sql = "DROP DATABASE IF EXISTS spotifylite; " +
                "CREATE DATABASE spotifylite; " +
                "DROP TABLE IF EXISTS songs; " +
                "CREATE TABLE songs ( " +
                "        id serial, " +
                "        title text, " +
                "        artist text, " +
                "        file text " +
                "); " +
                "INSERT INTO songs(title, artist, file) " +
                "VALUES('Bourree 4th Lute Suite', 'Bach Performed by Jon Sayles', '/uploads/Bach - Bourree from the 4th Lute Suite (1).mp3'), " +
                "('Etude Opus 8 No. 12 ', 'Skrjabin Performed by Nico De Napoli', '/uploads/Skrjabin Etude_Op8_No12" +
                ".mp3')," + "('Menuettos 1 and 2 from 41st Symphony', 'Mozart Performed by Jon Sales', 'uploads/Mozart - Menuettos 1 and 2 from 41st Symphony.mp3'); ";

        try {
            mConn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Song createSong(String title, String artist, String path) {
        String sql = "INSERT INTO songs(title, artist, path) VALUES('%s', '%s', '%s') RETURNING (id);";
        sql = String.format(sql, title, artist, path);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int id = results.getInt("id");
            Song song = new Song(id, title, artist, path);
            return song;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<Song> getAllSongs() {
        List<Song> users = new ArrayList<>();

        String sql = "SELECT * FROM songs";

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            while (results.next()) {
                int id = results.getInt("id");
                String title = results.getString("title");
                String artist = results.getString("artist");
                String path = results.getString("path");

                Song song = new Song(id, title, artist, path);
                songs.add(song);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    public static Song getSongById(int searchId) {
        String sql = "SELECT * FROM songs WHERE id=%d";
        sql = String.format(sql, searchId);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int id = results.getInt("id");
            String title = results.getString("title");
            String artist = results.getString("artist");
            String path = results.getString("path");

            Song song = new Song(id, title, artist, path);
            return song;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Song getSongByName (String searchTitle) {
        String sql = "SELECT * FROM songs WHERE title='%s'";
        sql = String.format(sql, searchTitle);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            if (!results.next()) {
                // no matching song
                return null;
            }

            int id = results.getInt("id");
            String title = results.getString("title");
            String artist = results.getString("artist");
            String path = results.getString("path");

            Song song = new Song(id, title, artist, path);
            return song;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Song updateSongArtist (int searchId, String newArtist) {
        String sql = "UPDATE songs SET artist='%s' WHERE id=%d RETURNING *;";
        sql = String.format(sql, newArtist, searchId);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();

            int id = results.getInt("id");
            String title = results.getString("title");
            String artist = results.getString("artist");
            String path = results.getString("path");

            Song song = new Song(id, title, artist, path);
            return song;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Song updateSongTitle (int searchId, String newTitle) {
        String sql = "UPDATE songs SET title='%s' WHERE id=%d RETURNING *;";
        sql = String.format(sql, newTitle, searchId);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();

            int id = results.getInt("id");
            String title = results.getString("title");
            String artist = results.getString("artist");
            String path = results.getString("path");

            Song song = new Song(id, title, artist, path);
            return song;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean deleteSong (int searchId) {
        String sql = "DELETE FROM songs WHERE id=%d;";
        sql = String.format(sql, searchId);

        try {
            mConn.createStatement().execute(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
