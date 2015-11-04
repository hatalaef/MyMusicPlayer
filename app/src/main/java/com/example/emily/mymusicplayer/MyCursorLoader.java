package com.example.emily.mymusicplayer;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MyCursorLoader extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorLoader cursorLoader;
    public static final String TAG = MyCursorLoader.class.getSimpleName();
    private static final int LOADER_ID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {

            case LOADER_ID:
                String[] projection = {MusicDatabase.SongsColumns.ID, MusicDatabase.SongsColumns.TITLE};
                cursorLoader = new CursorLoader(this, null, projection, null, null, null);
                return cursorLoader;

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        do {
            //do something
        } while (data.moveToNext());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
