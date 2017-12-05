package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by rynfd on 11/29/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCurrentlyPlayingResponse extends GetResponseTemplate {

    private String LOG_TAG = "TG.GetCurrentlyPlayingResponse";
    private boolean error;
    private String errorMessage;
    private JsonNode data;

    @JsonCreator
    public GetCurrentlyPlayingResponse (@JsonProperty("error") boolean error,
                                     @JsonProperty("error_message") String error_message,
                                     @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.errorMessage = error_message;
        this.data = data;

    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public void getCurrentSong(){

            Log.e(LOG_TAG, "error: " + getErrorMessage());

//            String currentMusicID = data.get("musics_id").asText();
//            String currentAlbum = data.get("album").asText();
//            String currentArtist = data.get("artist").asText();
//            String currentTitle = data.get("title").asText();
//            String currentGenre = data.get("genre").asText();
//            String currentImg = data.get("base64img").asText();

//            Song currentSong = new Song(currentMusicID, currentTitle,
//                    currentArtist, currentAlbum, currentGenre, currentImg);

//            return currentSong;
        }

}
