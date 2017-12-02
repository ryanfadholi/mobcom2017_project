package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import java.util.ArrayList;

/**
 * Created by rynfd on 11/27/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMusicsRequestResponse extends GetResponseTemplate {

    private static final String LOG_TAG = "TG.GetMusicsReqResp";

    @JsonCreator
    public GetMusicsRequestResponse (@JsonProperty("error") boolean error,
                  @JsonProperty("error_message") String error_message,
                  @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.errorMessage = error_message;
        this.data = data;
    }

    public ArrayList<Song> getMusics(){
        ArrayList<Song> result = new ArrayList<>();

        for (JsonNode music : data) {

            String currentMusicID = music.get("musics_id").asText();
            String currentAlbum = music.get("album").asText();
            String currentArtist = music.get("artist").asText();
            String currentTitle = music.get("title").asText();
            String currentGenre = music.get("genre").asText();
            String currentImg = music.get("base64img").asText();

            Song currentSong = new Song(currentMusicID, currentTitle,
                    currentArtist, currentAlbum, currentGenre, currentImg);

            result.add(currentSong);
        }

        Log.e(LOG_TAG,"Songs: " + String.valueOf(result.size()));
        return result;
    }
}
