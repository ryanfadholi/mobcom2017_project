package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Request;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Azhary Arliansyah on 14/11/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.SongViewHolder> {

    List<Song> songs;
    List<Request> requests;
    boolean inAddSong = false;

//    public PlaylistAdapter(List<Song> songs) {
//        this.songs = songs;
//    }

    public PlaylistAdapter(List<Song> songs, boolean inAddSong) {
        this.songs = songs;
        this.inAddSong = inAddSong;
    }

    public PlaylistAdapter(List<Request> requests) {this.requests = requests;}

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemView;
        if (!this.inAddSong) {
            itemView = R.layout.item_playlist;
        } else {
            itemView = R.layout.item_add_song;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(itemView, parent, false);
        SongViewHolder svh = new SongViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.title.setText(requests.get(position).getTitle());
        holder.artist.setText(requests.get(position).getArtist());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title;
        TextView artist;
        Button addBtn;
        ImageView upvoteBtn;
        ImageView downvoteBtn;
        boolean upvoted;
        boolean downvoted;

        public SongViewHolder(final View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover_item);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);
            upvoteBtn = (ImageView) itemView.findViewById(R.id.upvote);
            downvoteBtn = (ImageView) itemView.findViewById(R.id.downvote);
            addBtn = (Button) itemView.findViewById(R.id.button_add_song);

            upvoted = false;
            downvoted = false;

            upvoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upvoted = !upvoted;

                    String snackbarText;

                    if(upvoted){
                        downvoted = false;
                        downvoteBtn.setColorFilter(itemView.getResources().getColor(R.color.default_gray));

                        snackbarText = "Upvoted " + title.getText();
                        ((ImageView) v).setColorFilter(itemView.getResources().getColor(R.color.upvote_green));
                    } else {
                        snackbarText = title.getText() + " vote cancelled";
                        ((ImageView) v).setColorFilter(itemView.getResources().getColor(R.color.default_gray));

                    }

                    Snackbar.make(v, snackbarText, Snackbar.LENGTH_SHORT).show();
                }
            });

            downvoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downvoted = !downvoted;

                    String snackbarText;

                    if(downvoted){
                        upvoted = false;
                        upvoteBtn.setColorFilter(itemView.getResources().getColor(R.color.default_gray));

                        snackbarText = "Downvoted " + title.getText();
                        ((ImageView) v).setColorFilter(itemView.getResources().getColor(R.color.downvote_red));
                    } else {
                        snackbarText = title.getText() + " vote cancelled";
                        ((ImageView) v).setColorFilter(itemView.getResources().getColor(R.color.default_gray));

                    }

                    Snackbar.make(v, snackbarText, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

}
