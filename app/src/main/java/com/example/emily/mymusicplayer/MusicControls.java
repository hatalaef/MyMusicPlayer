package com.example.emily.mymusicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MusicControls extends Fragment {

    private final int playResource = R.drawable.ic_play_arrow_24dp;
    private final int pauseResource = R.drawable.ic_pause_24dp;

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
                mListener.onPrevClicked();

            }
        });

        imgRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRewindClicked();

            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayClicked();
            }
        });

        imgFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onForwardClicked();

            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNextClicked();

            }
        });

        return view;
    }

    public void updatePlayButton(boolean changeToPause) {
        if (changeToPause) {
            imgPlay.setImageDrawable(getResources().getDrawable(playResource, null));
        }
        else {
            imgPlay.setImageDrawable(getResources().getDrawable(pauseResource, null));
        }
    }

    public void setSongInfo(String title, String artist) {
        navSongName.setText(title);
        navSongArtist.setText(artist);
    }

    public interface OnFragmentInteractionListener {
        void onPlayClicked();
        void onNextClicked();
        void onPrevClicked();
        void onForwardClicked();
        void onRewindClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
