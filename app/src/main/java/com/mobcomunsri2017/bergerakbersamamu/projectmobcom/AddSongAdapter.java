package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
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

        ImageView cover;
        TextView title;
        TextView artist;
        Button addBtn;

        public SongViewHolder(final View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.song_cover);
            title = (TextView) itemView.findViewById(R.id.song_title);
            artist = (TextView) itemView.findViewById(R.id.song_artist);
            addBtn = (Button) itemView.findViewById(R.id.button_add_song);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected song in callback
                    Log.e(LOG_TAG, "itemOnClick Called");
                    listener.onSongSelected(songs.get(getAdapterPosition()));
                }
            });

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
        holder.title.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());

        String imageBytes = songs.get(position).getBase64Img();
        if (imageBytes.contains("null")) imageBytes = null;

        if (imageBytes != null) {
            byte[] imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);

            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    .into(holder.cover);
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
        notifyDataSetChanged();
    }

    public interface AddSongAdapterListener {
        void onSongSelected(Song song);
    }

}

