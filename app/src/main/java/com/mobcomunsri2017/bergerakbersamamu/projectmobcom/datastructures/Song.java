package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures;

/**
 * Created by rynfd on 10/30/2017.
 */

public class Song {
    private String album;
    private String artist;
    private String title;
    private int playtimeInMs;

    public Song(String album, String artist, String title, int playtimeInMs) {
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.playtimeInMs = playtimeInMs;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getPlaytimeInMs() {
        return playtimeInMs;
    }
}
