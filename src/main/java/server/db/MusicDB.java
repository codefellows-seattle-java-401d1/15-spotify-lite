package server.db;

import org.mindrot.jbcrypt.BCrypt;
import server.models.Music;
import server.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MusicDB {

    public static final List<Music> songs = new ArrayList<>();

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
                    String username = results.getString("username");
                    String artist = results.getString("artist");
                    String song = results.getString("song");
                    String uploadlocation = results.getString("uploadlocation");
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
        String query = "DROP DATABASE IF EXISTS  spotifylite; " +
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
            mConn.createStatement().execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Music createMusic(String username, String artist, String song, String uploadlocation) {
//        String query = "UPDATE music SET username=songs.username, artist=songs.artist, song=songs.song, uploadlocation=songs.uploadlocation;";
        String query = "INSERT INTO music(username, artist, song, uploadlocation) VALUES('%s', '%s', '%s', '%s') RETURNING (musicid);";
        query = String.format(query, username, artist, song, uploadlocation);

        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
            results.next();
            int musicid = results.getInt("musicid");
            Music music = new Music(musicid, username, artist, song, uploadlocation);
            return music;
        } catch (SQLException e) {
            return null;
        }
    }

//
//    public static List<Music> getAllMusic() {
//        List<Music> music = new ArrayList<>();
//
//        String query = "SELECT * FROM music";
//
//        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
//            while (results.next()) {
//                int musicid = results.getInt("musicid");
//                String username = results.getString("username");
//                String artist = results.getString("artist");
//                String song = results.getString("song");
//                String uploadlocation = results.getString("uploadlocation");
//
//                Music allMusic = new Music(musicid, username, artist, song);
//                music.add(allMusic);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return music;
//    }
//
    public static Queue<Music> getMusicByUserName(String searchUserName) {
        String query = "SELECT * FROM music WHERE username='%s'";
        query = String.format(query, searchUserName);

        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
            Queue<Music> musicList = new LinkedList<>();

            while(results.next()) {
//                results.next();
                int musicid = results.getInt("musicid");
                String username = results.getString("username");
                String artist = results.getString("artist");
                String song = results.getString("song");
                String uploadlocation = results.getString("uploadlocation");

                Music music = new Music(musicid, username, artist, song, uploadlocation);
                musicList.add(music);
            }
                return musicList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
//
//
//    public static Music getMusicById(int searchId) {
//        String query = "SELECT * FROM music WHERE musicid=%d";
//        query = String.format(query, searchId);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
//            results.next();
//            int musicid = results.getInt("musicid");
//            String username = results.getString("username");
//            String artist = results.getString("artist");
//            String song = results.getString("song");
//            String uploadlocation = results.getString("uploadlocation");
//
//            Music music = new Music(musicid, username, artist, song);
//            return music;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static Music getMusicByArtistName(String searchArtist) {
//        String query = "SELECT * FROM music WHERE artist='%s'";
//        query = String.format(query, searchArtist);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
//            if (!results.next()) {
//                // no matching user
//                return null;
//            }
//
//            int musicid = results.getInt("musicid");
//            String username = results.getString("username");
//            String artist = results.getString("artist");
//            String song = results.getString("song");
//            String uploadlocation = results.getString("uploadlocation");
//
//            Music music = new Music(musicid, username, artist, song);
//            return music;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    public static Music getMusicBySongName(String searchSong) {
//        String query = "SELECT * FROM music WHERE song='%s'";
//        query = String.format(query, searchSong);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
//            if (!results.next()) {
//                // no matching user
//                return null;
//            }
//
//            int musicid = results.getInt("musicid");
//            String username = results.getString("username");
//            String artist = results.getString("artist");
//            String song = results.getString("song");
//            String uploadlocation = results.getString("uploadlocation");
//
//            Music music = new Music(musicid, username, artist, song);
//            return music;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static Music updateMusicInfo (int searchId, String newArtist, String newSong) {
//        String query = "UPDATE music SET artist='%s', song='%s', uploadlocation='%s' WHERE musicid=%d RETURNING *;";
//        query = String.format(query, newArtist, newSong, searchId);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(query)) {
//            results.next();
//
//            int musicid = results.getInt("musicid");
//            String username = results.getString("username");
//            String artist = results.getString("artist");
//            String song = results.getString("song");
//            String uploadlocation = results.getString("uploadlocation");
//
//            Music music = new Music(musicid, username, artist, song);
//            return music;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static boolean deleteUser (int searchId) {
//        String query = "DELETE FROM users WHERE musicid=%d;";
//        query = String.format(query, searchId);
//
//        try {
//            mConn.createStatement().execute(query);
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
}