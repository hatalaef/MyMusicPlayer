package com.example.emily.mymusicplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.SyncFailedException;
import java.util.ArrayList;

public class CreatePlaylist {

    public final static String START_PLAYLIST = "#EXTM3U";
    public final static String MIDDLE_PLAYLIST = "#EXTINF:";
    private static ContentResolver musicResolver;
    private static int oldVisiblePos = 0;

    public static void setContentResolver(ContentResolver resolver) {
        musicResolver = resolver;
    }

    public static ArrayList<Song> songsFromPlaylist(ArrayList<Playlist> playlists, Playlist playlist, PlaylistAdapter adapter, int position) {

        //getting rid of old color
        playlists.get(oldVisiblePos).setHasColor(false);
        adapter.notifyItemChanged(oldVisiblePos);

        //adding new color
        playlists.get(position).setHasColor(true);
        adapter.notifyItemChanged(position);
        //adapter.notifyItemChanged(playlists.get(position).getVisiblePos());

        oldVisiblePos = position;
        //oldVisiblePos = playlists.get(position).getVisiblePos();

        ArrayList<Song> songList = new ArrayList<>();
        int i = 0;
        for (Song song: playlist.getSongs()) {
            //replace relative path with absolute path, kind of
            String thePath = song.getPath();
            song.setPath(thePath.replace("..", MainActivity.FOLDER_PATH));

            getSongList(song, songList, i);
            i++;
        }
        return songList;
    }

    //takes a song from a playlist and gives it the right data
    private static void getSongList(Song song, ArrayList<Song> songList, int i) {
        Uri musicUri = MainActivity.STORAGE_LOCATION;
        //gets music from certain folder
        Cursor musicCursor = musicResolver.query(musicUri, null, "UPPER(" + MediaStore.Audio.Media.DATA + ") LIKE UPPER(?) ",
                new String[]{"%" + song.getPath() + "%"}, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DURATION);
            int dataColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DATA);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                int theDuration = musicCursor.getInt(durationColumn);
                String thisPath = musicCursor.getString(dataColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist, theDuration, thisPath, i));
            }
            while (musicCursor.moveToNext());
        }
        else
            Log.d(MainActivity.DEBUG_TAG, String.format("Playlist: Couldn't find %s", song.getPath()));

        if (musicCursor != null) {
            musicCursor.close();
        }
    }

    public static void makePlaylist(ArrayList<Song> songs, String folderDir, String fileName, Context context) {
        File root = getPlaylistDir(folderDir);
        File theFile = new File(root, fileName);
        FileOutputStream stream;
        FileWriter writer;

        try {
            stream = new FileOutputStream(theFile);
            try {
                writer = new FileWriter(stream.getFD());
                writer.append(START_PLAYLIST);

                for (Song song : songs) {
                    writer.append("\n" + MIDDLE_PLAYLIST);
                    String info = String.format("%.0f, %s - %s%n", song.getDuration() / 1000.0, song.getArtist(), song.getTitle());
                    String relativePath = getRelativePath(theFile, root, song.getPath());

                    writer.append(info);
                    writer.append(relativePath);
                }

                writer.flush();
                writer.close();

            } catch (SyncFailedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                stream.getFD().sync();
                stream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getPlaylistDir(String folderDir) {
        File file = new File(Environment.getExternalStorageDirectory(), folderDir);
        if (!file.mkdirs()) {
            Log.d(MainActivity.DEBUG_TAG, "Directory not created");
        }
        return file;
    }

    //works for me, but probably not for all cases
    public static String getRelativePath(File file, File folder, String songPath) {
        String filePath = file.getAbsolutePath();
        String folderPath = folder.getAbsolutePath();
        String commonPath = "";
        String begPath = "";
        String relativePath;

        if (filePath.startsWith(folderPath)) {
            commonPath = filePath.substring(folderPath.length() + 1);
        }

        if(commonPath.startsWith("/"))
            commonPath.replaceFirst("/", "");

        String relatives[] = commonPath.split("/");
        for (int i = 0; i < relatives.length; i++) {
            begPath += "../";
        }

        relatives = songPath.split("/");
        relativePath = begPath + relatives[relatives.length - 1];


        return relativePath;
    }
}
