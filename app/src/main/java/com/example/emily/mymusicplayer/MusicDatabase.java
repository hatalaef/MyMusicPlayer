package com.example.emily.mymusicplayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class MusicDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "music.db";
    private static final int DATABASE_VERSION = 1;

    private static SQLiteDatabase db = null;

    public interface TABLES {
        String SONGS = "Songs";
    }

    public interface SongsColumns {
        String ID = "id";
        String TITLE = "title";
        String ARTIST = "artist";
        String ALBUM = "album";
        String GENRE = "genre";
        String TIME = "timeStamp";
        String PATH = "filePath";
    }

    public MusicDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        //use if not reading new database
        //context.deleteDatabase(DATABASE_NAME);
    }

    public void closeDatabase() {
        if (db != null) {
            db.close();
        }
    }

    public void addAllSongsToDb(ArrayList<Song> songList, String directory, ContentResolver contentResolver) {


        for (Song song: songList) {
            addSongToDb(song, directory, contentResolver);
        }
        exportDB();

    }


    public void addSongToDb(Song song, String directory, ContentResolver contentResolver) {
        //add a song from the filesystem to the db

        String title = "";
        String artist = "";
        String album = "";
        String genre = "";
        String filepath = song.getPath().split(directory)[1];

        try {
            Mp3File mp3 = new Mp3File(song.getPath());

            if (mp3.hasId3v1Tag()) {
                ID3v1 tag = mp3.getId3v1Tag();
                title = tag.getTitle() != null ? tag.getTitle() : "";
                artist = tag.getArtist() != null ? tag.getArtist() : "";
                album = tag.getAlbum() != null ? tag.getAlbum() : "";
                genre = tag.getGenreDescription() != null ? tag.getGenreDescription() : "";
            }

            if (mp3.hasId3v2Tag()) {
                ID3v2 tag = mp3.getId3v2Tag();
                title = tag.getTitle() != null ? tag.getTitle() : "";
                artist = tag.getArtist() != null ? tag.getArtist() : "";
                album = tag.getAlbum() != null ? tag.getAlbum() : "";
                genre = tag.getGenreDescription() != null ? tag.getGenreDescription() : "";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        Uri uri = DataBaseProvider.URI_SONGS;
        ContentValues values = new ContentValues();
        values.put(SongsColumns.TITLE, title);
        values.put(SongsColumns.ARTIST, artist);
        values.put(SongsColumns.ALBUM, album);
        values.put(SongsColumns.GENRE, genre);
        values.put(SongsColumns.PATH, filepath);
        String selection = null;
        String[] selectionArgs = null;

        contentResolver.update(uri, values, selection, selectionArgs);

    }

    public Cursor getAllSongs(ContentResolver contentResolver) {

        Uri uri = DataBaseProvider.URI_SONGS;
        String[] columns = {SongsColumns.ID, SongsColumns.TITLE};
        String selection = null;
        String[] selectionArgs = null;
        String orderBy = null;
        return contentResolver.query(uri, columns, selection, selectionArgs, orderBy);

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

    //just for testing
    public void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/com.example.emily.mymusicplayer/databases/music.db";
        String backupDBPath = "musicCopy.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.d(MainActivity.DEBUG_TAG, "Created dbcopy in: " + backupDBPath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


}
