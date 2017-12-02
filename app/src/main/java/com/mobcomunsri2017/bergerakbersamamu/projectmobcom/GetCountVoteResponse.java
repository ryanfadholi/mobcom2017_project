package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Azhary Arliansyah on 29/11/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCountVoteResponse {
    private boolean error;
    private String error_message;
    private JsonNode data;

    @JsonCreator
    public GetCountVoteResponse(@JsonProperty("error") boolean error,
                                @JsonProperty("error_message") String error_message,
                                @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.error_message = error_message;
        this.data = data;
    }

    public boolean getError() {
        return error;
    }

    public String getErrorMessage () {
        return error_message;
    }

    public int getVotes() {
        int upvotes = data.get("upvote").asInt();
        int downvotes = data.get("downvote").asInt();
        return upvotes - downvotes;
    }

    public int getDownvotes(){
        return data.get("downvote").asInt();
    }

    public int getUpvotes(){
        return data.get("upvote").asInt();
    }
}
