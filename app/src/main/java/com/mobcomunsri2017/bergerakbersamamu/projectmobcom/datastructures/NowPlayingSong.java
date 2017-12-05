package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures;

/**
 * Created by rynfd on 12/3/2017.
 */

public class NowPlayingSong extends Song {

    protected String requestID;

    public NowPlayingSong(String requestID, String musicID, String title,  String artist,
                          String album, String genre, String img) {
        super(musicID, title, artist, album, genre, img);
        this.requestID = requestID;
    }

    public NowPlayingSong(NowPlayingSong source){
        super(source.getMusicID(), source.getTitle(), source.getArtist(), source.getAlbum(),
                source.getGenre(), source.getBase64Img());
        this.requestID = source.getRequestID();
    }


    public String getRequestID() {
        return requestID;
    }
}
