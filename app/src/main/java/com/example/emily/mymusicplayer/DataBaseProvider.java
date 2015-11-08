package com.example.emily.mymusicplayer;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class DataBaseProvider extends ContentProvider {

    private MusicDatabase db;
    private static final String AUTHORITY ="com.example.emily.musicplayer.DataBaseProvider";
    public static final int SONGS = 1;
    public static final int SONGS_ID = 2;

    private static final String SONGS_BASE_PATH = "songs";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + SONGS_BASE_PATH);
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, SONGS_BASE_PATH, SONGS);
        uriMatcher.addURI(AUTHORITY, SONGS_BASE_PATH + "/#", SONGS_ID);
    }



    @Override
    public boolean onCreate() {
        db = new MusicDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri)==SONGS) {
            return db.getAllSongs2();
        }
        else
            return null;
        /*
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MusicDatabase.TABLES.SONGS);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case SONGS_ID:
                queryBuilder.appendWhere(MusicDatabase.SongsColumns.ID + "=" + uri.getLastPathSegment());
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
        return null;
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