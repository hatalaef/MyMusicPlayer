package com.example.emily.mymusicplayer;

import android.content.Context;
import android.os.Environment;
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
