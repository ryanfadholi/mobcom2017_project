package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Request;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.retrofitresponses.InsertVoteResponse;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mobcomunsri2017.bergerakbersamamu.projectmobcom.PlaylistActivity.service;

/**
 * Created by Mifta on 11/15/2017.
 */

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.SongViewHolder> {

    public static final String LOG_TAG = "TG.AddSongAdapter";
    List<Song> songs;
    private Context context;
    private AddSongAdapterListener listener;

    //Untuk efek seleksi
    private int prevSelectedItemPos;

    //Untuk Glide
    private RequestManager glideInstance;

    public class SongViewHolder extends RecyclerView.ViewHolder {

        String musicId;

        CardView parent;

        ImageView cover;
        TextView title;
        TextView artist;

        TextView upCounter;
        TextView downCounter;
        ImageView upvote;
        ImageView downvote;

        boolean upvoted;
        boolean downvoted;

        int defaultColorCode;
        int selectedCode;

        SongViewHolder instance;

        public SongViewHolder(final View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.add_song_item);

            cover = itemView.findViewById(R.id.song_cover);
            title = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.song_artist);

            upCounter = itemView.findViewById(R.id.up_counter);
            downCounter = itemView.findViewById(R.id.down_counter);
            upvote = itemView.findViewById(R.id.song_upvote);
            downvote = itemView.findViewById(R.id.song_downvote);

            title.setSelected(true);
            artist.setSelected(true);

            instance = this;

            upvoted = false;
            downvoted = false;

            defaultColorCode = itemView.getResources().getColor(R.color.default_gray);
            selectedCode = itemView.getResources().getColor(R.color.style_default_color);

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upvoted = !upvoted;

                    int downCounterChange = 0;
                    int upCounterChange = 0;
                    int newVoteStatus = -1;

                    if(upvoted){
                        downCounterChange = downvoted ? -1 : 0;
                        upCounterChange = 1;

                        downvoted = false;
                        setVoteViewState(true, false);
                        newVoteStatus = 1;
                    } else {
                        setUpvoteViewState(false);
                        upCounterChange = -1;
                    }

                    attemptVote(instance, musicId, "1");

                    listener.setUserVoteStatus(musicId, newVoteStatus);
                    updateVoteCounter(upCounterChange, downCounterChange);
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downvoted = !downvoted;

                    int downCounterChange = 0;
                    int upCounterChange = 0;
                    int newVoteStatus = -1;

                    if(downvoted){
                        downCounterChange = 1;
                        upCounterChange = upvoted ? -1 : 0;

                        upvoted = false;
                        setVoteViewState(false, true);

                        newVoteStatus = 0;
                    } else {
                        setDownvoteViewState(false);
                        downCounterChange = -1;
                    }

                    attemptVote(instance, musicId, "0");

                    listener.setUserVoteStatus(musicId, newVoteStatus);
                    updateVoteCounter(upCounterChange, downCounterChange);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // send selected song in callback
                    Log.e(LOG_TAG, "itemOnClick Called");
                    listener.onSongSelected(songs.get(getAdapterPosition()));
                    notifyItemChanged(getAdapterPosition());

                    if (prevSelectedItemPos != -1) {
                        Log.e(LOG_TAG, getAdapterPosition() + ", Prev Pos: " + prevSelectedItemPos);
                        notifyItemChanged(prevSelectedItemPos);
                    }

                    prevSelectedItemPos = getAdapterPosition();
                }
            });

        }

        public void checkUserVote(String musicId){
            int voteType = listener.getUserVoteStatus(musicId);

            if (voteType == 1) {
                upvoted = true;
                downvoted = false;
                setVoteViewState(true, false);
            } else if (voteType == 0) {
                upvoted = false;
                downvoted = true;
                setVoteViewState(false, true);
            } else {
                upvoted = false;
                downvoted = false;
                setVoteViewState(false, false);
            }
        }


        public void setVoteViewState(boolean isUpvoted, boolean isDownvoted){
            this.setUpvoteViewState(isUpvoted);
            this.setDownvoteViewState(isDownvoted);
        }

        public void setUpvoteViewState(boolean isUpvoted){
            int newColor = isUpvoted ? selectedCode : defaultColorCode;

            upvote.setColorFilter(newColor);
            upCounter.setTextColor(newColor);
        }

        public void setDownvoteViewState(boolean isDownvoted){
            int newColor = isDownvoted ? selectedCode : defaultColorCode;

            downvote.setColorFilter(newColor);
            downCounter.setTextColor(newColor);
        }

        public void updateVoteCounter(int upvoteChange, int downvoteChange){
            this.updateUpCounter(upvoteChange);
            this.updateDownCounter(downvoteChange);
        }

        public void updateDownCounter(int change){
            if (change == 0) return;

            this.downCounter.setText(
                    String.valueOf(
                            Integer.parseInt(
                                    String.valueOf(
                                            downCounter.getText())) + change));

            listener.updateDownvoteCounter(this.musicId, change);
        }

        public void updateUpCounter(int change){
            if(change == 0) return;

            this.upCounter.setText(
                    String.valueOf(
                            Integer.parseInt(
                                    String.valueOf(
                                            upCounter.getText())) + change));

            listener.updateUpvoteCounter(this.musicId, change);
        }
    }

    public AddSongAdapter(Context context, List<Song> songs, AddSongAdapterListener listener, RequestManager glideInstance) {
        this.context = context;
        this.listener = listener;
        this.songs = songs;

        this.glideInstance = glideInstance;

        this.prevSelectedItemPos = -1;
    }

    public void sortDataset(){
        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song o1, Song o2) {
                return o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase());
            }
        });
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_song, parent, false);

        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, final int position) {

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        Typeface normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);

        String currentMusicId = songs.get(position).getMusicID();

        holder.title.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());
        holder.musicId = currentMusicId;

        holder.upCounter.setText(String.valueOf(listener.getUpvoteCounter(currentMusicId)));
        holder.downCounter.setText(String.valueOf(listener.getDownvoteCounter(currentMusicId)));
        holder.checkUserVote(currentMusicId);

        Log.e(LOG_TAG, songs.get(position).getTitle() + ", " + songs.get(position).getBase64Img());

        String imageBytes = songs.get(position).getBase64Img();

        byte[] imageByteArray = null;
        if (imageBytes != null) imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);

        glideInstance
                .load(imageByteArray)
                .asBitmap()
                .placeholder(R.drawable.p200x200)
                .fallback(R.drawable.p200x200)
                .error(R.drawable.p200x200)
                .into(holder.cover);

        if(songs.get(position).getMusicID().equals(listener.getSelectedSongID())){
            holder.title.setTypeface(boldTypeface);
            holder.artist.setTypeface(boldTypeface);

            prevSelectedItemPos = position;
        } else {
            holder.title.setTypeface(normalTypeface);
            holder.artist.setTypeface(normalTypeface);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateList(List<Song> list) {
        songs = list;
        sortDataset();
        notifyDataSetChanged();
    }

    public interface AddSongAdapterListener {
        void onSongSelected(Song song);

        String getSelectedSongID();

        Integer getDownvoteCounter(String musicID);
        void updateDownvoteCounter(String musicID, int value);

        Integer getUpvoteCounter(String musicID);
        void updateUpvoteCounter(String musicID, int value);

        Integer getUserVoteStatus(String musicID);
        void setUserVoteStatus(String musicID, int newStatus);

    }


    private void attemptVote(final SongViewHolder svh, final String musics_id, String type) {
        String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Call<InsertVoteResponse> call = service.setVote(musics_id, type, device_id);
        call.enqueue(new Callback<InsertVoteResponse>() {

            @Override
            public void onResponse(Call<InsertVoteResponse> call, Response<InsertVoteResponse> response) {
                Log.e(LOG_TAG,"Insert Request JSON error? " + String.valueOf(response.body().getError()));
                //svh.updateCounter(musics_id);


            }

            @Override
            public void onFailure(Call<InsertVoteResponse> call, Throwable t) {
                Log.e(LOG_TAG,"Error! " + t.toString());
            }
        });
    }

}

