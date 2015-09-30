package com.example.emily.mymusicplayer;

public class Song {
    private long id;
    private String title;
    private String artist;
    private boolean hasColor;
    private int visibleSongPos;

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

    public int getVisibleSongPos() {
        return visibleSongPos;
    }

    public void setVisibleSongPos(int visibleSongPos) {
        this.visibleSongPos = visibleSongPos;
    }

    @Override
    public String toString() {
        return String.format("Song - Id: %d, Title: %s, Artist: %s, HasColor: %b", id, title, artist, hasColor);
    }

}


