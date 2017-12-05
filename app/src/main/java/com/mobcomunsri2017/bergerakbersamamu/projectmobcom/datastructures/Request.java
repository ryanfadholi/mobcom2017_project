package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures;

/**
 * Created by Azhary Arliansyah on 28/11/2017.
 */

public class Request extends Song {

    private String requestID;
    private int played;
    private int priority;
    private String schedule;
    private String createdAt;
    private String updatedAt;

    public Request(String requestID, String musicID, int played, int priority,
                   String schedule, String createdAt, String updatedAt,
                   String title, String artist, String album, String genre) {
        super(musicID, title, artist, album, genre);

        this.requestID = requestID;
        this.played = played;
        this.priority = priority;
        this.schedule = schedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Request(String requestID, String musicID, int played, int priority,
                   String schedule, String createdAt, String updatedAt,
                   String title, String artist, String album, String genre, String img) {
        super(musicID, title, artist, album, genre, img);

        this.requestID = requestID;
        this.played = played;
        this.priority = priority;
        this.schedule = schedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getRequestID() {return this.requestID;}
    public int getPlayed() {return this.played;}
    public int getPriority() {return this.priority;}
    public String getSchedule() {return this.schedule;}
    public String getCreatedAt() {return this.createdAt;}
    public String getUpdatedAt() {return this.updatedAt;}
}
