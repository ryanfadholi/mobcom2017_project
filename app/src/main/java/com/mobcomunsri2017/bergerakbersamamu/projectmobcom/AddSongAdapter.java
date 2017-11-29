package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mifta on 11/15/2017.
 */

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.SongViewHolder> {

    public static final String LOG_TAG = "TG.AddSongAdapter";
    List<Song> songs;
    private Context context;
    private AddSongAdapterListener listener;

    public class SongViewHolder extends RecyclerView.ViewHolder {

        CardView parent;
        ImageView cover;
        TextView title;
        TextView artist;
        TextView counter;
        ImageView upvote;
        ImageView downvote;

        boolean upvoted;
        boolean downvoted;
        int voteCounter = 0;

        int defaultColorCode;
        int upColorCode;
        int downColorCode;

        int itemSelectedColorCode;
        int itemUnselectedColorCode;

        public SongViewHolder(final View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.add_song_item);
            cover = itemView.findViewById(R.id.song_cover);
            title = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.song_artist);
            counter = itemView.findViewById(R.id.song_vote_count);
            upvote = itemView.findViewById(R.id.song_upvote);
            downvote = itemView.findViewById(R.id.song_downvote);

            upvoted = false;
            downvoted = false;

            defaultColorCode = itemView.getResources().getColor(R.color.default_gray);
            upColorCode = itemView.getResources().getColor(R.color.upvote_green);
            downColorCode = itemView.getResources().getColor(R.color.downvote_red);

            itemSelectedColorCode = itemView.getResources().getColor(R.color.colorAccent);
            itemUnselectedColorCode = itemView.getResources().getColor(android.R.color.white);

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upvoted = !upvoted;

                    String snackbarText;

                    if(upvoted){
                        downvoted = false;
                        downvote.setColorFilter(defaultColorCode);

                        snackbarText = "Upvoted " + title.getText();
                        ((ImageView) v).setColorFilter(upColorCode);
                        voteCounter = 1;

                    } else {
                        snackbarText = title.getText() + " vote cancelled";
                        ((ImageView) v).setColorFilter(defaultColorCode);
                        voteCounter = 0;
                    }

                    updateCounter();
                    Snackbar.make(v, snackbarText, Snackbar.LENGTH_SHORT).show();
                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downvoted = !downvoted;

                    String snackbarText;

                    if(downvoted){
                        upvoted = false;
                        upvote.setColorFilter(defaultColorCode);

                        snackbarText = "Downvoted " + title.getText();
                        ((ImageView) v).setColorFilter(downColorCode);
                        voteCounter = -1;
                    } else {
                        snackbarText = title.getText() + " vote cancelled";
                        ((ImageView) v).setColorFilter(defaultColorCode);
                        voteCounter = 0;
                    }

                    updateCounter();
                    Snackbar.make(v, snackbarText, Snackbar.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected song in callback
                    Log.e(LOG_TAG, "itemOnClick Called");
                    listener.onSongSelected(songs.get(getAdapterPosition()));
                    notifyDataSetChanged();
                }
            });

        }

        public void updateCounter(){
            String counterText = String.valueOf(voteCounter);

            if(voteCounter > 0){
                counterText = "+" + counterText;
                counter.setTextColor(upColorCode);
            } else if(voteCounter < 0){
                counter.setTextColor(downColorCode);
            } else {
                counter.setTextColor(defaultColorCode);
            }

            counter.setText(counterText);

        }
    }

    public AddSongAdapter(Context context, List<Song> songs, AddSongAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.songs = songs;
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

        holder.title.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());

        String imageBytes = songs.get(position).getBase64Img();
        if (imageBytes.contains("null")) imageBytes = null;

        if (imageBytes != null) {
            byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);

            Glide.with(holder.itemView.getContext())
                    .load(imageByteArray)
                    .asBitmap()
                    .into(holder.cover);
        }

        if(songs.get(position).getMusicID().equals(listener.getSelectedSongID())){
//            holder.parent.setCardBackgroundColor(holder.itemSelectedColorCode);
            holder.title.setTypeface(boldTypeface);
            holder.artist.setTypeface(boldTypeface);
        } else {
//            holder.parent.setCardBackgroundColor(holder.itemUnselectedColorCode);
            holder.title.setTypeface(normalTypeface);
            holder.artist.setTypeface(normalTypeface);
        }
//        Glide.with(context)
//                .load(song.getImage())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
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
        notifyDataSetChanged();
    }

    public interface AddSongAdapterListener {
        void onSongSelected(Song song);
        String getSelectedSongID();
    }

}

