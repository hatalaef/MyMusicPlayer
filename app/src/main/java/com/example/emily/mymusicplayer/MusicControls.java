package com.example.emily.mymusicplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MusicControls extends Fragment {

    public static final int PLAY_RESOURCE = R.drawable.ic_play_arrow_24dp;
    public static final int PAUSE_RESOURCE = R.drawable.ic_pause_24dp;
    public static final int REW_FWD_HANDLER_TIME = 100;
    public static final int REW_MILLES = 250;

    private ImageView imgPrev;
    private ImageView imgRew;
    private ImageView imgPlay;
    private ImageView imgFor;
    private ImageView imgNext;
    private SeekBar seekBar;
    private TextView navSongName;
    private TextView navSongArtist;
    private TextView navSongPos;
    private TextView navSongDur;
    private Handler handler = new Handler();
    private Runnable rewRunnable;
    private Runnable forRunnable;

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
        navSongPos = (TextView)view.findViewById(R.id.navSongPos);
        navSongDur = (TextView)view.findViewById(R.id.navSongDur);

        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPrevClicked();

            }
        });

        imgRew.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    revRunnable();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (rewRunnable != null) {
                        handler.removeCallbacks(rewRunnable);
                    Log.d(MainActivity.DEBUG_TAG, "Rewind handler stopped");
                }
                }

                return true;
            }

        });
        imgFor.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    forRunnable();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (forRunnable != null) {
                        handler.removeCallbacks(forRunnable);
                        Log.d(MainActivity.DEBUG_TAG, "Forward handler stopped");

                    }
                }

                return true;
            }

        });




        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayClicked();
            }
        });


        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onNextClicked();

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mListener.onSeekBarChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return view;
    }

    //for rewind/forward, advances music every so often
    public void revRunnable() {

        rewRunnable = new Runnable() {

            @Override
            public void run() {
                mListener.onRewindClicked();
                handler.postDelayed(this, REW_FWD_HANDLER_TIME);
            }
        };
        handler.postDelayed(rewRunnable, REW_FWD_HANDLER_TIME);
    }

    public void forRunnable() {

        forRunnable = new Runnable() {

            @Override
            public void run() {
                mListener.onForwardClicked();
                handler.postDelayed(this, REW_FWD_HANDLER_TIME);
            }
        };
        handler.postDelayed(forRunnable, REW_FWD_HANDLER_TIME);
    }

    public void updatePlayButton(boolean changeToPause) {
        if (changeToPause) {
            imgPlay.setImageDrawable(getResources().getDrawable(PLAY_RESOURCE, null));
        }
        else {
            imgPlay.setImageDrawable(getResources().getDrawable(PAUSE_RESOURCE, null));
        }
    }

    public void setSongInfo(String title, String artist) {
        navSongName.setText(title);
        navSongArtist.setText(artist);
    }

    public void setSongMax(int max) {
        seekBar.setMax(max);

        String theTime;
        int second = (max / 1000) % 60;
        int minute = (max / (1000 * 60)) % 60;
        int hour = (max / (1000 * 60 * 60)) % 60;

        if (hour > 0) {
            theTime = String.format("%d:%02d:%02d", hour, minute, second);
        }
        else {
            theTime = String.format("%d:%02d", minute, second);
        }
        navSongDur.setText(String.format(theTime));
    }

    public void setSongPos(int pos) {
        seekBar.setProgress(pos);

        String theTime;
        int second = (pos / 1000) % 60;
        int minute = (pos / (1000 * 60)) % 60;
        int hour = (pos / (1000 * 60 * 60)) % 60;

        if (hour > 0) {
            theTime = String.format("%d:%02d:%02d", hour, minute, second);
        }
        else {
            theTime = String.format("%d:%02d", minute, second);
        }

        navSongPos.setText(String.format(theTime));
    }




    public interface OnFragmentInteractionListener {
        void onPlayClicked();
        void onNextClicked();
        void onPrevClicked();
        void onForwardClicked();
        void onRewindClicked();
        void onSeekBarChanged(int progress);
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
