package com.example.emily.mymusicplayer;


import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context c) {
        super(c);
    }

    //Keeps the controller on the screen
    @Override
    public void hide() {

    }
}
