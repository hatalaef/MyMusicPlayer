package com.example.emily.mymusicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<Song> songs;
    private LayoutInflater inflater;


    public SongAdapter(Context context, ArrayList<Song> songs) {
        this.songs = songs;
        this.inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSong;
        TextView txtSongArtist;
        public ViewHolder(View v) {
            super(v);
            txtSong = (TextView)v.findViewById(R.id.txtSongName);
            txtSongArtist = (TextView)v.findViewById(R.id.txtSongArtist);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.song, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtSong.setText(songs.get(i).getTitle());
        viewHolder.txtSongArtist.setText(songs.get(i).getArtist());
    }

    @Override
    public long getItemId(int position) {
        return songs.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
