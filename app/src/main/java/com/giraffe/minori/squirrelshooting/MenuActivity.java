package com.giraffe.minori.squirrelshooting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.giraffe.minori.squirrelshooting.GameActivity.Gottenstarsum;
import static com.giraffe.minori.squirrelshooting.MainActivity.noGameActivity;

public class MenuActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getSharedPreferences("MyPref", GameActivity.MODE_PRIVATE);
        Gottenstarsum = pref.getInt("StarSum", 0);
        Log.e("Menu_Save",String.valueOf(Gottenstarsum));

        setScreenMenu();

        TextView text = (TextView)findViewById(R.id.starsum);
        text.setText("Your Star : " + String.valueOf(Gottenstarsum));

        Log.e("Menu","OnCreate");
        noGameActivity = true;
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit13);
        mediaPlayer2.setLooping(true);
        mediaPlayer2.seekTo(0);

    }
    private void setScreenMenu(){
        setContentView(R.layout.activity_menu);
        Button gamestart = (Button) this.findViewById(R.id.gamestart);
        gamestart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.e("Menu","You touched mainactivity!");
                if(noGameActivity==true) {
                    noGameActivity = false;
                    mediaPlayer2.pause();
                    Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                    startActivity(intent);
                    //mediaPlayer2.stop();
                    //mediaPlayer2.reset();
                    //mediaPlayer2.release();
                }
            }
        });
        Button credit = (Button) this.findViewById(R.id.credit);
        credit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScreenCredit();
            }
        });
        Button howtoplay = (Button) this.findViewById(R.id.howtoplay);
        howtoplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScreenHowtoplay();
            }
        });
    }
    private void setScreenCredit(){
        setContentView(R.layout.activity_credit);
        Button menu = (Button) this.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScreenMenu();
                TextView text = (TextView)findViewById(R.id.starsum);
                text.setText("Your Star : " + String.valueOf(Gottenstarsum));
            }
        });
    }

    private void setScreenHowtoplay(){
        setContentView(R.layout.activity_howtoplay);
        Button menu = (Button) this.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setScreenMenu();
                TextView text = (TextView)findViewById(R.id.starsum);
                text.setText("Your Star : " + String.valueOf(Gottenstarsum));
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.e("Menu","OnResume");
        mediaPlayer2.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer2.pause();
        Log.e("Menu","onPause");
    }

    @Override
    protected void onStop(){
        mediaPlayer2.pause();
        super.onStop();
        Log.e("Menu","OnStop");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        mediaPlayer2.start();
        Log.e("Menu","onRestart");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mediaPlayer2.stop();
        mediaPlayer2.reset();
        mediaPlayer2.release();
        Log.e("Menu","onDestroy");
    }

    @Override
    public void onBackPressed() {
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}

