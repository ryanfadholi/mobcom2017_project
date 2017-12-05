package com.mobcomunsri2017.bergerakbersamamu.projectmobcom;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Request;
import com.mobcomunsri2017.bergerakbersamamu.projectmobcom.datastructures.Song;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Azhary Arliansyah on 14/11/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.SongViewHolder> {

    public static String LOG_TAG = "TG.PlaylistAdapter";

    List<Song> songs;
    List<Request> requests;
    RequestManager glideInstance;
    Context context;

//    public PlaylistAdapter(List<Song> songs) {
//        this.songs = songs;
//    }

    public PlaylistAdapter(Context context, List<Request> requests, RequestManager glideInstance) {
        this.requests = requests;
        this.context = context;
        this.glideInstance = glideInstance;
    }

    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        return new SongViewHolder(v);
    }

    public void sortDataset(){
        Collections.sort(requests, new Comparator<Request>() {
            @Override
            public int compare(Request o1, Request o2) {
                return o1.getRequestID().toLowerCase().compareTo(o2.getRequestID());
            }
        });
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.title.setText(requests.get(position).getTitle());
        holder.artist.setText(requests.get(position).getArtist());

        String imageBytes = requests.get(position).getBase64Img();

        byte[] imageByteArray = null;
        if (imageBytes != null) imageByteArray = Base64.decode(imageBytes, Base64.DEFAULT);

        glideInstance
                .load(imageByteArray)
                .asBitmap()
                .placeholder(R.drawable.p200x200)
                .fallback(R.drawable.p200x200)
                .error(R.drawable.p200x200)
                .into(holder.cover);
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

        public SongViewHolder(final View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.cover_item);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);
        }
    }

}
