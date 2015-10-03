package com.example.emily.mymusicplayer;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CreatePlaylist {

    public final static String START_PLAYLIST = "#EXTM3U";
    public final static String MIDDLE_PLAYLIST = "#EXTINF:";

    private ArrayList<Song> songs;
    private String folderPath;

    public static void makePlaylist(ArrayList<Song> songs, String folderDir, String fileName) {
        File root = getPlaylistDir(folderDir);
        File theFile = new File(root, fileName);


        if(!theFile.exists()) {
            try {
                Log.d(MainActivity.DEBUG_TAG, "Creating file");
                theFile.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        else
            Log.d(MainActivity.DEBUG_TAG, "Didn't create file");

        Log.d(MainActivity.DEBUG_TAG, theFile.getPath());

        try {
            FileWriter writer = new FileWriter(theFile);
            writer.write("here");
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        try {
            FileWriter writer = new FileWriter(theFile);
            writer.append(START_PLAYLIST + "\n");
            for (Song song : songs) {
                writer.append(MIDDLE_PLAYLIST);
                String info = String.format("%d, %s - %s%n", song.getDuration(), song.getArtist(), song.getTitle());
                String path = (song.getUri().getPath() + "\n");
                writer.append(info);
                writer.append(path);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

    private static File getPlaylistDir(String folderDir) {
        File file = new File(Environment.getExternalStorageDirectory(), folderDir);
        if (!file.mkdirs()) {
            Log.d(MainActivity.DEBUG_TAG, "Directory not created");
        }
        return file;
    }

}
