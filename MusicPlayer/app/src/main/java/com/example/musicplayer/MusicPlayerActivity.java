package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.Model.Song;
import com.example.musicplayer.Model.Station;
import com.example.musicplayer.Services.OnClearFromRecentService;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.io.IOException;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity implements Playable{  //

    Song song;
    List<Song> track;
    List<Station> stations;
    int currentPosition;
    private MediaPlayer musicPlayer;
    TextView currentTime, lengthTime, mpTitle, mpArtist, titleMini;
    SeekBar  seekbarVolume;
    Slider seekBarTime;
    ImageButton playBtn, forwardBtn, backwardBtn, shuffleBtn, repeatBtn, playMini;
    ImageView imageView;
    ListView nextListView;
    NextBaseAdapter songsAdapter;
    private Boolean loop = false;
    private Boolean shuffle = false;
    private Boolean prepared = false;
    private Boolean started = false;
    private String mode;

    NotificationManager notificationManager;
    boolean isPlayable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        //getSupportActionBar().hide();
        Intent intent = getIntent();
        nextListView = findViewById(R.id.nextListView);
        //String title = intent.getExtras().getString("title");
        //String artist = intent.getExtras().getString("artist");
        //String path = intent.getExtras().getString("path");
        //song = new Song(title, artist, path);
        track = intent.getParcelableArrayListExtra("tracks");
        stations = intent.getParcelableArrayListExtra("stations");
        mode = intent.getExtras().getString("mode");
        //if(mode == "Music")
        currentPosition = intent.getExtras().getInt("position",-1);
        // Declarations
        seekBarTime = findViewById(R.id.timer_Seeker);
        seekbarVolume = findViewById(R.id.volume_Seeker);
        currentTime = findViewById(R.id.currentTime);
        lengthTime = findViewById(R.id.fullTime);

        playBtn = findViewById(R.id.playBtn);
        playMini = findViewById(R.id.playMini);
        forwardBtn = findViewById(R.id.forwardBtn);
        backwardBtn = findViewById(R.id.backBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        repeatBtn = findViewById(R.id.repeatBtn);
        imageView = findViewById(R.id.frag_cover);

        mpTitle = findViewById(R.id.titleText);
        titleMini = findViewById(R.id.titleMini);
        mpArtist = findViewById(R.id.artistText);


        if(mode.equals("Music")) {
            mpTitle.setText(track.get(currentPosition).getTitle());
            titleMini.setText(track.get(currentPosition).getTitle());
            mpArtist.setText(track.get(currentPosition).getArtist());
            musicPlayer = MediaPlayer.create(this, Uri.parse(track.get(currentPosition).getPath()));
            musicPlayer.setLooping(false);
            musicPlayer.seekTo(0);
            mpTitle.setSelected(true);
            titleMini.setSelected(true);
            musicPlayer.setVolume(1.0f, 1.0f);
            setArt();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                createChannel();
                registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
            }

            String currentDuration = Util.converter(musicPlayer.getDuration());
            lengthTime.setText(currentDuration);

            seekBarTime.setLabelFormatter(new LabelFormatter() {
                @NonNull
                @Override
                public String getFormattedValue(float value) {

                    return Util.converter(value);
                }
            });

            songsAdapter = new NextBaseAdapter(this, track);
            nextListView.setAdapter(songsAdapter);
            nextListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            nextListView.setSelection(currentPosition);

            playMusic();

            seekBarTime.setValueTo(musicPlayer.getDuration());

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
                    //onTrackPlay();
                }
            });

            forwardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(musicPlayer.isPlaying())
                    //musicPlayer.seekTo(musicPlayer.getCurrentPosition() + 10000);
                    onTrackNext();
                }
            });

            backwardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if(musicPlayer.isPlaying())
                    //musicPlayer.seekTo(musicPlayer.getCurrentPosition() - 10000);
                    onTrackPrevious();
                }
            });

            repeatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!loop){
                        loop = true;
                        shuffle = false;
                        repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24);
                        shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24_grey);
                        musicPlayer.setLooping(true);
                    }
                    else if(loop){
                        loop = false;
                        repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24_grey);
                        musicPlayer.setLooping(false);
                    }

                }
            });

            shuffleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!shuffle){
                        shuffle = true;
                        loop = false;
                        shuffleBtn.setImageResource(R.drawable.ic_baseline_shuffle_24);
                        repeatBtn.setImageResource(R.drawable.ic_baseline_repeat_24_grey);
                        musicPlayer.setLooping(false);
                    }
                    else if(shuffle){
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

            seekBarTime.addOnChangeListener(new Slider.OnChangeListener() {
                @Override
                public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                    if (fromUser) {
                        musicPlayer.seekTo((int) slider.getValue());
                        seekBarTime.setValue(slider.getValue());
                        currentTime.setText(Util.converter(musicPlayer.getCurrentPosition()));
                    }
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentTime.setText(elapsedTime);
                                        seekBarTime.setValue(checkTime(current));
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
        else if(mode.equals("Radio")) {
            switch(stations.get(currentPosition).getTitle()){
                case "Z 103.5":
                    imageView.setImageResource(R.drawable.radio_z103_5);
                    break;
                case "Virgin 99.9":
                    imageView.setImageResource(R.drawable.radio_virgin_99_9);
                    break;
                case "KISS 92.5":
                    imageView.setImageResource(R.drawable.radio_kiss_92_5);
                    break;
                case "TODAY 93.5":
                    imageView.setImageResource(R.drawable.radio_today_93_5);
                    break;
                case "CHUM 104.5":
                    imageView.setImageResource(R.drawable.radio_chum_104_5);
                    break;
            }
            mpTitle.setText(stations.get(currentPosition).getTitle());
            titleMini.setText(stations.get(currentPosition).getTitle());
            musicPlayer = new MediaPlayer();
            musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            new PlayTask().execute(stations.get(currentPosition).getPath());
            musicPlayer.start();
            mpTitle.setSelected(true);
            titleMini.setSelected(true);
            musicPlayer.setVolume(1.0f, 1.0f);
            seekBarTime.setVisibility(View.GONE);
            lengthTime.setVisibility(View.GONE);
            mpArtist.setVisibility(View.GONE);
            currentTime.setVisibility(View.GONE);
            forwardBtn.setVisibility(View.GONE);
            backwardBtn.setVisibility(View.GONE);
            shuffleBtn.setVisibility(View.GONE);
            repeatBtn.setVisibility(View.GONE);


        }



       /* seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


    }

    public void playMusic(){
        musicPlayer.start();
        CreateNotification.createNotification(this,track.get(currentPosition), R.drawable.ic_pause,1,track.size() -1);
        isPlayable = musicPlayer.isPlaying();
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                if(shuffle) {
                    musicPlayer.stop();
                    //musicPlayer.release();
                    int randomPos = (int) (Math.random() * track.size() + 1);
                    currentPosition = randomPos;
                    mpTitle.setText(track.get(randomPos).getTitle());
                    titleMini.setText(track.get(randomPos).getTitle());
                    mpArtist.setText(track.get(randomPos).getArtist());
                    musicPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(track.get(randomPos).getPath()));
                    musicPlayer.seekTo(0);
                    seekBarTime.setValue(0);
                    seekBarTime.setValueTo(musicPlayer.getDuration());
                    lengthTime.setText(Util.converter(musicPlayer.getDuration()));
                    setArt();
                    playMusic();
                }else{
                    musicPlayer.pause();
                    isPlayable = musicPlayer.isPlaying();
                    seekBarTime.setValueFrom(0);
                    seekBarTime.setValueTo(musicPlayer.getDuration());
                    musicPlayer.seekTo(0);
                    seekBarTime.setValue(0);
                    currentTime.setText("0.00");
                    //pauseBtn.setVisibility(View.INVISIBLE);
                    playBtn.setImageResource(R.drawable.ic_play);
                    playMini.setImageResource(R.drawable.ic_play);
                }
            }
        });
    }


    private class PlayTask extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                musicPlayer.setDataSource(strings[0]);
                musicPlayer.prepareAsync();
                prepared = true;
            }catch (IOException e){
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            playBtn.setEnabled(true);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            musicPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicPlayer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Thread.interrupted();
        if(mode.equals("Music")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.cancelAll();
            }
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        Thread.interrupted();
        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
        super.onBackPressed();
    }

    public void setArt(){
        byte[] art = new byte[0];
        try {
            art = Util.getAlbumArt(track.get(currentPosition).getPath());
            Glide.with(this).asBitmap().load(art).into(imageView);
        } catch (IOException e) {
            Glide.with(this).asBitmap().load(R.drawable.music_logo).into(imageView);
        }
        //if(art !=null)

        //else

    }



    public int checkTime(double current){
        if (current > musicPlayer.getDuration())
            return musicPlayer.getDuration();
        else
            return (int) current;
    }

    public void nextSongClicked(int i){
        Log.d("Listview", "Clicked");
        Song song = track.get(i);
        currentPosition = i;
        musicPlayer.stop();
        mpTitle.setText(track.get(i).getTitle());
        titleMini.setText(track.get(i).getTitle());
        mpArtist.setText(track.get(i).getArtist());
        musicPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(track.get(i).getPath()));
        musicPlayer.seekTo(0);
        seekBarTime.setValueFrom(0);
        seekBarTime.setValueTo(musicPlayer.getDuration());
        seekBarTime.setValue(0);
        lengthTime.setText(Util.converter(musicPlayer.getDuration()));
        //imageView.setImageDrawable();
        playMusic();
        nextListView.setSelection(currentPosition);
        nextListView.smoothScrollToPosition(currentPosition);
        playBtn.setImageResource(R.drawable.ic_pause);
        playMini.setImageResource(R.drawable.ic_pause);
        //pauseBtn.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), "You Clicked: "+song.getTitle(), Toast.LENGTH_LONG).show();
        setArt();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "Music Player", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.ACTION_PREVIOUS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if(isPlayable)
                        onTrackPause();
                    else
                        onTrackPlay();
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    public void isPlaying(boolean playing){
        if(playing){
            onTrackPause();
        }else if(!playing) {
            onTrackPlay();
        }
    }

    @Override
    public void onTrackPlay() {
        musicPlayer.start();
        if(mode.equals("Music")) {
            CreateNotification.createNotification(MusicPlayerActivity.this, track.get(currentPosition),
                    R.drawable.ic_pause, currentPosition, track.size() - 1);
        }
        else if(mode.equals("Radio")){
            //new PlayTask().execute(stations.get(currentPosition).getPath());
            //musicPlayer.prepareAsync();

            musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    musicPlayer.start();
                }
            });
        }
        playBtn.setImageResource(R.drawable.ic_pause);
        playMini.setImageResource(R.drawable.ic_pause);
        isPlayable = true;
    }

    @Override
    public void onTrackPause() {
        musicPlayer.pause();
        if(mode.equals("Music")) {
            CreateNotification.createNotification(MusicPlayerActivity.this, track.get(currentPosition),
                R.drawable.ic_play,currentPosition, track.size() -1);
        }
        playBtn.setImageResource(R.drawable.ic_play);
        playMini.setImageResource(R.drawable.ic_play);
        isPlayable = false;
    }

    @Override
    public void onTrackPrevious() {
        musicPlayer.stop();
        //musicPlayer.release();
        if(currentPosition == 0)
            currentPosition = track.size() -1;
        else
            currentPosition = currentPosition - 1;
        mpTitle.setText(track.get(currentPosition).getTitle());
        titleMini.setText(track.get(currentPosition).getTitle());
        mpArtist.setText(track.get(currentPosition).getArtist());
        musicPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(track.get(currentPosition).getPath()));
        musicPlayer.seekTo(0);
        seekBarTime.setValue(0);
        lengthTime.setText(Util.converter(musicPlayer.getDuration()));
        seekBarTime.setValueTo(musicPlayer.getDuration());
        playMusic();
        setArt();
        CreateNotification.createNotification(MusicPlayerActivity.this, track.get(currentPosition),
                R.drawable.ic_pause,currentPosition, track.size() -1);
    }


    @Override
    public void onTrackNext() {
        musicPlayer.stop();
        //musicPlayer.release();
        int maxLength = track.size() -1;
        if(currentPosition == maxLength)
            currentPosition = 0;
        else
            currentPosition = currentPosition + 1;
        mpTitle.setText(track.get(currentPosition).getTitle());
        titleMini.setText(track.get(currentPosition).getTitle());
        mpArtist.setText(track.get(currentPosition).getArtist());
        musicPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(track.get(currentPosition).getPath()));
        musicPlayer.seekTo(0);
        seekBarTime.setValue(0);
        lengthTime.setText(Util.converter(musicPlayer.getDuration()));
        seekBarTime.setValueTo(musicPlayer.getDuration());
        playMusic();
        setArt();
        CreateNotification.createNotification(MusicPlayerActivity.this, track.get(currentPosition),
                R.drawable.ic_pause,currentPosition, track.size() -1);
    }


}

