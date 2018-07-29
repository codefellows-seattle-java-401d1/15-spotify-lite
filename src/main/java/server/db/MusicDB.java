package server.db;

import org.mindrot.jbcrypt.BCrypt;
import server.models.Music;
import server.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MusicDB {

    private static Connection mConn;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/spotifylite";

            try {
                mConn = DriverManager.getConnection(url);
                ResultSet results = mConn.createStatement().executeQuery("SELECT * FROM music");
                while (results.next()) {
                    int musicid = results.getInt("musicid");
                    String artist = results.getString("artist");
                    String song = results.getString("song");
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
        String sql = "DROP DATABASE IF EXISTS  spotifylite; " +
                "CREATE DATABASE spotifylite; " +
                "DROP TABLE IF EXISTS music; " +
                "CREATE TABLE music ( " +
                "        musicid serial, " +
                "        username text, " +
                "        artist text, " +
                "        song text, " +
                "        uploadlocation text " +
                "); " +
                "INSERT INTO users(username, artist, song, uploadlocation) " +
                "VALUES('gooseberries', 'unknown', 'china doll', '/uploads/China Doll.wav'), " +
                "       ('huckleberries', 'unknown', 'fallin', '/uploads/Fallin-extended-mix.mp3'); ";

        try {
            mConn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Music createMusic(String username, String artist, String song, String uploadlocation) {
        String sql = "INSERT INTO music(username, artist, song, uploadlocation) VALUES('%s', '%s', '%s', '%s') RETURNING (musicid);";
        sql = String.format(sql, username, artist, song, uploadlocation);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int musicid = results.getInt("musicid");
            Music music = new Music(musicid, username, artist, song);
            return music;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<Music> getAllMusic() {
        List<Music> music = new ArrayList<>();

        String sql = "SELECT * FROM music";

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            while (results.next()) {
                int musicid = results.getInt("musicid");
                String username = results.getString("username");
                String artist = results.getString("artist");
                String song = results.getString("song");

                Music allMusic = new Music(musicid, username, artist, song);
                music.add(allMusic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return music;
    }

    public static Music getMusicById(int searchId) {
        String sql = "SELECT * FROM music WHERE musicid=%d";
        sql = String.format(sql, searchId);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int musicid = results.getInt("musicid");
            String username = results.getString("username");
            String artist = results.getString("artist");
            String song = results.getString("song");

            Music music = new Music(musicid, username, artist, song);
            return music;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Music getMusicByArtistName(String searchArtist) {
        String sql = "SELECT * FROM music WHERE artist='%s'";
        sql = String.format(sql, searchArtist);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            if (!results.next()) {
                // no matching user
                return null;
            }

            int musicid = results.getInt("musicid");
            String username = results.getString("username");
            String artist = results.getString("artist");
            String song = results.getString("song");

            Music music = new Music(musicid, username, artist, song);
            return music;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Music getMusicBySongName(String searchSong) {
        String sql = "SELECT * FROM music WHERE song='%s'";
        sql = String.format(sql, searchSong);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            if (!results.next()) {
                // no matching user
                return null;
            }

            int musicid = results.getInt("musicid");
            String username = results.getString("username");
            String artist = results.getString("artist");
            String song = results.getString("song");

            Music music = new Music(musicid, username, artist, song);
            return music;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Music updateMusicInfo (int searchId, String newArtist, String newSong) {
        String sql = "UPDATE music SET artist='%s' WHERE musicid=%d RETURNING *;";
        sql = String.format(sql, newArtist, newSong, searchId);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();

            int musicid = results.getInt("musicid");
            String username = results.getString("username");
            String artist = results.getString("artist");
            String song = results.getString("song");

            Music music = new Music(musicid, username, artist, song);
            return music;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
//
//    public static boolean deleteUser (int searchId) {
//        String sql = "DELETE FROM users WHERE musicid=%d;";
//        sql = String.format(sql, searchId);
//
//        try {
//            mConn.createStatement().execute(sql);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
}