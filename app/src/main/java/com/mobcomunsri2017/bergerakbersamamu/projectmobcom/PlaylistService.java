package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import com.fasterxml.jackson.databind.JsonNode;

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

    @GET("musics/get_musics")
    Call<GetMusicsRequestResponse> getMusics();

    @GET("request/get_request?played=0")
    Call<GetPlaylistResponse> getUnplayedRequest();

    @GET("player/get_currently_playing")
    Call<JsonNode> getCurrentlyPlaying();
}
