package com.example.emily.mymusicplayer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class DataBaseProvider extends ContentProvider {

    //all uris share these parts
    public static final String AUTHORITY = "com.example.emily.mymusicplayer.mymusicprovider";
    public static final String SCHEME = "content://";

    //uris
    //used for all songs
    public static final String SONGS = SCHEME + AUTHORITY + "/" + MusicDatabase.TABLES.SONGS;
    public static final Uri URI_SONGS = Uri.parse(SONGS);

    public static final int SONGS_LIST = 1;
    public static final int SONGS_ITEM = 2;

    //used for a signal song, add id to end
    public static final String SONG_BASE = SONGS + "/";

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, MusicDatabase.TABLES.SONGS, SONGS_LIST);
        uriMatcher.addURI(AUTHORITY, MusicDatabase.TABLES.SONGS + "/#", SONGS_ITEM);
    }

    private MusicDatabase db;




    @Override
    public boolean onCreate() {
        db = new MusicDatabase(getContext());
        if(db == null) {
            Log.d(MainActivity.DEBUG_TAG, "DataBaseProvider: Didn't create db");
            return false;
        }
        else {
            Log.d(MainActivity.DEBUG_TAG, "DataBaseProvider: Created db");
            return true;
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MusicDatabase.TABLES.SONGS);

        switch (uriMatcher.match(uri)) {
            case SONGS_LIST:
                break;
            case SONGS_ITEM:
                queryBuilder.appendWhere(MusicDatabase.SongsColumns.ID + "=" + uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException(("Invalid URI: " + uri));
        }

        Cursor result = queryBuilder.query(db.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        //result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;


        //if(uriMatcher.match(uri)==SONGS) {
        //    return db.getAllSongs2(uri, projection, selection, selectionArgs, sortOrder);
        //}
        //else
        //    return null;
        /*
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MusicDatabase.TABLES.SONGS);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case SONGS_ID:
                queryBuilder.appendWhere(ID + "=" + uri.getLastPathSegment());
                break;
            case SONGS:
                //no filter
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");


        }

        Cursor cursor = queryBuilder.query(db.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
        */
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case SONGS_LIST:
                return null;
            case SONGS_ITEM:
                return null;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}