package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures;

/**
 * Created by rynfd on 10/30/2017.
 */

public class Song {
    private String musicID;
    private String album;
    private String artist;
    private String title;
    private String genre;
    private String base64Img;

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

    private void fixEmptyFields(){

        if(this.title == "" || this.title == null){
            this.title = "Unknown";
        }

        if(this.artist == "" || this.artist == null){
            this.artist = "Unknown Artist";
        }

        if(this.album == "" || this.album == null){
            this.album = "Unknown Album";
        }

        if(this.genre == "" || this.genre == null){
            this.genre = "Unknown";
        }

        if (this.base64Img == "") {
            this.base64Img = null;
        }
    }
}
