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
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final int NOTIFY_ID = 1;
    private static final int SEEKBAR_TIME = 1000;

    private Handler seekHandler = new Handler();
    private Runnable runnable;

    private MusicControls musicControls;
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPos;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private String songArtist = "";
    private boolean isShuffle = false;
    private Random rand;
    private SongAdapter adapter;
    private int oldSong;

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

    public void setList(ArrayList<Song> songs,SongAdapter adapter, MusicControls musicControls) {
        this.songs = songs;
        this.adapter = adapter;
        this.musicControls = musicControls;
        oldSong = 0;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong() {
        player.reset();
        Song playSong = songs.get(songPos);
        songTitle = playSong.getTitle();
        songArtist = playSong.getArtist();
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(MainActivity.STORAGE_LOCATION, currSong);

        try {
            Log.d(MainActivity.DEBUG_TAG, playSong.toString());
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepareAsync();

            //getting rid of old color
            songs.get(oldSong).setHasColor(false);
            adapter.notifyItemChanged(oldSong);
            Log.d(MainActivity.DEBUG_TAG, String.format("OldSongPos: %d", oldSong));

             //adding new color
            songs.get(songPos).setHasColor(true);
            adapter.notifyItemChanged(songPos);
            oldSong = songPos;
            Log.d(MainActivity.DEBUG_TAG, String.format("NewSongPos: %d", songPos));

        } catch (IOException e) {
            e.printStackTrace();
            //Todo - Doesn't show the right information on exception
        }
    }

    public void playPrev() {
        songPos--;
        if (songPos < 0) {
            songPos = songs.size() - 1;
        }
        playSong();
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
            if (songPos > songs.size() - 1) {
                songPos = 0;
            }
        }
        playSong();
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
        if(runnable != null) {
            seekHandler.removeCallbacks(runnable);
        }
        musicControls.updatePlayButton(true);
    }

    public void seek(int pos){
        player.seekTo(pos);
    }

    public void go(){
        player.start();
        musicControls.updatePlayButton(false);
    }

    public void seekBarTimer() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                musicControls.setSeekBarPos(getPos());
                seekHandler.postDelayed(this, SEEKBAR_TIME);
            }
        };
        seekHandler.postDelayed(runnable, SEEKBAR_TIME);
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

        musicControls.updatePlayButton(false);
        musicControls.setSongInfo(songTitle, songArtist);
        musicControls.setSeekBarMax(getDur());
        musicControls.setSeekBarPos(getPos());

        mp.start();
        //mp.setOnCompletionListener(new onCompletionListener());
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        seekBarTimer();

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(MusicControls.PLAY_RESOURCE)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification notif = builder.build();

        startForeground(NOTIFY_ID, notif);
    }
}
