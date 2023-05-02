package com.example.musicplayer.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.Util;
import com.example.musicplayer.R;
import com.example.musicplayer.Model.Song;
import com.example.musicplayer.SongBaseAdapter;
import com.google.android.material.slider.Slider;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment {

    Song song;
    List<Song> track;
    int currentposition;
    private MediaPlayer musicPlayer;
    TextView currentTime, lengthTime, mpTitle, mpArtist, titleMini;
    SeekBar  seekbarVolume;
    Slider seekBarTime;
    ImageButton playBtn, forwardBtn, backwardBtn, shuffleBtn, repeatBtn, playMini;
    ImageView imageView;
    ListView nextListView;
    SongBaseAdapter songsAdapter;
    private Boolean loop = false;
    private Boolean shuffle = false;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    public static MusicPlayerFragment newInstance() {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Thread.interrupted();
                if (musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Intent intent = getActivity().getIntent();
        Bundle mbundle = this.getArguments();
        if(mbundle != null)
        {
            track = mbundle.getParcelableArrayList("tracks");
            currentposition = mbundle.getInt("position",-1);
        }
        nextListView = getView().findViewById(R.id.nextListView);
        //track = intent.getParcelableArrayListExtra("tracks");
        //currentposition = intent.getExtras().getInt("position",-1);
        // Declarations
        seekBarTime = getView().findViewById(R.id.timer_Seeker);
        seekbarVolume = getView().findViewById(R.id.volume_Seeker);
        currentTime = getView().findViewById(R.id.currentTime);
        lengthTime = getView().findViewById(R.id.fullTime);

        playBtn = getView().findViewById(R.id.playBtn);
        playMini = getView().findViewById(R.id.playMini);
        forwardBtn = getView().findViewById(R.id.forwardBtn);
        backwardBtn = getView().findViewById(R.id.backBtn);
        shuffleBtn = getView().findViewById(R.id.shuffleBtn);
        repeatBtn = getView().findViewById(R.id.repeatBtn);
        imageView = getView().findViewById(R.id.frag_cover);

        mpTitle = getView().findViewById(R.id.titletext);
        titleMini = getView().findViewById(R.id.titleMini);
        mpArtist = getView().findViewById(R.id.artistText);
        Log.d("test",track.get(currentposition).getTitle());
        mpTitle.setText(track.get(currentposition).getTitle());
        mpTitle.setSelected(true);

        titleMini.setText(track.get(currentposition).getTitle());
        titleMini.setSelected(true);

        mpArtist.setText(track.get(currentposition).getArtist());

        musicPlayer = MediaPlayer.create(getContext(), Uri.parse(track.get(currentposition).getPath()));
        musicPlayer.setLooping(false);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);
        try {
            setArt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String currentDuration = Util.converter(musicPlayer.getDuration());
        lengthTime.setText(currentDuration);


        songsAdapter = new SongBaseAdapter(getContext(), track);
        nextListView.setAdapter(songsAdapter);
        nextListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        nextListView.setSelection(currentposition);
        nextListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = track.get(position);
                currentposition = position;
                musicPlayer.stop();
                mpTitle.setText(track.get(position).getTitle());
                titleMini.setText(track.get(position).getTitle());
                mpArtist.setText(track.get(position).getArtist());
                musicPlayer = MediaPlayer.create(getContext(), Uri.parse(track.get(position).getPath()));
                musicPlayer.seekTo(0);
                seekBarTime.setValue(0);
                lengthTime.setText(Util.converter(musicPlayer.getDuration()));
                seekBarTime.setValueTo(musicPlayer.getDuration());
                //imageView.setImageDrawable();
                playMusic();
                nextListView.setSelection(currentposition);
                nextListView.smoothScrollToPosition(currentposition);
                playBtn.setImageResource(R.drawable.ic_pause);
                playMini.setImageResource(R.drawable.ic_pause);
                //pauseBtn.setVisibility(View.VISIBLE);
                //Toast.makeText(getApplicationContext(), "You Clicked: "+song.getTitle(), Toast.LENGTH_LONG).show();
                try {
                    setArt();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        playMusic();



        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (!musicPlayer.isPlaying()) {
                    musicPlayer.start();
                    playBtn.setVisibility(View.INVISIBLE);
                    pauseBtn.setVisibility(View.VISIBLE);
                }*/
                isPlaying(musicPlayer.isPlaying());
            }
        });

        playMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isPlaying(musicPlayer.isPlaying());
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(musicPlayer.isPlaying())
                //musicPlayer.seekTo(musicPlayer.getCurrentPosition() + 10000);

                musicPlayer.stop();
                //musicPlayer.release();
                currentposition = currentposition + 1;
                mpTitle.setText(track.get(currentposition).getTitle());
                titleMini.setText(track.get(currentposition).getTitle());
                mpArtist.setText(track.get(currentposition).getArtist());
                musicPlayer = MediaPlayer.create(getContext(), Uri.parse(track.get(currentposition).getPath()));
                musicPlayer.seekTo(0);
                seekBarTime.setValue(0);
                lengthTime.setText(Util.converter(musicPlayer.getDuration()));
                seekBarTime.setValueTo(musicPlayer.getDuration());
                playMusic();
                try {
                    setArt();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(musicPlayer.isPlaying())
                //musicPlayer.seekTo(musicPlayer.getCurrentPosition() - 10000);

                musicPlayer.stop();
                //musicPlayer.release();
                if(currentposition == 0)
                    currentposition = track.size();
                currentposition = currentposition - 1;
                mpTitle.setText(track.get(currentposition).getTitle());
                titleMini.setText(track.get(currentposition).getTitle());
                mpArtist.setText(track.get(currentposition).getArtist());
                musicPlayer = MediaPlayer.create(getContext(), Uri.parse(track.get(currentposition).getPath()));
                musicPlayer.seekTo(0);
                seekBarTime.setValue(0);
                lengthTime.setText(Util.converter(musicPlayer.getDuration()));
                seekBarTime.setValueTo(musicPlayer.getDuration());
                playMusic();
                try {
                    setArt();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loop == false){
                    loop = true;
                    shuffle = false;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24);
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24_grey);
                    musicPlayer.setLooping(true);
                }
                else if(loop ==true){
                    loop = false;
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24_grey);
                    musicPlayer.setLooping(false);
                }

            }
        });

        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle == false){
                    shuffle = true;
                    loop = false;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24);
                    repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24_grey);
                    musicPlayer.setLooping(false);
                }
                else if(shuffle ==true){
                    shuffle = false;
                    shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24_grey);

                }

            }
        });


        // default to 100%
        seekbarVolume.setProgress(100);
        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Float volume = progress / 100f;
                musicPlayer.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarTime.setValueTo(musicPlayer.getDuration());
        /*.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                    currentTime.setText(Common.converter(musicPlayer.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
        seekBarTime.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {



            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                musicPlayer.seekTo((int) slider.getValue());
                seekBarTime.setValue(slider.getValue());
                currentTime.setText(Util.converter(musicPlayer.getCurrentPosition()));
            }


            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (musicPlayer != null) {
                    if (musicPlayer.isPlaying()) {
                        //playMusic();
                        try {
                            final double current = musicPlayer.getCurrentPosition();
                            String elapsedTime = Util.converter(current);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    currentTime.setText(elapsedTime);
                                    seekBarTime.setValue((int) current);
                                }
                            });

                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            }
        }).start();
    }

    public void playMusic(){
        musicPlayer.start();
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if(shuffle) {
                    musicPlayer.stop();
                    //musicPlayer.release();
                    int randomPos = (int) (Math.random() * track.size() + 1);
                    currentposition = randomPos;
                    mpTitle.setText(track.get(randomPos).getTitle());
                    titleMini.setText(track.get(randomPos).getTitle());
                    mpArtist.setText(track.get(randomPos).getArtist());
                    musicPlayer = MediaPlayer.create(getContext(), Uri.parse(track.get(randomPos).getPath()));
                    musicPlayer.seekTo(0);
                    seekBarTime.setValue(0);
                    lengthTime.setText(Util.converter(musicPlayer.getDuration()));
                    seekBarTime.setValueTo(musicPlayer.getDuration());
                    try {
                        setArt();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    playMusic();
                }else{
                    musicPlayer.pause();
                    musicPlayer.seekTo(0);
                    seekBarTime.setValue(0);
                    //pauseBtn.setVisibility(View.INVISIBLE);
                    playBtn.setImageResource(R.drawable.ic_play);
                    playMini.setImageResource(R.drawable.ic_play);
                }
            }
        });
    }

    /* @Override
    protected void onDestroy() {
        Thread.interrupted();
        super.onDestroy();
    }

   @Override
    public void onBackPressed() {
        Thread.interrupted();
        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
        super.onBackPressed();
    }*/

    public void setArt() throws IOException {
        byte[] art = Util.getAlbumArt(track.get(currentposition).getPath());
        if(art !=null)
            Glide.with(this).asBitmap().load(art).into(imageView);
        else
            Glide.with(this).asBitmap().load(R.drawable.music_logo).into(imageView);
    }
    public void isPlaying(boolean playing){
        if(!playing){
            musicPlayer.start();
            playBtn.setImageResource(R.drawable.ic_pause);
            playMini.setImageResource(R.drawable.ic_pause);
        }else if(playing) {
            musicPlayer.pause();
            playBtn.setImageResource(R.drawable.ic_play);
            playMini.setImageResource(R.drawable.ic_play);
        }
    }


}