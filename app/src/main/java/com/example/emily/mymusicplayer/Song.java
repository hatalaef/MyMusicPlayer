package com.example.emily.mymusicplayer;

public class Song {
    private long id;
    private String title;
    private String artist;
    private boolean hasColor;
    private int visibleSongPos;
    private int duration;
    private String path;

    public Song(String title, String artist, int duration, String path) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        hasColor = false;
    }

    public Song(long id, String title, String artist, int duration, String path, int visibleSongPos) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.visibleSongPos = visibleSongPos;
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

    public int getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("Song - Id: %d, Title: %s, Artist: %s, Dur: %d, Uri: %s, HasColor: %b, VisibleSongPos: %d",
                id, title, artist, duration, path, hasColor, visibleSongPos);
    }

}


