package com.example.emily.mymusicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final int NOTIFY_ID = 1;
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPos;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private boolean isShuffle = false;
    private Random rand;
    private RecyclerView mainListMusic;
    private SongAdapter adapter;

    @Override
    public void onCreate() {
        super.onCreate();
        songPos = 0;
        rand = new Random();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        //player.setOnCompletionListener(this);
        player.setOnCompletionListener(new onCompletionListener());
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songs, RecyclerView mainListMusic, SongAdapter adapter) {
        this.songs = songs;
        this.mainListMusic = mainListMusic;
        this.adapter = adapter;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong(View v) {
        player.reset();
        Song playSong = songs.get(songPos);
        songTitle = playSong.getTitle();
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(MainActivity.STORAGE_LOCATION, currSong);
        try {
            player.setDataSource(getApplicationContext(), trackUri);
            if (v != null) {
                songs.get(songPos).setHasColor(true);
                RecyclerView recyclerView = (RecyclerView) v.getParent();
                songs.get(recyclerView.getChildAdapterPosition(v)).setHasColor(true);
                adapter.notifyItemChanged(recyclerView.getChildAdapterPosition(v));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    public void playPrev() {
        songPos--;
        if (songPos >= 0) {
            songPos = songs.size() - 1;
        }
        LinearLayout songLayout = (LinearLayout)mainListMusic.findViewById(R.id.songLayout);
        playSong(songLayout);
    }

    public void playNext() {
        if (isShuffle) {
            int newSong = songPos;
            while (newSong == songPos) {
                newSong = rand.nextInt(songs.size() - 1);
            }
            songPos = newSong;
        } else {
            songPos++;
            if (songPos >= songs.size() - 1) {
                songPos = 0;
            }
        }
        LinearLayout songLayout = (LinearLayout)mainListMusic.findViewById(R.id.songLayout);
        playSong(songLayout);
    }

    public void setShuffle() {
        isShuffle = !isShuffle;
    }

    public void setSong(int songPos) {
        this.songPos = songPos;
    }

    public int getPos(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int pos){
        player.seekTo(pos);
    }

    public void go(){
        player.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    private class onCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if(player.getCurrentPosition() > 0) {
                mp.reset();
                playNext();
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        //mp.setOnCompletionListener(new onCompletionListener());
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification notif = builder.build();

        startForeground(NOTIFY_ID, notif);
    }
}
