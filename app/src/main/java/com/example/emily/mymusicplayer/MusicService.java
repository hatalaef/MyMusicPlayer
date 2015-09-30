package com.example.emily.mymusicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final int NOTIFY_ID = 1;
    private static final int SEEKBAR_TIME = 300;

    private Handler seekHandler = new Handler();
    private Runnable runnable;

    private MusicControls musicControls;
    private MediaPlayer player;
    private ArrayList<Song> songs;
    private ArrayList<Song> nowPlayingSongs;
    private int songPos;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle = "";
    private String songArtist = "";
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private boolean playAfterPrepare = true;
    //private Random rand;
    private SongAdapter adapter;
    private int oldSong;

    @Override
    public void onCreate() {
        super.onCreate();
        songPos = 0;
        //rand = new Random();
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
        nowPlayingSongs = new ArrayList<>();
        nowPlayingSongs.addAll(songs);
        this.adapter = adapter;
        this.musicControls = musicControls;
        oldSong = 0;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void playSong(boolean fromUserClick) {
        //for shuffle purposes. if the user picked next song or if program did
        ArrayList<Song> tempList = new ArrayList<>();

        if (fromUserClick)
            tempList = songs;
        else {
            tempList = nowPlayingSongs;
        }
        player.reset();
        Song playSong = tempList.get(songPos);
        songTitle = playSong.getTitle();
        songArtist = playSong.getArtist();
        long currSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(MainActivity.STORAGE_LOCATION, currSong);

        try {
            Log.d(MainActivity.DEBUG_TAG, playSong.toString());
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepareAsync();

            //getting rid of old color
            songs.get(tempList.get(songPos).getVisibleSongPos()).setHasColor(false);
            adapter.notifyItemChanged(oldSong);
            Log.d(MainActivity.DEBUG_TAG, String.format("OldSongPos: %d", oldSong));

            //adding new color
            songs.get(tempList.get(songPos).getVisibleSongPos()).setHasColor(true);
            adapter.notifyItemChanged(songPos);

            Log.d(MainActivity.DEBUG_TAG, String.format("NewSongPos: %d", songPos));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), String.format("Couldn't play song %s", songTitle), Toast.LENGTH_LONG).show();
            //Todo - Doesn't show the right information on exception
        }
    }

    public void playPrev() {
        songPos--;
        if (songPos < 0) {
            songPos = nowPlayingSongs.size() - 1;
        }
        playSong(false);
    }

    public void playNext(boolean fromUser) {
        playAfterPrepare = true;
        /*
        if (isShuffle) {
            int newSong = songPos;
            while (newSong == songPos) {
                newSong = rand.nextInt(nowPlayingSongs.size() - 1);
            }
            songPos = newSong;
        } else {
        */
        songPos++;
        if (songPos > nowPlayingSongs.size() - 1) {
            if (isRepeat || fromUser) {
                songPos = 0;
            }
            else {
                songPos--;
                musicControls.updatePlayButton(true);
                playAfterPrepare = false;
            }
        }

        playSong(false);
    }

    public void setShuffle() {
        isShuffle = !isShuffle;
        musicControls.updateShuffleButton(isShuffle);
        if (isShuffle) {
            shuffleFisherYates();
        }
        else {
            nowPlayingSongs.clear();
            nowPlayingSongs.addAll(songs);
        }
    }

    public void setRepeat() {
        isRepeat = !isRepeat;
        musicControls.updateRepeatButton(isRepeat);
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
        seekBarTimer();
    }

    public void seekBarTimer() {
        runnable = new Runnable() {

            @Override
            public void run() {
                if (player.isPlaying()) {
                    musicControls.setSongPos(getPos());
                    seekHandler.postDelayed(this, SEEKBAR_TIME);
                }
                else
                    seekHandler.removeCallbacks(this);
            }
        };
        seekHandler.postDelayed(runnable, SEEKBAR_TIME);
    }

    //hopefully shuffles the songs
    public void shuffleFisherYates() {
        Random random = new Random();
        for (int i = 0; i < nowPlayingSongs.size(); i++) {
            int j = random.nextInt((nowPlayingSongs.size() - 1) - i + 1) + i;
            Song temp = nowPlayingSongs.get(i);
            nowPlayingSongs.set(i, nowPlayingSongs.get(j));
            nowPlayingSongs.set(j, temp);
        }
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
                playNext(false);
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

        if (playAfterPrepare) {
            musicControls.updatePlayButton(false);
            musicControls.setSongInfo(songTitle, songArtist);
            musicControls.setSongMax(getDur());
            musicControls.setSongPos(getPos());

            mp.start();
            //mp.setOnCompletionListener(new onCompletionListener());
            seekBarTimer();

            NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notIntent = new Intent(this, MainActivity.class);
            notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        /*
        Intent notPauseIntent = new Intent();
        notPauseIntent.setAction("PAUSE");
        PendingIntent pendIntPause = PendingIntent.getActivity(this, 0, notPauseIntent, 0);
        NotificationCompat.Action pauseAction = new NotificationCompat.Action.Builder(MusicControls.PLAY_RESOURCE, "Pause", pendIntPause).build();
        */

            builder.setContentIntent(pendInt)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(songTitle)
                    .setContentTitle("Playing")
                    .setContentText(songTitle);
            Notification notif = builder.build();

            startForeground(NOTIFY_ID, notif);

        }
        else {
            playAfterPrepare = true;
            musicControls.setSongMax(getDur());
            musicControls.setSongPos(getPos());
        }
    }
}
