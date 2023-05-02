package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class StartActivity extends AppCompatActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //spinner is loading bar at loading screen
        spinner=(ProgressBar)findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(StartActivity.this, ListMusicActivity.class);
                StartActivity.this.startActivity(myIntent);
                finish();
            }
        },1000);


    }
}