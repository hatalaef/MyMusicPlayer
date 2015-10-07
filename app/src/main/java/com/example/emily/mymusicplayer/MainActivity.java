package com.example.emily.mymusicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.emily.mymusicplayer.MusicService.MusicBinder;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements MusicControls.OnFragmentInteractionListener {

    public static final Uri STORAGE_LOCATION = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final String FOLDER_PATH = "MyMusic";
    public static final String PLAYLIST_PATH = "MyMusic/Playlists";
    public static final String PLAYLIST_TYPE = "m3u";
    public static final String DEBUG_TAG = "MyMusicPlayerDebug";

    private MusicControls musicControls;

    private RecyclerView mainListMusic;
    private SongAdapter songAdapter;
    private PlaylistAdapter playlistAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Song> songList;
    private ArrayList<Playlist> playlistList;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean paused = false;
    private boolean playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicControls = (MusicControls) getSupportFragmentManager().findFragmentById(R.id.controlsFragment);

        mainListMusic = (RecyclerView) findViewById(R.id.mainListMusic);

        mainListMusic.hasFixedSize();

        layoutManager = new LinearLayoutManager(this);
        mainListMusic.setLayoutManager(layoutManager);

        songList = new ArrayList<>();
        getSongList();

        songAdapter = new SongAdapter(this, songList);
        mainListMusic.setAdapter(songAdapter);

        playlistList = new ArrayList<>();
        getPlaylistList();

        playlistAdapter = new PlaylistAdapter(this, playlistList);
        mainListMusic.setAdapter(playlistAdapter);


        //CreatePlaylist.makePlaylist(songList, PLAYLIST_PATH, "testPlaylist.m3u", getApplicationContext());

        songAdapter.setOnItemClickListener(new SongAdapter.ClickListener() {
            //changes color
            @Override
            public void onItemClick(int position, View v) {
                songPicked(position);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                songPicked(position);
            }
        });

        playlistAdapter.setOnItemClickListener(new PlaylistAdapter.ClickListener() {
            //changes color
            @Override
            public void onItemClick(int position, View v) {
            }

            @Override
            public void onItemLongClick(int position, View v) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {

            paused = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songList, songAdapter, musicControls);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
        }

        return super.onOptionsItemSelected(item);
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = STORAGE_LOCATION;
        String folderPath = "%" + FOLDER_PATH + "%";
        //gets music from certain folder
        Cursor musicCursor = musicResolver.query(musicUri, null, MediaStore.Audio.Media.DATA + " like ? ",
                new String[]{folderPath}, null);

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
            int i = 0;
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                int theDuration = musicCursor.getInt(durationColumn);
                String thisPath = musicCursor.getString(dataColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist, theDuration, thisPath, i));
                i++;
            }
            while (musicCursor.moveToNext());
        }
        if (musicCursor != null) {
            musicCursor.close();
        }
    }

    public void getPlaylistList() {
        String folderPath = PLAYLIST_PATH;
        //gets music from certain folder

        File directory = new File(Environment.getExternalStorageDirectory(), folderPath);
        if (!directory.mkdirs()) {
            Log.d(MainActivity.DEBUG_TAG, "Directory not created");
        }
        File[] files = directory.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                String thisPath = files[i].getAbsolutePath();
                //check if m3u
                if (thisPath.split("\\.").length > 1 && thisPath.split("\\.")[1].equalsIgnoreCase(PLAYLIST_TYPE)) {
                    String thisTitle = files[i].getName();
                    int thisDuration = 0;
                    int thisCount = 0;
                    playlistList.add(new Playlist(thisTitle, thisDuration, thisCount, thisPath, i));
                }
            }
        }
        else
            Log.d(DEBUG_TAG, "No playlists found");

    }

    public void songPicked(int i) {
        musicService.setSong(i);
        musicService.playSong(true);
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

    @Override
    public void onPlayClicked() {
        if (!musicService.isPlaying()) {
        //if (playbackPaused) {
            playbackPaused = false;
            musicService.go();
        }
        else {
            playbackPaused = true;
            musicService.pausePlayer();
        }

    }

    @Override
    public void onNextClicked() {
        musicService.playNext(true);
    }

    @Override
    public void onPrevClicked() {
        musicService.playPrev();
    }

    @Override
    public void onForwardClicked() {
        if (musicService.getPos() - MusicControls.REW_MILLES * 3 < musicService.getDur()) {
            musicService.seek(musicService.getPos() + MusicControls.REW_MILLES * 3);
            Log.d(DEBUG_TAG, String.format("ForwardClicked - getPos: %d, timeForward: %d", musicService.getPos(),  MusicControls.REW_MILLES * 3));
        }
        else {
            musicService.seek(musicService.getDur());
            Log.d(DEBUG_TAG, String.format("ForwardClicked - sent to %d", musicService.getDur()));
        }
        musicControls.setSongPos(musicService.getPos());
    }

    @Override
    public void onRewindClicked() {
        if (musicService.getPos() - MusicControls.REW_MILLES * 3 > 0) {
            musicService.seek(musicService.getPos() - MusicControls.REW_MILLES * 3);
            Log.d(DEBUG_TAG, String.format("RewindClicked - getPos: %d, timeRev: %d", musicService.getPos(), MusicControls.REW_MILLES * 3));
        }
        else {
            musicService.seek(0);
            Log.d(DEBUG_TAG, "RewindClicked - sent to 0");
        }
        musicControls.setSongPos(musicService.getPos());
    }

    @Override
    public void onSeekBarChanged(int progress) {
        musicService.seek(progress);
        musicControls.setSongPos(musicService.getPos());
    }

    @Override
    public void onShuffleClicked() {
        musicService.setShuffle();
    }

    @Override
    public void onRepeatClicked() {
        musicService.setRepeat();
    }
}
