package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetCountAllVotesResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetMusicsRequestResponse;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.GetUserAllVotesResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobcomunsri2017.bergerakbersamamu.projectmobcom.PlaylistActivity.service;

/**
 * Created by Mifta on 11/15/2017.
 */

public class AddSongActivity extends AppCompatActivity implements AddSongAdapter.AddSongAdapterListener {

    private static final String LOG_TAG = "TG.AddSongActivity";

    public static String EXTRA_MUSIC_ID = "AddSong.MusicID";

    private ProgressBar addSongProgressBar;
    private RecyclerView addSongRecyclerView;
    private AddSongAdapter addSongAdapter;

    private ArrayList<Song> songs = new ArrayList<>();

    private HashMap<String, Integer> downvotes = new HashMap<>();
    private HashMap<String, Integer> upvotes = new HashMap<>();
    private HashMap<String, Integer> userVotes = new HashMap<>();

    private List<Song> songList;
    private SearchView searchView;

    //For bottom sheet purposes
    private View mBottomSheetPadding;
    private TextView mBottomSheetText;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton mAddSongFab;

    //To store the selected song in the AddSongAdapter;
    private String selectedSongID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        Toolbar toolbar = findViewById(R.id.add_song_toolbar);
        setSupportActionBar(toolbar);

        setTitle("Tracklist");

