package com.example.emily.mymusicplayer;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MusicControls extends Fragment implements MediaPlayer {

    private ImageView imgPrev;
    private ImageView imgRew;
    private ImageView imgPlay;
    private ImageView imgFor;
    private ImageView imgNext;
    private SeekBar seekBar;
    private TextView navSongName;
    private TextView navSongArtist;

    private OnFragmentInteractionListener mListener;

    public static MusicControls newInstance() {
        MusicControls fragment = new MusicControls();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MusicControls() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music_controls, container, false);

        imgPrev = (ImageView)view.findViewById(R.id.imgPrev);
        imgRew = (ImageView)view.findViewById(R.id.imgRew);
        imgPlay = (ImageView)view.findViewById(R.id.imgPlay);
        imgFor = (ImageView)view.findViewById(R.id.imgFor);
        imgNext = (ImageView)view.findViewById(R.id.imgNext);
        seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        navSongName = (TextView)view.findViewById(R.id.navSongName);
        navSongArtist = (TextView)view.findViewById(R.id.navSongArtist);

        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        imgRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        imgFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return view;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
