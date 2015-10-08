package com.example.emily.mymusicplayer;

import java.util.ArrayList;

public class Playlist {
    private String title;
    private int visiblePos;
    private int duration;
    private String path;
    private boolean hasColor;
    private ArrayList<Song> songs;

    public Playlist(String title, int duration, int count, String path, ArrayList<Song> songs, int visiblePos) {
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.songs = songs;
        this.visiblePos = visiblePos;
        hasColor = false;
    }


    public String getTitle() {
        return title;
    }

    public int getVisiblePos() {
        return visiblePos;
    }

    public int getDuration() {
        return duration;
    }

    public int getCount() {
        return songs.size();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getHasColor() {
        return hasColor;
    }

    public void setHasColor(boolean hasColor) {
        this.hasColor = hasColor;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    @Override
    public String toString() {
        return String.format("Playlist - Title: %s, Dur: %d, Count: %d, HasColor: %b, Uri: %s, VisibleSongPos: %d",
                title, duration, songs.size(), hasColor, path, visiblePos);
    }

}


