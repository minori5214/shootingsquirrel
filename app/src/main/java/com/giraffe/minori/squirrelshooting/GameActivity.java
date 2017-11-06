package com.giraffe.minori.squirrelshooting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import icepick.Icepick;
import icepick.State;

import static android.os.SystemClock.uptimeMillis;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.mThread;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.replay;
import static com.giraffe.minori.squirrelshooting.MainActivity.noGameActivity;
import static java.lang.Math.abs;
import static java.lang.Thread.sleep;


public class GameActivity extends AppCompatActivity{
    private SurfaceCreate mSurfaceView;
    //MediaPlayer mediaPlayer2;

    public static int count;
    public static int portion;

    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private GestureDetector mGestureDetector;
    public static float distance_y;
    public static float velocity_x;
    public static float velocity_y;
    private myThread gameThread;
    boolean mThreadRun;
    private boolean mIsAttached;

    volatile public static Squirrel mSquirrel;
    public static Bitmap mBitmapSquirrel;

    volatile public static Blackhall mBlackhall;
    public static Bitmap mBitmapBlackhall;

    volatile public static Planet mPlanet;
    public static Bitmap mBitmapEarth;
    public static Bitmap mBitmapMars;
    public static Bitmap mBitmapUranus;
    public static Bitmap mBitmapSaturn;

    public static int mWidth;
    public static int mHeight;

    volatile public static Star mStar;
    public static Bitmap mBitmapStar;

    volatile public static List<Blackhall> mBlackhallList = new ArrayList<>(2);
    volatile public static List<Star> mStarList = new ArrayList<>(15);
    volatile public static List<Planet> mPlanetList = new ArrayList<>(4);

    volatile public static boolean Moving;
    volatile public boolean isStop;
    volatile public static boolean StopFlag;

    volatile public static int Gottenstar = 0;
    volatile public static int Gottenstarsum;

    volatile public static boolean notStartYet;
    volatile public static boolean isLocked;

    private Random mRand;
    volatile public static int numStar;

    volatile public static float timer;
    private long time;
    private long time2;
    volatile public static boolean isFinished;
    volatile public static boolean surfacefinish;
    private static boolean greatswitch;

    MediaPlayer mediaPlayer3;
    SoundPool mSoundPool;
    private int mSoundStar, mSoundCollision;

    SoundPool mSurfaceSoundPool;
    private int mSoundGreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Game","OnCreate");
        super.onCreate(savedInstanceState);
        Log.e("Save", String.valueOf(Gottenstarsum));

        SharedPreferences pref = getSharedPreferences("MyPref", GameActivity.MODE_PRIVATE);
        Gottenstarsum = pref.getInt("StarSum", 0);
        Log.e("Save",String.valueOf(Gottenstarsum));

        Resources rsc = getResources();
        mBitmapBlackhall = BitmapFactory.decodeResource(rsc, R.drawable.blackhall);
        mBitmapSquirrel = BitmapFactory.decodeResource(rsc, R.drawable.squirrel);
        mBitmapStar = BitmapFactory.decodeResource(rsc, R.drawable.star);
        mBitmapEarth = BitmapFactory.decodeResource(rsc, R.drawable.earth);
        mBitmapMars = BitmapFactory.decodeResource(rsc, R.drawable.mars);
        mBitmapSaturn = BitmapFactory.decodeResource(rsc, R.drawable.saturn);
        mBitmapUranus = BitmapFactory.decodeResource(rsc, R.drawable.uranus);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display dp = wm.getDefaultDisplay();
        Point size = new Point();
        dp.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        initialize();

        mSurfaceView = new SurfaceCreate(this);
        setContentView(mSurfaceView);

