package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetCountAllVotesResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetMusicsRequestResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetNowPlayingResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetPlaylistResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetTotalVoteResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetUserAllVotesResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetUserVoteResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.InsertRequestResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.InsertVoteResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Ryan Fadholi on 14/11/2017.
 */

public interface PlaylistService {

    @FormUrlEncoded
    @POST("request/set_request")
    Call<InsertRequestResponse> sendRequest(@Field("musics_id") String musics_id,
                                            @Field("priority") String priority,
                                            @Field("schedule") String schedule);

    @FormUrlEncoded
    @POST("votes/set_votes")
    Call<InsertVoteResponse> setVote(@Field("musics_id") String musics_id,
                                     @Field("type") String type,
                                     @Field("device_id") String device_id);

    @GET("votes/count_all_votes")
    Call<GetCountAllVotesResponse> getAllVotes();

    @FormUrlEncoded
    @POST("votes/count_votes")
    Call<GetTotalVoteResponse> getVotes(@Field("musics_id") String musics_id);

    @FormUrlEncoded
    @POST("votes/check_all_votes")
    Call<GetUserAllVotesResponse> chcekAllUserVotes(@Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("votes/check_votes")
    Call<GetUserVoteResponse> checkUserVote(@Field("musics_id") String musics_id,
                                            @Field("device_id") String device_id);

    @GET("musics/get_musics")
    Call<GetMusicsRequestResponse> getMusics();

    @GET("request/get_request?played=0")
    Call<GetPlaylistResponse> getUnplayedRequest();

    @GET("player/get_currently_playing")
    Call<GetNowPlayingResponse> getCurrentlyPlaying();
}
