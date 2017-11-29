package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Request;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlaylistActivity extends AppCompatActivity {

//    public static final String BASE_WEB_SERVICE_URL = "http://192.168.43.144/mpcafe/"; // azhary
//    public static final String BASE_WEB_SERVICE_URL = "http://192.168.0.123/mpcafe/"; //ryan ganteng
    public static final String BASE_WEB_SERVICE_URL = "http://192.168.0.145/mpcafe/"; //ryan - VAIO
//    public static final String BASE_WEB_SERVICE_URL = "http://10.102.227.131/mpcafe/";

    private static final String LOG_TAG = "TG.PlaylistActivity";
    public static final int REQUEST_CODE_ADD_SONG = 1;
    public static PlaylistService service;

    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Request> requests = new ArrayList<>();

    private TextView nowPlayingTitle;
    private TextView nowPlayingDetails;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.playlist_toolbar);
        setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.playlist_collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.playlist_appbar);
        appBarLayout.setExpanded(true);
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
        mContext = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.playlist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PlaylistActivity.this, AddSongActivity.class), REQUEST_CODE_ADD_SONG);
            }
        });

        //Initialize views
        nowPlayingTitle = findViewById(R.id.nowplaying_title);
        nowPlayingTitle = findViewById(R.id.nowplaying_detail);

        //Retrieve Data
        this.initPlaylist();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //fetchCurrentlyPlaying();
                fetchRequests();
            }
        }, 1000, 5000);
    }

    public void fetchRequests() {
        PlaylistActivity.checkService();
        Call<GetPlaylistResponse> call = service.getUnplayedRequest();

        call.enqueue(new Callback<GetPlaylistResponse>() {
            @Override
            public void onResponse(Call<GetPlaylistResponse> call, Response<GetPlaylistResponse>
                    response) {
                if (response.body().isError()) {
                    Snackbar.make(findViewById(android.R.id.content),
                            "We can't check available requests right now. " +
                                    "Please try again in a moment.",
                            Snackbar.LENGTH_LONG);
                    requests.clear();
                    return;
                }

                requests.clear();
                requests.addAll(response.body().getRequests());
                playlistRecyclerView = (RecyclerView)findViewById(R.id.playlist);
                playlistRecyclerView.setHasFixedSize(true);
                playlistAdapter = new PlaylistAdapter(mContext, requests);
                playlistRecyclerView.setAdapter(playlistAdapter);
                playlistRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            }

            @Override
            public void onFailure(Call<GetPlaylistResponse> call, Throwable t) {
                Log.e(LOG_TAG,"Error! " + t.toString());
            }
        });
    }

    private void fetchCurrentlyPlaying(){
        PlaylistActivity.checkService();
        Call<JsonNode> call = service.getCurrentlyPlaying();

        call.enqueue(new Callback<JsonNode>() {
            @Override
            public void onResponse(Call<JsonNode> call, Response<JsonNode> response) {
                Log.e(LOG_TAG, "welp, this is actually called!");

                Log.e(LOG_TAG, "json: " + response.body().toString());
//                if (response.body().isError()) {
//                    nowPlayingTitle.setText("Uh-Oh!");
//                    nowPlayingDetails.setText("We experienced some problems. Please try again.");
//
//                    Log.e(LOG_TAG, response.body().getErrorMessage());
//                    return;
//                }
//
//                Log.e(LOG_TAG, "Wadooooo");
//
//                Log.e(LOG_TAG, response.body().toString());
//
//                response.body().getCurrentSong();

//                nowPlayingDetails.setText(currentSong.getTitle() + " | " + currentSong.getArtist());
            }

            @Override
            public void onFailure(Call<JsonNode> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        checkService();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(LOG_TAG,"OnActivityResult called");

        if (requestCode == REQUEST_CODE_ADD_SONG) {
            if (resultCode == RESULT_OK) {
                String musicID = data.getStringExtra(AddSongActivity.EXTRA_MUSIC_ID);

                if(musicID == null) return;
                attemptSongRequest(findViewById(android.R.id.content), musicID);
            }
        }
    }

    public static void checkService(){
        if(service == null){
            initPlaylist();
        }
    }

    public static void initPlaylist(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_WEB_SERVICE_URL)
                .addConverterFactory(JacksonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        service = retrofit.create(PlaylistService.class);
    }

    private void attemptSongRequest(final View view, String musicID){
        Call<InsertRequestResponse> call = service.sendRequest(musicID,null,null);

        call.enqueue(new Callback<InsertRequestResponse>() {

            @Override
            public void onResponse(Call<InsertRequestResponse> call, Response<InsertRequestResponse> response) {
                Log.e(LOG_TAG,"Insert Request JSON error? " + String.valueOf(response.body().getError()));

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
