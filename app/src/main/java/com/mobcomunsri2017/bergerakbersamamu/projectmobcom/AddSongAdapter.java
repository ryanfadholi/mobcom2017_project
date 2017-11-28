package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

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
        public SongViewHolder(final View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.song_cover);
            title = (TextView) itemView.findViewById(R.id.song_title);
            artist = (TextView) itemView.findViewById(R.id.song_artist);

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

        if(songs.get(position).getMusicID().equals(listener.getSelectedSongID())){
            holder.title.setTypeface(boldTypeface);
            holder.artist.setTypeface(boldTypeface);
        } else {
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

