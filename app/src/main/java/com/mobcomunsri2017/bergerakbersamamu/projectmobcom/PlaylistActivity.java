package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PlaylistActivity extends AppCompatActivity {

    public static final String BASE_WEB_SERVICE_URL = "http://localhost/";
    private PlaylistService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.playlist_toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.playlist_collapsing_toolbar);
        collapsingToolbar.setTitle("Music Player");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.playlist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if(this.service == null){
            this.initPlaylist();
        }

        super.onResume();
    }

    private PlaylistService initPlaylist(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_WEB_SERVICE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        this.service = retrofit.create(PlaylistService.class);
    }

}
