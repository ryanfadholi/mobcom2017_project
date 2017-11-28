package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import java.util.ArrayList;

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

    private RecyclerView addSongRecyclerView;
    private AddSongAdapter addSongAdapter;
    private ArrayList<Song> songs = new ArrayList<>();
   // private List<Song> songList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_song_toolbar);
        setSupportActionBar(toolbar);

        addSongRecyclerView = (RecyclerView)findViewById(R.id.add_song);
        addSongRecyclerView.setHasFixedSize(true);
        addSongAdapter = new AddSongAdapter(this, songs, this);

        // white background notification bar
        whiteNotificationBar(addSongRecyclerView);

        addSongRecyclerView.setAdapter(addSongAdapter);
        addSongRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchSongs();

        //Set bottom sheet listener
        View bottomSheet = findViewById( R.id.bottom_sheet );
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }

    private void fetchSongs() {

        PlaylistActivity.checkService();
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

                songs.clear();
                songs.addAll(response.body().getMusics());
                addSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GetMusicsRequestResponse> call, Throwable t) {
                Log.e(LOG_TAG,"Error! " + t.toString());
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
                addSongAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                addSongAdapter.getFilter().filter(query);
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onSongSelected(Song song) {
        Toast.makeText(getApplicationContext(), "Selected: " + song.getTitle() + ", " + song.getArtist(), Toast.LENGTH_LONG).show();
        startRequestSong(song.getMusicID());
    }
}
