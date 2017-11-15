package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    public static final String BASE_WEB_SERVICE_URL = "http://192.168.0.169/mpcafe/";
//    public static final String BASE_WEB_SERVICE_URL = "http://192.168.0.123/mpcafe/";

    private static String LOG_TAG = "PV.MainActivity";
    private PlaylistService service;

    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private ArrayList<Song> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.playlist_toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.playlist_collapsing_toolbar);
        collapsingToolbar.setTitle("Music Player");

        this.initPlaylist();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.playlist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               attemptSongRequest(view);
            }
        });


        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));
        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));
        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));
        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));
        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));
        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));
        songs.add(new Song("Partai Perindo", "Hary Tanoesoedibjo", "Mars Perindo", 20));
        songs.add(new Song("Unknown Album", "Unknown Artist", "Kapten Tsubasa", 20));



        playlistRecyclerView = (RecyclerView)findViewById(R.id.playlist);
        playlistRecyclerView.setHasFixedSize(true);
        playlistAdapter = new PlaylistAdapter(songs);
        playlistRecyclerView.setAdapter(playlistAdapter);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onResume() {
        if(this.service == null){
            this.initPlaylist();
        }

        super.onResume();
    }

    private void initPlaylist(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_WEB_SERVICE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        this.service = retrofit.create(PlaylistService.class);
    }

    private void attemptSongRequest(final View view){
        Call<InsertRequestResponse> call = service.sendRequest("1",null,null);

        call.enqueue(new Callback<InsertRequestResponse>() {

            @Override
            public void onResponse(Call<InsertRequestResponse> call, Response<InsertRequestResponse> response) {
                Log.e(LOG_TAG,"JSON error? " + String.valueOf(response.body().getError()));

                String snackbarContent;
                if(!response.body().getError()){
                    snackbarContent = "Song requested successfully";
                } else {
                    snackbarContent = "Failed to request song";
                    Log.e(LOG_TAG,response.body().getError_message());
                }

                Snackbar.make(view, snackbarContent, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }

            @Override
            public void onFailure(Call<InsertRequestResponse> call, Throwable t) {
                Log.e(LOG_TAG,"Error! " + t.toString());
            }
        });
    }

}
