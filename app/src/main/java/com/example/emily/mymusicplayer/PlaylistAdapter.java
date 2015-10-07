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
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private static ArrayList<Playlist> playlists;
    private LayoutInflater inflater;
    private static ClickListener clickListener;
    private static Context context;

    public PlaylistAdapter(Context context, ArrayList<Playlist> playlists) {
        PlaylistAdapter.playlists = playlists;
        PlaylistAdapter.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtPlaylistName;
        TextView txtPlaylistCount;
        LinearLayout linearLayout;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            txtPlaylistName = (TextView)v.findViewById(R.id.txtPlaylistName);
            txtPlaylistCount = (TextView)v.findViewById(R.id.txtPlaylistCount);
            linearLayout = (LinearLayout)v.findViewById(R.id.playlistLayout);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            if(!playlists.get(this.getAdapterPosition()).getHasColor())
                playlists.get(this.getAdapterPosition()).setHasColor(true);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            if(!playlists.get(this.getAdapterPosition()).getHasColor())
                playlists.get(this.getAdapterPosition()).setHasColor(true);
            return false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.playlist, viewGroup, false);
        return new ViewHolder(v);
    }

    //like the getView on a ListView, this is where the data is bound to the views. called when redrawn for scrolls or w/e
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtPlaylistName.setText(playlists.get(i).getTitle());
        viewHolder.txtPlaylistCount.setText(Integer.toString(playlists.get(i).getCount()));

        viewHolder.txtPlaylistName.setTag(i); //used I think
        viewHolder.txtPlaylistCount.setTag(i); //maybe not used

        //change view if playing
        viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.songRow));
        if (playlists.get(i).getHasColor()) {
            viewHolder.linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.accent));
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //used in the MainActivity
    public void setOnItemClickListener(ClickListener clickListener) {
        PlaylistAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}
