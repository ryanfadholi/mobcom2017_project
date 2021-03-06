package com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;

/**
 * Created by rynfd on 12/2/2017.
 */

public class GetCountAllVotesResponse extends GetResponseTemplate {

    @JsonCreator
    public GetCountAllVotesResponse(@JsonProperty("error") boolean error,
                                @JsonProperty("error_message") String error_message,
                                @JsonProperty("data") JsonNode data) {
        this.error = error;
        this.errorMessage = error_message;
        this.data = data;
    }

    public HashMap<String, Integer> getTotalVotes() {
        HashMap<String, Integer> result = new HashMap<>();


        HashMap<String, Integer> upvotes = this.getUpvotes();
        HashMap<String, Integer> downvotes = this.getDownvotes();


        for(String music_id : upvotes.keySet()){
            result.put(music_id, upvotes.get(music_id) - downvotes.get(music_id));
        }

        return result;
    }

    public HashMap<String, Integer> getDownvotes(){
        HashMap<String, Integer> result = new HashMap<>();

        String currentId;
        int total_vote;

        for (JsonNode voteData : data) {
            currentId = voteData.get("musics_id").asText();
            total_vote = voteData.get("downvote").asInt();

            result.put(currentId, total_vote);
        }

        return result;
    }

    public HashMap<String, Integer> getUpvotes(){
        HashMap<String, Integer> result = new HashMap<>();

        String currentId;
        int total_vote;

        for (JsonNode voteData : data) {
            currentId = voteData.get("musics_id").asText();
            total_vote = voteData.get("upvote").asInt();

            result.put(currentId, total_vote);
        }

        return result;
    }
}
