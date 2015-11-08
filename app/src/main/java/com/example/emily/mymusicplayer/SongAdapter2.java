package com.example.emily.mymusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter2 extends CursorRecyclerAdapter<SongAdapter2.ViewHolder> {

    private static ArrayList<Song> songs;
    private LayoutInflater inflater;
    private static SongAdapter.ClickListener clickListener;
    private static Context context;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;

    public SongAdapter2(Cursor c) {
        super(c);
    }

    public SongAdapter2(Cursor c, Context context, ArrayList<Song> songs) {
        super(c);
        this.songs = songs;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.song, parent, false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtSong;
        TextView txtSongArtist;
        LinearLayout linearLayout;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            txtSong = (TextView)v.findViewById(R.id.txtSongName);
            txtSongArtist = (TextView)v.findViewById(R.id.txtSongArtist);
            linearLayout = (LinearLayout)v.findViewById(R.id.songLayout);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            if(!songs.get(this.getAdapterPosition()).getHasColor())
                songs.get(this.getAdapterPosition()).setHasColor(true);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            if(!songs.get(this.getAdapterPosition()).getHasColor())
                songs.get(this.getAdapterPosition()).setHasColor(true);
            return false;
        }
    }
}



