package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Azhary Arliansyah on 29/11/2017.
 */

public class GetUserVoteResponse {
    private boolean error;
    private String error_message;
    private JsonNode data;

    public GetUserVoteResponse(@JsonProperty("error") boolean error,
                               @JsonProperty("error_message") String error_message,
                               @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.error_message = error_message;
        this.data = data;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public int checkVotes() {
        return data.has("type") ? data.get("type").asInt() : -1;
    }
}
