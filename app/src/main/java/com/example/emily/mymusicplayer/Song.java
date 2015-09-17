package com.example.emily.mymusicplayer;

public class Song {
    private long id;
    private String title;
    private String artist;
    private int listId;
    private int viewAdapterPos;
    boolean hasColor;

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
    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getListId() {
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

}


