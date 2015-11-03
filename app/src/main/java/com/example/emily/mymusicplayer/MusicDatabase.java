package com.example.emily.mymusicplayer;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

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



}
