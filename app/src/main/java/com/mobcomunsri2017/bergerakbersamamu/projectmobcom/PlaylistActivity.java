package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentQueryMap;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.JsonNode;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.NowPlayingSong;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Request;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetNowPlayingResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetPlaylistResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.InsertRequestResponse;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PlaylistActivity extends AppCompatActivity {

//    public static final String BASE_WEB_SERVICE_URL = "http://192.168.43.144/mpcafe/"; // azhary
    public static final String BASE_WEB_SERVICE_URL = "http://192.168.0.123/mpcafe/"; //ryan ganteng
//    public static final String BASE_WEB_SERVICE_URL = "http://192.168.0.145/mpcafe/"; //ryan - VAIO
//    public static final String BASE_WEB_SERVICE_URL = "http://10.102.227.131/mpcafe/";

    private static final String LOG_TAG = "TG.PlaylistActivity";
    public static final int REQUEST_CODE_ADD_SONG = 1;
    private static final int CACHE_SIZE = 20 * 1024 * 1024; // 20 MB

    public static PlaylistService service;

    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Request> requests = new ArrayList<>();

    private Toolbar nowPlayingToolbar;
    private TextView nowPlayingTitle;
    private TextView nowPlayingSongTitle;
    private TextView nowPlayingSongArtist;
    private ImageView nowPlayingCover;
    private View nowPlayingCoverGradient;
    private RelativeLayout nowPlayingToolbarLayout;

    private Context mContext;

    private int currentToolbarColor;

    private NowPlayingSong nowPlaying = new NowPlayingSong("", "", "", "", "", "", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        nowPlayingToolbar = findViewById(R.id.playlist_toolbar);
        setSupportActionBar(nowPlayingToolbar);

        final CollapsingToolbarLayout collapsingToolbar =
                 findViewById(R.id.playlist_collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.playlist_appbar);
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
        nowPlayingSongTitle = findViewById(R.id.nowplaying_song_title);
        nowPlayingSongArtist = findViewById(R.id.nowplaying_song_artist);
        nowPlayingCover = findViewById(R.id.nowplaying_cover);
        nowPlayingCoverGradient = findViewById(R.id.nowplaying_gradient);
        nowPlayingToolbarLayout = findViewById(R.id.playlist_toolbar_layout);

        currentToolbarColor = getResources().getColor(R.color.colorPrimary);

        playlistRecyclerView = findViewById(R.id.playlist);
        playlistAdapter = new PlaylistAdapter(mContext, requests, Glide.with(this));

        playlistRecyclerView.setAdapter(playlistAdapter);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        //Retrieve Data
        this.initPlaylist(this);
        this.fetchCurrentlyPlaying();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchCurrentlyPlaying();
                fetchRequests();
            }
        }, 1000, 5000);
    }



    private void animateToolbarChange(int bgColorFrom, int bgColorTo, boolean isCoverShow){

        final int coverVisibility = isCoverShow ? View.VISIBLE : View.INVISIBLE;

        ValueAnimator bgAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), bgColorFrom, bgColorTo);
        bgAnimation.setDuration(500); // milliseconds

        Log.e(LOG_TAG, "Change animated!");

        bgAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                nowPlayingToolbar.setBackgroundColor((int) animator.getAnimatedValue());
                nowPlayingToolbarLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        bgAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                nowPlayingCover.setVisibility(coverVisibility);
                nowPlayingCoverGradient.setVisibility(coverVisibility);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        bgAnimation.start();
    }

    private void adjustToolbarStyle(){
        final int defaultToolbarColor = getResources().getColor(R.color.colorPrimary);
        int defaultToolbarTextColor = getResources().getColor(R.color.style_default_color);

        boolean isCoverArtSet = false;

        if(nowPlaying.getBase64Img() != null) isCoverArtSet = true;

        nowPlayingCover.setVisibility(View.INVISIBLE);
        nowPlayingCoverGradient.setVisibility(View.INVISIBLE);

        if(isCoverArtSet){

            Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {

                    Palette.Swatch dominantSwatch = palette.getDominantSwatch();
                    int bgColor = dominantSwatch.getRgb();

                    GradientDrawable drawable = new GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            new int[] { bgColor, ColorUtils.setAlphaComponent(bgColor, 0)}
                    );
                    nowPlayingCoverGradient.setBackground(drawable);

                    animateToolbarChange(currentToolbarColor, bgColor, true);
                    currentToolbarColor = bgColor;

                    nowPlayingTitle.setTextColor(dominantSwatch.getTitleTextColor());
                    nowPlayingSongTitle.setTextColor(dominantSwatch.getBodyTextColor());
                    nowPlayingSongArtist.setTextColor(dominantSwatch.getBodyTextColor());
                }};

            byte[] coverArtByte = Base64.decode(nowPlaying.getBase64Img(), Base64.DEFAULT);
            Bitmap coverArt = BitmapFactory.decodeByteArray(coverArtByte, 0, coverArtByte.length);
            Palette.from(coverArt).generate(paletteListener);

            Glide.with(this).load(coverArtByte).asBitmap().into(nowPlayingCover);
        } else {

            animateToolbarChange(currentToolbarColor, defaultToolbarColor, false);
            this.nowPlayingTitle.setTextColor(defaultToolbarTextColor);
            this.nowPlayingSongTitle.setTextColor(defaultToolbarTextColor);
            this.nowPlayingSongArtist.setTextColor(defaultToolbarTextColor);
        }
    }

    public void fetchRequests() {
        PlaylistActivity.checkService(this);
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

                playlistAdapter.sortDataset();
                playlistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<GetPlaylistResponse> call, Throwable t) {
                Log.e(LOG_TAG,"Error! " + t.toString());
            }
        });
    }

    private void fetchCurrentlyPlaying(){
        PlaylistActivity.checkService(this);
        Call<GetNowPlayingResponse> call = service.getCurrentlyPlaying();

        call.enqueue(new Callback<GetNowPlayingResponse>() {
            @Override
            public void onResponse(Call<GetNowPlayingResponse> call, Response<GetNowPlayingResponse> response) {
                Log.e(LOG_TAG,"Get Currently Playing JSON error? "
                        + String.valueOf(response.body().isError()));

                if(nowPlaying.getRequestID().
                        equals(
                                response.body().getNowPlaying().getRequestID())){
                    Log.e(LOG_TAG, "Still playing...");
                    return;
                } else {
                    nowPlaying = new NowPlayingSong(response.body().getNowPlaying());
                    Log.e(LOG_TAG, "Playin' new song!");
                    updateNowPlayingText();
                    adjustToolbarStyle();
                }

            }

            @Override
            public void onFailure(Call<GetNowPlayingResponse> call, Throwable t) {

            }
        });
    }

    private void updateNowPlayingText(){
        this.nowPlayingSongTitle.setText(nowPlaying.getTitle());
        this.nowPlayingSongArtist.setText(nowPlaying.getArtist());
    }

    @Override
    protected void onResume() {
        checkService(this);
        playlistAdapter = new PlaylistAdapter(mContext, requests, Glide.with(this));

        playlistRecyclerView.setAdapter(playlistAdapter);
        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
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

    public static void checkService(Context context){
        if(service == null){
            initPlaylist(context);
        }
    }

    public static void initPlaylist(Context cacheContext){

        Cache mCache = new Cache(cacheContext.getCacheDir(), CACHE_SIZE);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(mCache)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_WEB_SERVICE_URL)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create());

        Retrofit retrofit = builder.build();

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