        mediaPlayer3 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit11);
        mediaPlayer3.setLooping(true);
        mediaPlayer3.seekTo(0);

        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .build())
                .setMaxStreams(5)
                .build();
        mSoundStar = mSoundPool.load(getApplicationContext(), R.raw.se_maoudamashii_system46, 0);
        mSoundCollision = mSoundPool.load(getApplicationContext(), R.raw.se_maoudamashii_retro28, 1);

        mSurfaceSoundPool = new SoundPool.Builder()
            .setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .build())
            .setMaxStreams(1)
            .build();
        mSoundGreat = mSurfaceSoundPool.load(getApplicationContext(), R.raw.se_maoudamashii_onepoint04, 0);

        mThreadRun = true;
        gameThread = new myThread();
        gameThread.start();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("Game","OnStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        mediaPlayer3.start();
        mGestureDetector = new GestureDetector(this, mOnGestureListener);
        //mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit11);
        //mediaPlayer2.start();
        Log.e("Game","OnResume");
    }


    class myThread extends Thread {
        boolean stop = false;

        @Override
        public void run() {
            mIsAttached = true;
            while (mIsAttached) {
                //if(replay == true) {
                //    replay = false;
                //    initialize();
                //}
                time = uptimeMillis();
                Log.e("Game", String.valueOf(time2));
                update();
                time2 = uptimeMillis() - time;
                Log.e("Game2", String.valueOf(time2));
                while (uptimeMillis() - time <= 20) {
                }
            }
            Log.e("Game", "Thread ends");
        }

        public synchronized void setStop() {
            stop = !stop;
            if(!stop) {
                notify();
            }
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer3.pause();
        SharedPreferences pref = getSharedPreferences("MyPref", GameActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("StarSum", Gottenstarsum);
        Log.e("Save",String.valueOf(Gottenstarsum));
        editor.commit();
        noGameActivity = true;
        gameThread.setStop();
    }

    protected void onStop(){
        super.onStop();
        mediaPlayer3.pause();
        Log.e("onStop", "gameactivity Stop");
        Log.e("Game","OnStop");
        //finish();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mediaPlayer3.stop();
        mediaPlayer3.reset();
        mediaPlayer3.release();
        mSoundPool.release();
        mSurfaceSoundPool.release();
        gameThread = null;
        Log.e("Game","onDestroy");
    }

    protected void onRestart(){
        super.onRestart();
        Log.e("Game","OnRestart");
        mSurfaceView = new SurfaceCreate(this);
        setContentView(mSurfaceView);
        mediaPlayer3.start();
        gameThread.setStop();
    }

    @Override
    public void onBackPressed() {
    }

    public void update(){
        try {
            Log.e("GamePhase", "Phase_0");
            if(count >= 4 && isLocked == false) {
                if(velocity_x!=0 || velocity_y!=0){
                    mSquirrel.Svelocity_x = velocity_x/200.0f;
                    mSquirrel.Svelocity_y = velocity_y/200.0f;
                    Moving = true;
                }
                Log.e("GamePhase_rotation", "Phase_0_1");
                for (Planet planet : mPlanetList) {
                    planet.rotation();
                    if (mSquirrel.collision(planet)) {
                        mSoundPool.play(mSoundCollision, 1.0F, 1.0F, 0, 0, 1.0F);
                        synchronized (mStarList) {
                            rePlay();
                        }
                    }
                }
            }
            Log.e("GamePhase", "Phase_1");
            if(Moving == true){
                mSquirrel.update(mSquirrel.calcNetForceExertedByX(mBlackhallList), mSquirrel.calcNetForceExertedByY(mBlackhallList));
                if(mSquirrel.getRight() <0.0f-100.0f || mSquirrel.getLeft() > mWidth+100.0f || mSquirrel.getBottom() < 0.0f-100.0f || mSquirrel.getTop() > mHeight+100.0f){
                    synchronized (mStarList) {
                        rePlay();
                    }
                }
                for(Star star : mStarList) {
                    if(mSquirrel.GotStar(star)){
                        mSoundPool.play(mSoundStar, 1.0F, 1.0F, 0, 0, 1.0F);
                        Gottenstar += 1;
                        star.move(-1000,-1000);
                        numStar += 1;
                    }
                }
            }

            if (timer <= -3.0f) {
                isFinished = true;
            }
            if (count >= 4 && timer > -2500.0f) {
                timer -= 20.0f / 1000.0f;
            }
            if (count < 4) {
                if (portion % 50 == 0) {
                    count += 1;
                }
            }
            portion += 1;
            Log.e("GamePhase", "Phase_2");
            if(timer <=0.0f && isLocked == false){
                timer = 0.0f;
                velocity_x = 0.0f;
                velocity_y = 0.0f;
                isLocked = true;
                Moving = false;
                Gottenstarsum += numStar;
            }
            if(timer <= -2.0f && numStar >= 30 && greatswitch == true){
                mSurfaceSoundPool.play(mSoundGreat, 1.0F, 1.0F, 0, 0, 1.0F);
                greatswitch = false;
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            synchronized (gameThread) {
                if (gameThread.stop) {
                    Log.e("Wait", "Waiting");
                    gameThread.wait();
                }
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    private void rePlay(){
        mSquirrel.setLocate(mWidth/2, (float)mHeight-(float)mBitmapSquirrel.getHeight());
        newStar(Gottenstar);
        Gottenstar = 0;
        Moving = false;
        StopFlag = false;
        mSquirrel.Svelocity_x = 0.0f;
        mSquirrel.Svelocity_y = 0.0f;
        velocity_x = 0.0f;
        velocity_y = 0.0f;
    }
    private void newBlackhall(){
        Blackhall blackhall;
        mBlackhallList.clear();
        List<Float> left = new ArrayList<>(2);
        List<Float> top = new ArrayList<>(2);
        float nextleft;
        float nexttop;
        boolean flag = true;

        for(int i=0; i<2; i++){
            nextleft = mWidth/2 * i +(float)(mRand.nextInt(mWidth/32)-mWidth/16);
            nexttop = (float)(mRand.nextInt(mHeight/2)
            );
            //float left = 200 + 500 * i + (float)(mRand.nextInt(mWidth/4)-mWidth/4);
            //float top = 200 + 600 * i + (float)(mRand.nextInt(mHeight/4)-mWidth/4);
            if(i>0){
                while(flag == true){
                    flag = false;
                    if( (nexttop-200.0f) <= top.get(0) && (nexttop+200.0f) >= top.get(0)){
                        nexttop = (float)(mRand.nextInt(mHeight/2));
                        flag = true;
                    }
                }
            }

            left.add(nextleft);
            top.add(nexttop);
            float mass = 1000;
            blackhall = new Blackhall(nextleft, nexttop, mBitmapBlackhall.getWidth(), mBitmapBlackhall.getHeight(), mass);
            mBlackhallList.add(blackhall);
        }
        left.clear();
        top.clear();
    }
    private void newSquirrel(){
        mSquirrel = new Squirrel(mWidth/2-mBitmapSquirrel.getWidth()/2, mHeight-mBitmapSquirrel.getHeight()-100, mBitmapSquirrel.getWidth(), mBitmapSquirrel.getHeight(), 1000);
    }
    private void newStar(int number){
        Star star;
        if(number == -1) {
            mStarList.clear();
            number = 15;
        }
        for(int i=0; i<number; i++){
            //float left = 100;
            //float top = 100;
            float left = mRand.nextInt(mWidth);
            float top = mRand.nextInt(mHeight - mBitmapStar.getHeight() * 2);
            float mass = 10.0f;
            star = new Star(left, top, mBitmapStar.getWidth(), mBitmapStar.getHeight(), mass);
            mStarList.add(star);
        }
    }
    private void newPlanet(){
        Log.e("Surface","planet1");
        Planet planet;
        Log.e("Surface","planet2");
        mPlanetList.clear();
        Log.e("Surface","planet3");
        List<Bitmap> images= new ArrayList<>(4);
        Log.e("Surface","planet4");
        List<Float> left = new ArrayList<>(4);
        Log.e("Surface","planet5");
        List<Float> top = new ArrayList<>(4);
        Log.e("Surface","planet6");
        float nextleft;
        Log.e("Surface","planet7");
        float nexttop;
        Log.e("Surface","planet8");
        //images.add(mBitmapEarth);
        //images.add(mBitmapMars);
        //images.add(mBitmapSaturn);
        //images.add(mBitmapUranus);
        float degrees = 0.0f;
        float magx = 0.0f;
        float magy = 0.0f;
        int speed = 0;
        Log.e("Surface","planet9");
        Bitmap image = null;
        Log.e("Surface","planet10");
        for(int i=0; i<4; i++){
            Log.e("Surface","planet10_2");
            nextleft = (float)(mRand.nextInt(mWidth - 80)+40);
            Log.e("Surface","planet10_3");
            nexttop = (float)(mRand.nextInt(mHeight - 900)+300);
            Log.e("Surface","planet10_4");
            for(int j=0; j < i; j++){
                Log.e("Surface","planet10_5");
                //if not acceptable execute nextInt again
                if( (nexttop-100.0f) <= top.get(j) && (nexttop+100.0f) >= top.get(j)){
                    Log.e("Surface","planet10_6");
                    nexttop = (float)(mRand.nextInt(mHeight - 900)+300);
                    Log.e("Surface","planet10_7");
                    //j = 0;
                    Log.e("Surface","planet10_8");
                }
            }
            Log.e("Surface","planet11");
            left.add(nextleft);
            Log.e("Surface","planet12");
            top.add(nexttop);
            Log.e("Surface","planet13");
            if(i == 0){
                degrees = 0.0f + 45.0f * i;
                magx = 8.0f;
                magy = 8.0f;
                speed = 6;
                image = mBitmapMars;
            } else if(i == 1){
                degrees = 0.0f + 45.0f * i;
                magx = 10.0f;
                magy = 10.0f;
                speed = 2;
                image = mBitmapEarth;
            } else if(i == 2){
                degrees = 0.0f + 45.0f * i;
                magx = 5.0f;
                magy = 5.0f;
                speed = 1;
                image = mBitmapSaturn;
            } else if(i == 3){
                degrees = 0.0f + 45.0f * i;
                magx = 8.0f;
                magy = 12.0f;
                speed = 2;
                image = mBitmapUranus;
            }
            Log.e("Surface","planet14");
            float mass = 10.0f;
            Log.e("Surface","planet15");
            planet = new Planet(nextleft, nexttop, image.getWidth(), image.getHeight(), mass, degrees, magx, magy, speed, image);
            Log.e("Surface","planet16");
            mPlanetList.add(planet);
            Log.e("Surface","planet17");
        }
        Log.e("Surface","planet18");
    }
    private void initialize(){
        mRand = new Random();
        numStar = 0;
        newBlackhall();
        newStar(-1);
        newPlanet();
        newSquirrel();
        Moving = false;
        isFinished = false;
        isLocked = false;
        surfacefinish = false;
        greatswitch = true;
        timer = 10.0f;
        count = 0;
        portion = 1;
    }

    @Override
    public  boolean onTouchEvent(MotionEvent event){
        Log.e("Touch","nonTouch");
        if (isFinished == true) {
            Log.e("Touch","yesTouch");
            isFinished = false;
            replay = true;
            initialize();
        }
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
