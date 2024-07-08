package com.example.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView imagePlayPause;
    private SeekBar playerSeekBar;
    private TextView textCurrentTime, textTotalDuration;
    private MediaPlayer mediaPlayer;
    private final Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagePlayPause=findViewById(R.id.imagePlayPause);
        textTotalDuration=findViewById(R.id.textTotalDuration);
        textCurrentTime=findViewById(R.id.textCurrentTime);
        playerSeekBar=findViewById(R.id.playerSeekBar);
        mediaPlayer=new MediaPlayer();

        playerSeekBar.setMax(100);

        imagePlayPause.setOnClickListener(view -> {
            if(mediaPlayer.isPlaying()){
                handler.removeCallbacks(updater);
                mediaPlayer.pause();
                imagePlayPause.setImageResource(R.drawable.baseline_play);
            }else {
                mediaPlayer.start();
                imagePlayPause.setImageResource(R.drawable.baseline_pause);
                updateSeekBar();
            }
        });
        prepareMediaPlayer();
    }

private void prepareMediaPlayer(){
        try {
            mediaPlayer.setDataSource("https://pagallworld.co.in/cheri-cheri-lady/");//URL of Audio
            mediaPlayer.prepare();
            textTotalDuration.setText(milliSecondsToTimer(mediaPlayer.getDuration()));
        }catch (Exception exception){
            Toast.makeText(this,exception.getMessage(),Toast.LENGTH_SHORT).show();
        }
}


    private final Runnable updater= new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration=mediaPlayer.getCurrentPosition();
            textCurrentTime.setText(milliSecondsToTimer(currentDuration));
        }
    };

    private void updateSeekBar(){
        if(mediaPlayer.isPlaying()){
            playerSeekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }

    private String milliSecondsToTimer(long milliSeconds){
        String timerString="";
        String secondsString;

        int hours=(int)(milliSeconds/(1000*60*60));
        int minutes=(int)(milliSeconds%(1000*60*60))/(1000*60);
        int seconds=(int)((milliSeconds%(1000*60*60))%(1000*60)/1000);

        if(hours>0) {
            timerString = hours + ":";
        }
        if(seconds<10){
            secondsString="0"+seconds;
        }else{
            secondsString=""+seconds;
        }
        timerString=timerString+minutes+":"+secondsString;
        return timerString;
    }
}