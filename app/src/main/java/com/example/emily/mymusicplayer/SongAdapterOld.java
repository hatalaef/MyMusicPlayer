package com.example.emily.mymusicplayer;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//Adapter class for the RecyclerView. To get click listeners on items, had to use an interface. Getting tags from the view outside of the class seems very hard.
public class SongAdapterOld extends RecyclerView.Adapter<SongAdapterOld.ViewHolder> {

    private static ArrayList<Song> songs;
    private LayoutInflater inflater;
    private static ClickListener clickListener;
    private static Context context;

    public SongAdapterOld(Context context, ArrayList<Song> songs) {
        SongAdapterOld.songs = songs;
        SongAdapterOld.context = context;
        this.inflater = LayoutInflater.from(context);
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.song, viewGroup, false);
        return new ViewHolder(v);
    }

    //like the getView on a ListView, this is where the data is bound to the views. called when redrawn for scrolls or w/e
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtSong.setText(songs.get(i).getTitle());
        viewHolder.txtSongArtist.setText(songs.get(i).getArtist());

        viewHolder.txtSong.setTag(i); //used I think
        viewHolder.txtSongArtist.setTag(i); //maybe not used

        //change view if playing
        viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.songRow));
        if (songs.get(i).getHasColor()) {
            viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));
        }
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


    //used in the MainActivity
    public void setOnItemClickListener(ClickListener clickListener) {
        SongAdapterOld.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}
