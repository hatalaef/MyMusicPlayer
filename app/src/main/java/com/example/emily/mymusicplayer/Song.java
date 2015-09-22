package com.example.emily.mymusicplayer;

import android.widget.LinearLayout;

public class Song {
    private long id;
    private String title;
    private String artist;
    boolean hasColor;
    private LinearLayout theView;

    public Song(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        hasColor = false;
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


