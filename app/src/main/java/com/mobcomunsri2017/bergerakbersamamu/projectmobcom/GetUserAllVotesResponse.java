package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * Created by rynfd on 12/2/2017.
 */

public class GetUserAllVotesResponse {
    private boolean error;
    private String error_message;
    private JsonNode data;

    public GetUserAllVotesResponse(@JsonProperty("error") boolean error,
                               @JsonProperty("error_message") String error_message,
                               @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.error_message = error_message;
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public String getErrorMessage() {
        return error_message;
    }

    public HashMap<String, Integer> checkUserVotes() {
        String currentMusicId;
        Integer voteType;

        HashMap<String, Integer> results = new HashMap<>();

        for (JsonNode voteData : data) {
            currentMusicId = voteData.get("musics_id").asText();
            voteType = voteData.get("type").asInt();

            results.put(currentMusicId, voteType);
        }

        return results;
    }

    public int checkVotes() {
        return data.has("type") ? data.get("type").asInt() : -1;
    }
}
