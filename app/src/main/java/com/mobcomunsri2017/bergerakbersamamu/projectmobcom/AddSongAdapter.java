package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    List<Song> songs;
    List<Song> songsFiltered;
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
                    // send selected contact in callback
                    listener.onSongSelected(songsFiltered.get(getAdapterPosition()));
                }
            });

        }
    }


    public AddSongAdapter(Context context, List<Song> songs, AddSongAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.songs = songs;
        this.songsFiltered = songs;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_song, parent, false);

        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, final int position) {
        final Song song = songsFiltered.get(position);
        holder.title.setText(songs.get(position).getTitle());
        holder.artist.setText(songs.get(position).getArtist());

//        Glide.with(context)
//                .load(song.getImage())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return songsFiltered.size();
    }

    //@Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    songsFiltered = songs;
                } else {
                    List<Song> filteredList = new ArrayList<>();
                    for (Song row : songs) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getArtist().toLowerCase().contains(charString.toLowerCase())
                                || row.getAlbum().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row);
                        }
                    }

                    songsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = songsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                songsFiltered = (ArrayList<Song>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

