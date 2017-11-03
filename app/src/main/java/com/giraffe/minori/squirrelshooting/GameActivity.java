package com.giraffe.minori.squirrelshooting;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import static com.giraffe.minori.squirrelshooting.SurfaceCreate.Moving;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.isFinished;
import static com.giraffe.minori.squirrelshooting.MainActivity.noGameActivity;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.mIsAttached;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.mThread;
import static com.giraffe.minori.squirrelshooting.MainActivity.mediaPlayer2;
import static java.lang.Math.abs;


public class GameActivity extends AppCompatActivity {
    private SurfaceCreate mSurfaceView;
    //MediaPlayer mediaPlayer2;

    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private GestureDetector mGestureDetector;
    public static float distance_y;
    public static float velocity_x;
    public static float velocity_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Game","OnCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("Game","OnStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        mSurfaceView = new SurfaceCreate(this);
        setContentView(mSurfaceView);
        mGestureDetector = new GestureDetector(this, mOnGestureListener);
        //mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit11);
        //mediaPlayer2.start();
        Log.e("Game","OnResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        //mediaPlayer2.pause();
        noGameActivity = true;
        Log.e("Game","OnPause");
    }

    protected void onStop(){
        super.onStop();
        Log.e("Game","OnStop");
        //finish();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        //mediaPlayer2.release();
        Log.e("Game","onDestroy");
    }

    protected void onRestart(){
        super.onRestart();
        Log.e("Game","OnRestart");
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public  boolean onTouchEvent(MotionEvent event){
        if (isFinished == true) {

            isFinished = false;
            //finish();
            Intent intent = new Intent(GameActivity.this, Result.class);
            startActivity(intent);
        }
        //return super.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    public GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
                if (Moving == false) {
                    // 移動距離・スピードを出力
                    //distance_y = Math.abs((event1.getY() - event2.getY()));
                    if(velocityX <= -9000){
                        velocityX = -9000;
                    }
                    else if(velocityX >= 9000){
                        velocityX = 9000;
                    }
                    if(velocityY <= -9000){
                        velocityY = -9000;
                    }
                    if(velocityY >= -2000){
                        if(abs(velocityX) <= 2000){
                            velocityX = 0.0f;
                            velocityY = 0.0f;
                        }
                    }
                    velocity_x = -velocityX;
                    velocity_y = -velocityY;
                }
            return false;
        }
    };
}
