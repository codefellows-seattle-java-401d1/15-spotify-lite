package server.db;

import org.mindrot.jbcrypt.BCrypt;
import server.models.Music;
import server.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UserDB {
    private static Connection mConn;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/spotifyjavaauth";

            try {
                mConn = DriverManager.getConnection(url);
                ResultSet results = mConn.createStatement().executeQuery("SELECT * FROM users");
                while (results.next()) {
                    int userid = results.getInt("userid");
                    String name = results.getString("username");
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
        String sql = "DROP DATABASE IF EXISTS  spotifyjavaauth; " +
                "CREATE DATABASE spotifyjavaauth; " +
                "DROP TABLE IF EXISTS users; " +
                "CREATE TABLE users ( " +
                "        userid serial, " +
                "        username text, " +
                "        passhash text " +
                "); " +
                "INSERT INTO users(username, passhash) " +
                "VALUES('gooseberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se'), " +
                "       ('huckleberries', '$2a$12$u7s.Q60pWu01Yujt6KH4wuX8Dcf9Pm1PlwEoQcGXhHrpYzRH53.Se'); ";

        try {
            mConn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User createUser(String username, String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String sql = "INSERT INTO users(username, passhash) VALUES('%s', '%s') RETURNING (userid);";
        sql = String.format(sql, username, hashed);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int userid = results.getInt("userid");
            User user = new User(userid, username, hashed);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            while (results.next()) {
                int userid = results.getInt("userid");
                String username = results.getString("username");
                String passhash = results.getString("passhash");

                User user = new User(userid, username, passhash);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User getUserById(int searchId) {
        String sql = "SELECT * FROM users WHERE userid=%d";
        sql = String.format(sql, searchId);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int userid = results.getInt("userid");
            String username = results.getString("username");
            String passhash = results.getString("passhash");

            User user = new User(userid, username, passhash);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserByName(String searchUsername) {
        String sql = "SELECT * FROM users WHERE username='%s'";
        sql = String.format(sql, searchUsername);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            if (!results.next()) {
                // no matching user
                return null;
            }

            int userid = results.getInt("userid");
            String username = results.getString("username");
            String passhash = results.getString("passhash");

            User user = new User(userid, username, passhash);
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
                String uploadlocation = results.getString("uploadlocation");

                Music allMusic = new Music(username, artist, song, uploadlocation);
                music.add(allMusic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return music;
    }

    public static Music getMusicByUserName(String searchUserName) {
        String sql = "SELECT * FROM music WHERE username=%d";
        sql = String.format(sql, searchUserName);

        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
            results.next();
            int musicid = results.getInt("musicid");
            String username = results.getString("username");
            String artist = results.getString("artist");
            String song = results.getString("song");
            String uploadlocation = results.getString("uploadlocation");

            Music music = new Music(username, artist, song, uploadlocation);
            return music;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static Music getMusicById(int searchId) {
//        String sql = "SELECT * FROM music WHERE musicid=%d";
//        sql = String.format(sql, searchId);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
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
            String uploadlocation = results.getString("uploadlocation");

            Music music = new Music(username, artist, song, uploadlocation);
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
            String uploadlocation = results.getString("uploadlocation");

            Music music = new Music(username, artist, song, uploadlocation);
            return music;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static Music updateMusicInfo (int searchId, String newArtist, String newSong) {
//        String sql = "UPDATE music SET artist='%s', song='%s', uploadlocation='%s' WHERE musicid=%d RETURNING *;";
//        sql = String.format(sql, newArtist, newSong, searchId);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
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

//    public static User updateUserBio (int searchId, String newBio) {
//        String sql = "UPDATE users SET bio='%s' WHERE userid=%d RETURNING *;";
//        sql = String.format(sql, newBio, searchId);
//
//        try (ResultSet results = mConn.createStatement().executeQuery(sql)) {
//            results.next();
//
//            int userid = results.getInt("userid");
//            String username = results.getString("username");
//            String passhash = results.getString("passhash");
//            String bio = results.getString("bio");
//
//            User user = new User(userid, username, passhash, bio);
//            return user;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    public static boolean deleteUser (int searchId) {
//        String sql = "DELETE FROM users WHERE userid=%d;";
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