        addSongProgressBar = findViewById(R.id.addsong_progress);
        addSongProgressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.style_default_color),
                PorterDuff.Mode.MULTIPLY
        );

        addSongRecyclerView = findViewById(R.id.add_song);
        addSongRecyclerView.setHasFixedSize(true);
        addSongAdapter = new AddSongAdapter(this, songs, this, Glide.with(this));

        addSongRecyclerView.setAdapter(addSongAdapter);
        addSongRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set bottom sheet things
        mBottomSheetPadding = findViewById(R.id.sheetPadding);

        mBottomSheetText = findViewById(R.id.addsong_bottom_sheet_text);
        mBottomSheetText.setText("");

        mAddSongFab = findViewById(R.id.addsong_fab);
        mAddSongFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequestSong(selectedSongID);
            }
        });

        View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        fetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedSongID = "-1";
        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            hideBottomSheet();
        }
        fetchData();
    }

    private void showBottomSheet(Song chosenSong){

        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            mBottomSheetText.setText(chosenSong.getTitle() + " - " + chosenSong.getArtist());
            return;
        }

        //Show the bottom sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        mBottomSheetBehavior.setHideable(false);

        //Handle floating action button animations
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mAddSongFab.getLayoutParams();
        p.gravity = Gravity.NO_GRAVITY;
        p.anchorGravity = Gravity.TOP | Gravity.END;
        p.setAnchorId(R.id.bottom_sheet);
        mAddSongFab.setLayoutParams(p);
        mAddSongFab.show();

        mBottomSheetText.setText(chosenSong.getTitle() + " - " + chosenSong.getArtist());
    }

    private void hideBottomSheet(){

        //Hide the bottom sheet
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Handle floating action button animations
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mAddSongFab.getLayoutParams();
        p.gravity = Gravity.BOTTOM | Gravity.END;
        p.setAnchorId(View.NO_ID);
        mAddSongFab.setLayoutParams(p);
        mAddSongFab.hide();
    }

    private void fetchData(){
        addSongRecyclerView.setVisibility(View.GONE);
        addSongProgressBar.setVisibility(View.VISIBLE);
        this.fetchVotes();
        this.fetchUserVotes();
        this.fetchSongs();
    }

    private void fetchSongs() {

        PlaylistActivity.checkService(this);
        Call<GetMusicsRequestResponse> call = service.getMusics();

        call.enqueue(new Callback<GetMusicsRequestResponse>() {

            @Override
            public void onResponse(Call<GetMusicsRequestResponse> call, Response<GetMusicsRequestResponse> response) {
                Log.e(LOG_TAG,"Get Musics JSON error? " + String.valueOf(response.body().isError()));

                if(response.body().isError()){
                    Log.e(LOG_TAG, "Get Musics JSON Error, " + response.body().getErrorMessage());
                    Snackbar.make(findViewById(android.R.id.content),
                            "We can't check available songs right now. " +
                                    "Please try again in a moment.",
                            Snackbar.LENGTH_LONG);
                    songs.clear();
                    return;
                }

                ArrayList<Song> tracklist = response.body().getMusics();

                //for searchdata
                songList = new ArrayList<>();
                songList.addAll(tracklist);

                songs.clear();
                songs.addAll(tracklist);

                addSongAdapter.sortDataset();
                addSongAdapter.notifyDataSetChanged();

                Log.e(LOG_TAG, "Should be here anytime soon....");
                addSongProgressBar.setVisibility(View.GONE);
                addSongRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<GetMusicsRequestResponse> call, Throwable t) {
                Log.e(LOG_TAG,"Error! " + t.toString());
            }
        });
    }

    private void fetchUserVotes(){
        String device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        PlaylistActivity.checkService(this);
        Call<GetUserAllVotesResponse> call = service.chcekAllUserVotes(device_id);

        call.enqueue(new Callback<GetUserAllVotesResponse>() {
            @Override
            public void onResponse(Call<GetUserAllVotesResponse> call, Response<GetUserAllVotesResponse> response) {
                Log.e(LOG_TAG,"Get User Votes JSON error? " + String.valueOf(response.body().isError()));

                if(response.body().isError()) {
                    Log.e(LOG_TAG, "Get User Votes JSON Error, " + response.body().getErrorMessage());
                }

                userVotes.clear();
                userVotes.putAll(response.body().checkUserVotes());

                Log.e(LOG_TAG,"refreshed user votes: " + userVotes.toString());
            }

            @Override
            public void onFailure(Call<GetUserAllVotesResponse> call, Throwable t) {

            }
        });
    }

    private void fetchVotes(){
        PlaylistActivity.checkService(this);
        Call<GetCountAllVotesResponse> call = service.getAllVotes();

        call.enqueue(new Callback<GetCountAllVotesResponse>() {
            @Override
            public void onResponse(Call<GetCountAllVotesResponse> call, Response<GetCountAllVotesResponse> response) {
                Log.e(LOG_TAG,"Get All Votes JSON error? " + String.valueOf(response.body().isError()));

                upvotes.clear();
                upvotes.putAll(response.body().getUpvotes());

                downvotes.clear();
                downvotes.putAll(response.body().getDownvotes());

                Log.e(LOG_TAG,"refreshed upvotes: " + upvotes.toString());
                Log.e(LOG_TAG,"refreshed downvotes: " + downvotes.toString());
            }

            @Override
            public void onFailure(Call<GetCountAllVotesResponse> call, Throwable t) {

            }
        });
    }

    private void startRequestSong(String musicID){
        Intent returnIntent = new Intent();

        Log.e(LOG_TAG,"Song Request Intent method handler called");

        returnIntent.putExtra(EXTRA_MUSIC_ID, musicID);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                //addSongAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                query = query.toLowerCase();
                songs.clear();
                if (query.length() <= 0) {
                    songs.addAll(songList);
                } else {
                    for (Song song : songList) {
                        if (song.getTitle().toLowerCase().contains(query)) {
                            songs.add(song);
                        }
                    }
                }
                addSongAdapter.sortDataset();
                addSongAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }


    //----------------------------------------------------------------
    //START ADDSONGADAPTER LISTENER IMPLEMENTATION
    @Override
    public void onSongSelected(Song song) {
        String newSelectedSong = song.getMusicID();

        //If user selects the same song ID, assume they want to cancel it.
        if(selectedSongID.equals(newSelectedSong)){
            this.selectedSongID = "0";
            hideBottomSheet();
        //else show it!
        } else {
            this.selectedSongID = song.getMusicID();
            showBottomSheet(song);
        }
    }

    @Override
    public String getSelectedSongID() {
        return this.selectedSongID;
    }

    @Override
    public Integer getDownvoteCounter(String musicID) {
        Integer result = this.downvotes.get(musicID);

        return (result != null) ? result : 0;
    }

    @Override
    public Integer getUpvoteCounter(String musicID) {
        Integer result = this.upvotes.get(musicID);

        return (result != null) ? result : 0;
    }

    @Override
    public Integer getUserVoteStatus(String musicID) {
        Integer userVote = userVotes.get(musicID);

        return userVote != null ? userVote : -1;
    }

    @Override
    public void setUserVoteStatus(String musicID, int newStatus){
        userVotes.put(musicID, newStatus);
    }

    @Override
    public void updateDownvoteCounter(String musicID, int value) {
        downvotes.put(musicID, downvotes.get(musicID) + value);
    }

    @Override
    public void updateUpvoteCounter(String musicID, int value) {
        upvotes.put(musicID, upvotes.get(musicID) + value);
    }

    //END ADDSONGADAPTER LISTENER IMPLEMENTATION
    //----------------------------------------------------------------
}
