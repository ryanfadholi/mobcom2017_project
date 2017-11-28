package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Request;

import java.util.ArrayList;

/**
 * Created by Azhary Arliansyah on 28/11/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPlaylistResponse {

    private String LOG_TAG = "TG.GetPlaylistResponse";
    private boolean error;
    private String errorMessage;
    private JsonNode data;

    @JsonCreator
    public GetPlaylistResponse(@JsonProperty("error") boolean error,
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

    public ArrayList<Request> getRequests() {
        ArrayList<Request> result = new ArrayList<>();
        for (JsonNode request : data) {
            String requestID    = request.get("request_id").asText();
            String musicID      = request.get("musics_id").asText();
            String played       = request.get("played").asText();
            String priority     = request.get("priority").asText();
            String schedule     = request.get("schedule").asText();
            String createdAt    = request.get("created_at").asText();
            String updatedAt    = request.get("updated_at").asText();
            String title        = request.get("title").asText();
            String artist       = request.get("artist").asText();
            String album        = request.get("album").asText();
            String genre        = request.get("genre").asText();
            String base64Img    = request.get("base64img").asText();

            result.add(new Request(requestID, musicID, Integer.parseInt(played), Integer.parseInt(priority),
                    schedule, createdAt, updatedAt, title, artist, album, genre, base64Img));
        }

        Log.e(LOG_TAG,"Requests: " + String.valueOf(result.size()));
        return result;
    }
}
