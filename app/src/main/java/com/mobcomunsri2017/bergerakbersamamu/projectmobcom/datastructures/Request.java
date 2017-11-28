package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures;

/**
 * Created by Azhary Arliansyah on 28/11/2017.
 */

public class Request {

    private String requestID;
    private String musicID;
    private int played;
    private int priority;
    private String schedule;
    private String createdAt;
    private String updatedAt;

    private String title;
    private String artist;
    private String album;
    private String genre;
    private String base64Img;

    public Request(String requestID, String musicID, int played, int priority,
                   String schedule, String createdAt, String updatedAt,
                   String title, String artist, String album, String genre) {
        this.requestID = requestID;
        this.musicID = musicID;
        this.played = played;
        this.priority = priority;
        this.schedule = schedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;

        this.fixEmptyFields();
    }

    public Request(String requestID, String musicID, int played, int priority,
                   String schedule, String createdAt, String updatedAt,
                   String title, String artist, String album, String genre, String img) {
        this.requestID = requestID;
        this.musicID = musicID;
        this.played = played;
        this.priority = priority;
        this.schedule = schedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.base64Img = img;

        this.fixEmptyFields();
    }

    public String getRequestID() {return this.requestID;}
    public String getMusicID() {return this.musicID;}
    public int getPlayed() {return this.played;}
    public int getPriority() {return this.priority;}
    public String getSchedule() {return this.schedule;}
    public String getCreatedAt() {return this.createdAt;}
    public String getUpdatedAt() {return this.updatedAt;}
    public String getTitle() {return this.title;}
    public String getArtist() {return this.artist;}
    public String getAlbum() {return this.album;}
    public String getGenre() {return this.genre;}
    public String getBase64Img() {return this.base64Img;}

    private void fixEmptyFields() {
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
