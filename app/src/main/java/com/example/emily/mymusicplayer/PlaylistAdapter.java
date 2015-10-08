package com.example.emily.mymusicplayer;

import android.content.Context;
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
        TextView txtPlaylistDuration;
        LinearLayout linearLayout;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            txtPlaylistName = (TextView)v.findViewById(R.id.txtPlaylistName);
            txtPlaylistCount = (TextView)v.findViewById(R.id.txtPlaylistCount);
            txtPlaylistDuration = (TextView)v.findViewById(R.id.txtPlaylistDuration);
            linearLayout = (LinearLayout)v.findViewById(R.id.playlistLayout);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
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
        String theTime;
        int theDuration = playlists.get(i).getDuration();
        int second = theDuration % 60;
        int minute = (theDuration /  60) % 60;
        int hour = (theDuration / (60 * 60)) % 60;

        if (hour > 0) {
            theTime = String.format("%d:%02d:%02d", hour, minute, second);
        }
        else {
            theTime = String.format("%d:%02d", minute, second);
        }

        viewHolder.txtPlaylistName.setText(playlists.get(i).getTitle());
        viewHolder.txtPlaylistCount.setText(Integer.toString(playlists.get(i).getCount()) + " songs");
        viewHolder.txtPlaylistDuration.setText(theTime);

        viewHolder.txtPlaylistName.setTag(i); //used I think
        viewHolder.txtPlaylistCount.setTag(i);
        viewHolder.txtPlaylistDuration.setTag(i);//maybe not used
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
