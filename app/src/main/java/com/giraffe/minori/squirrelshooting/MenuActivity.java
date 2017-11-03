package com.giraffe.minori.squirrelshooting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import static com.giraffe.minori.squirrelshooting.MainActivity.noGameActivity;

public class MenuActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer2;
    //MediaPlayer mediaPlayer3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Log.e("Menu","OnCreate");
        noGameActivity = true;
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit13);
        //mediaPlayer3 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit11);

        Button gamestart = (Button) this.findViewById(R.id.gamestart);
        gamestart.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.e("Menu","You touched mainactivity!");
                if(noGameActivity==true) {
                    noGameActivity = false;
                    mediaPlayer2.pause();
                    //isGameBGMeEnded = false;
                    //mediaPlayer3.start();
                    Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                    startActivity(intent);
                    mediaPlayer2.stop();
                    mediaPlayer2.reset();
                    mediaPlayer2.release();
                }
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
        mediaPlayer2.release();
        //mediaPlayer3.stop();
        //mediaPlayer3.reset();
        //mediaPlayer3.release();
        Log.e("Menu","onDestroy");
    }

    @Override
    public void onBackPressed() {
    }

    //@Override
    //public boolean onTouchEvent(MotionEvent event){
    //    Log.e("Main","You touched mainactivity!");
    //    if(noGameActivity==true) {
    //        noGameActivity = false;
    //        mediaPlayer2.pause();
    //        //isGameBGMeEnded = false;
    //        mediaPlayer3.start();
    //        Intent intent = new Intent(MenuActivity.this, GameActivity.class);
    //        startActivity(intent);
    //    }
    //    return super.onTouchEvent(event);
    //}
}

