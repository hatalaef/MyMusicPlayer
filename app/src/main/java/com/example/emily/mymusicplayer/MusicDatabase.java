package com.example.emily.mymusicplayer;

import android.content.Context;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.IOException;
import java.util.HashMap;

public class MusicDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "music";
    private static final int DATABASE_VERSION = 1;

    private double skill;
    private HashMap<String, Integer> perks;
    private double fortifyAlchemy;

    public interface TABLES {
        String SONGS = "Songs";
    }

    public interface SongsColumns {
    }

    public MusicDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //use if not reading new database
        //context.deleteDatabase(DATABASE_NAME);
    }


    public void addSongToDb(String filepath) {
        //add a song from the filesystem to the db
        try {
            Mp3File mp3 = new Mp3File(filepath);

            if (mp3.hasId3v2Tag()) {
                ID3v2 tag = mp3.getId3v2Tag();
                String title = tag.getTitle();
                String artist = tag.getArtist();
                String album = tag.getAlbum();
                String genre = tag.getGenreDescription();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

    }

    public int findSongByTitle(String title) {
        //sql query to get song from db
        return 0;
    }

    public void updateSong(int id, String title, String artist, String album, String genre, String fileparameter) {
        //sql query to update
    }

    public long compareTimes(String filePath) {
        //compare a song's last modified
        return 0;
    }

    public void updateSinceLastRun(String fileName) {
        //just update new songs
    }


}
