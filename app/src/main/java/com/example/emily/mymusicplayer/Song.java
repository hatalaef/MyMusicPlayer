package com.example.emily.mymusicplayer;

import android.widget.LinearLayout;

public class Song {
    private long id;
    private String title;
    private String artist;
    private long listId;
    private int viewAdapterPos;
    boolean hasColor;
    private LinearLayout theView;

    public Song(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.listId = 0;
        hasColor = false;
        viewAdapterPos = 0;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    //ListId is probably unnecessary
    public void setListId(long listId) {
        this.listId = listId;
    }

    public long getListId() {
        return listId;
    }

    public int getViewAdapterPos() {
        return viewAdapterPos;
    }

    public void setViewAdapterPos(int viewAdapterPos) {
        this.viewAdapterPos = viewAdapterPos;
    }

    public boolean getHasColor() {
        return hasColor;
    }

    public void setHasColor(boolean hasColor) {
        this.hasColor = hasColor;
    }

    public LinearLayout getTheView() {
        return theView;
    }

    public void setTheView(LinearLayout theView) {
        this.theView = theView;
    }

}


