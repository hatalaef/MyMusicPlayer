package com.example.emily.mymusicplayer;

import android.net.Uri;

public class Song {
    private long id;
    private String title;
    private String artist;
    private boolean hasColor;
    private int visibleSongPos;
    private int duration;
    private Uri uri;

    public Song(long id, String title, String artist, int duration, Uri uri, int visibleSongPos) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.uri = uri;
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

    public void setVisibleSongPos(int visibleSongPos) {
        this.visibleSongPos = visibleSongPos;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return String.format("Song - Id: %d, Title: %s, Artist: %s, Dur: %d, Uri: %s, HasColor: %b, VisibleSongPos: %d",
                id, title, artist, duration, uri.toString(), hasColor, visibleSongPos);
    }

}


