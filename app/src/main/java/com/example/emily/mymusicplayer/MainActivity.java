package com.example.emily.mymusicplayer;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.emily.mymusicplayer.MusicService.MusicBinder;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements MusicControls.OnFragmentInteractionListener {

    public static final Uri STORAGE_LOCATION = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final String FOLDER_PATH = "MyMusic";
    public static final String DEBUG_TAG = "MyMusicPlayerDebug";

    private MusicControls musicControls;

    private Handler seekHandler = new Handler();

    private RecyclerView mainListMusic;
    private SongAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Song> songList;
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

        adapter = new SongAdapter(this, songList);
        mainListMusic.setAdapter(adapter);

        adapter.setOnItemClickListener(new SongAdapter.ClickListener() {
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
            musicService.setList(songList, adapter, musicControls);
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
            case R.id.action_repeat:
                break;
            case R.id.action_shuffle:
                musicService.setShuffle();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = STORAGE_LOCATION;
        String folderPath = "%" + FOLDER_PATH + "%";
        //gets music from certain folder
        Cursor musicCursor = musicResolver.query(musicUri, null, MediaStore.Audio.Media.DATA + " like ? ",
                new String[] {folderPath}, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        if (musicCursor != null) {
            musicCursor.close();
        }
    }

    public void songPicked(int i) {
        musicService.setSong(i);
        musicService.playSong();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

    private void setController2() {
    }

    private void playNext() {
        musicService.playNext();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }

    private void playPrev() {
        musicService.playPrev();
        if (playbackPaused) {
            playbackPaused = false;
        }
    }



    @Override
    public void onPlayClicked() {
        if (playbackPaused) {
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
        musicService.playNext();
    }

    @Override
    public void onPrevClicked() {
        musicService.playPrev();
    }

    @Override
    public void onForwardClicked() {
        if (musicService.getPos() + MusicControls.REW_MILLES < musicService.getDur())
            musicService.seek(musicService.getPos() + MusicControls.REW_MILLES);
    }

    @Override
    public void onRewindClicked() {
        if (musicService.getPos() + MusicControls.REW_MILLES < musicService.getDur())
            musicService.seek(musicService.getPos() + MusicControls.REW_MILLES);
    }

    @Override
    public void onSeekBarChanged(int progress) {
        musicService.seek(progress);
    }
}
