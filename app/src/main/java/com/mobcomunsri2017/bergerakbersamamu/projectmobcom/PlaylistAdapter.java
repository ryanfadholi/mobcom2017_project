package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Azhary Arliansyah on 14/11/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.SongViewHolder> {

    List<Song> songs;

    public PlaylistAdapter(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        SongViewHolder svh = new SongViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.title.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title;
        TextView artist;
        ImageView upvoteBtn;
        ImageView downvoteBtn;
        boolean upvoted;
        boolean downvoted;

        public SongViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover_item);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);
            upvoteBtn = (ImageView) itemView.findViewById(R.id.upvote);
            downvoteBtn = (ImageView) itemView.findViewById(R.id.downvote);

            upvoted = false;
            downvoted = false;

            upvoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upvoted = !upvoted;
                }
            });
        }
    }

}
