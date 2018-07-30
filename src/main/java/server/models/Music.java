package server.models;

import org.mindrot.jbcrypt.BCrypt;
import server.pojo.MusicPojo;
import server.pojo.UserPojo;

public class Music {
//    private static final String DEFAULT_USER = "No Name";
//    private static final String DEFAULT_PASSHASH = "password";
    private static final String DEFAULT_ARTIST = "unknown";
    private static final String DEFAULT_SONG = "unknown";
    private static final String DEFAULT_UPLOADLOCATION = "/uploads";


    public String username;
    public String artist;
    public String song;
    public String uploadlocation;

    public Music() {
        this(DEFAULT_ARTIST, DEFAULT_SONG, DEFAULT_UPLOADLOCATION);
    }


    public Music(MusicPojo music) {
        this(music.artist, music.song, music.uploadlocation);
    }

    public Music(String username, String artist, String song, String uploadlocation) {
        this(artist, song, uploadlocation);
    }

    public Music(String artist, String song, String uploadlocation) {
//        this.username = username;
        this.artist = artist;
        this.song = song;
        this.uploadlocation = uploadlocation;
    }

}