package server.models;

import server.pojos.SongPojo;

public class Song {

    //set default values
    private static final String DEFAULT_TITLE = "No Title";
    private static final String DEFAULT_ARTIST = "unknown";
    private static final String DEFAULT_PATH = "/uploads";

    //create properties of the class, aka "constructor"
    public String title;
    public String artist;
    public String path;
    public int id;

    public Song() {
        this(DEFAULT_TITLE,DEFAULT_ARTIST, DEFAULT_PATH);
    }

    public Song(SongPojo song) {
        this(song.title, song.artist, song.path);
    }

    public Song(String title, String artist, String path) {
        this(-1, title, artist, path);
    }

    public Song (int id, String title, String artist, String path) {
        this.title = title;
        this.artist = artist;
        this.path = path;
    }
}