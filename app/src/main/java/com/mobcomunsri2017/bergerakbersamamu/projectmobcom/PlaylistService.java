package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ryan Fadholi on 14/11/2017.
 */

public interface PlaylistService {

    @GET("request/set_request")
    Call<String> sendRequest(@Query("musics_id") String musics_id,
                             @Query("priority") String priority,
                             @Query("schedule") String schedule);
}
