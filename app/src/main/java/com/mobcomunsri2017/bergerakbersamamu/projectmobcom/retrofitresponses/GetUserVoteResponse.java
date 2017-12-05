package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Azhary Arliansyah on 29/11/2017.
 */

public class GetUserVoteResponse extends GetResponseTemplate {

    private static String LOG_TAG = "TG.GetUserVoteResp";

    public GetUserVoteResponse(@JsonProperty("error") boolean error,
                               @JsonProperty("error_message") String error_message,
                               @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.errorMessage = error_message;
        this.data = data;
    }

    public int checkVotes() {
        return data.has("type") ? data.get("type").asInt() : -1;
    }
}
