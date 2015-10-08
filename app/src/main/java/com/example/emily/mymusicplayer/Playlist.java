package com.example.emily.mymusicplayer;

import java.util.ArrayList;

public class Playlist {
    private String title;
    private int visibleSongPos;
    private int duration;
    private String path;
    private ArrayList<Song> songs;

    public Playlist(String title, int duration, int count, String path, ArrayList<Song> songs, int visibleSongPos) {
        this.title = title;
        this.duration = duration;
        this.path = path;
        this.songs = songs;
        this.visibleSongPos = visibleSongPos;
    }


    public String getTitle() {
        return title;
    }

    public int getVisibleSongPos() {
        return visibleSongPos;
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

    @Override
    public String toString() {
        return String.format("Playlist - Title: %s, Dur: %d, Count: %d, Uri: %s, VisibleSongPos: %d",
                title, duration, songs.size(), path);
    }

}


