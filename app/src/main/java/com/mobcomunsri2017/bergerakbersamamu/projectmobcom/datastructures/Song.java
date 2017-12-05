package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures;

import android.util.Log;

/**
 * Created by rynfd on 10/30/2017.
 */

public class Song {

    public static String LOG_TAG = "TG.Song";

    protected String musicID;
    protected String album;
    protected String artist;
    protected String title;
    protected String genre;
    protected String base64Img;

    public Song(String musicID, String title,  String artist, String album) {
        this.musicID = musicID;
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.genre = null;
        this.fixEmptyFields();
    }

    public Song(String musicID, String title,  String artist, String album, String genre) {
        this.musicID = musicID;
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        this.fixEmptyFields();
    }

    public Song(String musicID, String title,  String artist, String album, String genre, String img) {
        this.musicID = musicID;
        this.album = album;
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        this.base64Img = img;
        this.fixEmptyFields();
    }

    public String getMusicID() {
        return musicID;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    public String getBase64Img() {
        return base64Img;
    }

    protected void fixEmptyFields(){

        boolean adjusted = false;

        if(this.title.equals("") || this.title.toLowerCase().equals("unknown") || this.title == null){
            adjusted = true;
            this.title = "Unknown";
        }

        if(this.artist.equals("") || this.artist.toLowerCase().equals("unknown") || this.artist == null){
            adjusted = true;
            this.artist = "Unknown Artist";
        }

        if(this.album.equals("") || this.album.toLowerCase().equals("unknown") || this.album == null){
            adjusted = true;
            this.album = "Unknown Album";
        }

        if(this.genre.equals("") || this.genre.toLowerCase().equals("unknown") || this.genre == null){
            adjusted = true;
            this.genre = "Unknown";
        }

        if (this.base64Img != null && (this.base64Img == "" || this.base64Img.toLowerCase().equals("null"))) {
            adjusted = true;
            this.base64Img = null;
        }

        Log.e(LOG_TAG, "(" + musicID + "|" + title + ") fixEmptyFields() is called.");
    }
}
