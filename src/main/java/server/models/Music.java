package server.models;

import org.mindrot.jbcrypt.BCrypt;
import server.pojo.MusicPojo;
import server.pojo.UserPojo;

public class Music {
    private static final String DEFAULT_USER = "No Name";
    private static final String DEFAULT_ARTIST = "unknown";
    private static final String DEFAULT_SONG = "unknown";


    public int musicid;
    public String username;
    private String artist;
    private String song;

    public Music() {
        this(DEFAULT_ARTIST, DEFAULT_ARTIST, DEFAULT_SONG);
    }

    public Music(MusicPojo music) {
        this(music.username, music.artist, music.song);
    }

    public Music(String username, String artist, String song) {
        this(-1, username, artist, song);
    }

    public Music(int id, String username, String artist, String song) {
        this.musicid = id;
        this.username = username;
        this.artist = artist;
        this.song = song;
    }
}