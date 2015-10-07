package com.example.emily.mymusicplayer;

public class Playlist {
    private String title;
    private boolean hasColor;
    private int visibleSongPos;
    private int duration;
    private int count;
    private String path;

    public Playlist(String title, int duration, int count, String path, int visibleSongPos) {
        this.title = title;
        this.duration = duration;
        this.count = count;
        this.path = path;
        this.visibleSongPos = visibleSongPos;
        hasColor = false;
    }


    public String getTitle() {
        return title;
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

    public int getCount() {
        return count;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return String.format("Playlist - Title: %s, Dur: %d, Count: %d, Uri: %s, HasColor: %b, VisibleSongPos: %d",
                title, duration, count, path, hasColor, visibleSongPos);
    }

}


