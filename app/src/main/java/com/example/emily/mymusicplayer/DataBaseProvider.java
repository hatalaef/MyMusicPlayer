package com.example.emily.mymusicplayer;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class DataBaseProvider extends ContentProvider {

    private static final String SONG_TABLE = MusicDatabase.TABLES.SONGS;

    //all uris share these parts
    public static final String AUTHORITY = "com.example.emily.mymusicplayer.mymusicprovider";
    public static final String SCHEME = "content://";

    //uris
    //used for all songs
    public static final String SONGS = SCHEME + AUTHORITY + "/" + SONG_TABLE;
    public static final Uri URI_SONGS = Uri.parse(SONGS);

    public static final int SONGS_LIST = 1;
    public static final int SONGS_ITEM = 2;

    //types
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/example.emily.mymusicplayer.music/ " + SONG_TABLE;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/example.emily.mymusicplayer/music/" + SONG_TABLE;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, SONG_TABLE, SONGS_LIST);
        uriMatcher.addURI(AUTHORITY, SONG_TABLE + "/#", SONGS_ITEM);
    }

    private SQLiteDatabase db;




    @Override
    public boolean onCreate() {
        MusicDatabase mDb = new MusicDatabase(getContext());
        db = mDb.getWritableDatabase();

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
        queryBuilder.setTables(SONG_TABLE);

        switch (uriMatcher.match(uri)) {
            case SONGS_LIST:
                break;
            case SONGS_ITEM:
                queryBuilder.appendWhere(MusicDatabase.SongsColumns.ID + "=" + uri.getLastPathSegment());
            default:
                throw new IllegalArgumentException(("Invalid URI: " + uri));
        }

        Cursor result = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        //result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case SONGS_LIST:
                return CONTENT_TYPE;
            case SONGS_ITEM:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != SONGS_LIST) {
            throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        long id = db.insert(SONG_TABLE, null, values);

        if (id > 0)
            return ContentUris.withAppendedId(uri, id);
        throw new SQLException("Error inserting into table " +SONG_TABLE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted = 0;

        switch (uriMatcher.match(uri)) {
            case SONGS_LIST:
                db.delete(SONG_TABLE, selection, selectionArgs);
                break;
            case SONGS_ITEM:
                String where = MusicDatabase.SongsColumns.ID + " = " + uri.getLastPathSegment();
                if (!selection.isEmpty())
                    where += " AND " + selection;
                deleted = db.delete(SONG_TABLE, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int updated = 0;

        switch (uriMatcher.match(uri)) {
            case SONGS_LIST:
                db.updateWithOnConflict(SONG_TABLE, values, selection, selectionArgs, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            case SONGS_ITEM:
                String where = MusicDatabase.SongsColumns.ID + " = " + uri.getLastPathSegment();
                if (!selection.isEmpty())
                    where += " AND " + selection;
                updated = db.updateWithOnConflict(SONG_TABLE, values, where, selectionArgs, SQLiteDatabase.CONFLICT_IGNORE);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }

        return updated;
    }

}