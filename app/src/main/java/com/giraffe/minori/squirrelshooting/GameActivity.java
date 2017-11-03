package com.giraffe.minori.squirrelshooting;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
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

import static com.giraffe.minori.squirrelshooting.SurfaceCreate.replay;
import static com.giraffe.minori.squirrelshooting.MainActivity.noGameActivity;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.mThread;
import static java.lang.Math.abs;


public class GameActivity extends AppCompatActivity implements Runnable{
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
    private Thread gameThread;
    boolean mThreadRun;
    private boolean mIsAttached;

    public static Squirrel mSquirrel;
    public static Bitmap mBitmapSquirrel;

    public static Blackhall mBlackhall;
    public static Bitmap mBitmapBlackhall;

    public static Planet mPlanet;
    public static Bitmap mBitmapEarth;
    public static Bitmap mBitmapMars;
    public static Bitmap mBitmapUranus;
    public static Bitmap mBitmapSaturn;

    public static int mWidth;
    public static int mHeight;

    public static Star mStar;
    public static Bitmap mBitmapStar;

    public static List<Blackhall> mBlackhallList = new ArrayList<>(2);
    public static List<Star> mStarList = new ArrayList<>(15);
    public static List<Planet> mPlanetList = new ArrayList<>(4);

    public static boolean Moving;
    public boolean isStop;
    public static boolean StopFlag;

    public static int Gottenstar = 0;

    public static boolean notStartYet;
    public static boolean isLocked;

    private Random mRand;
    public static int numStar;

    public static float timer;
    private long time;
    public static boolean isFinished;

    MediaPlayer mediaPlayer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Game","OnCreate");
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);

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
        mThreadRun = true;
        gameThread = new Thread(this);
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

    @Override
    public void run() {
        mIsAttached = true;
        while (mIsAttached) {
            //if(replay == true) {
            //    replay = false;
            //    initialize();
            //}
            while (isFinished == false) {
                time = System.currentTimeMillis();
                update();
                while (System.currentTimeMillis() - time <= 20) {

                }
            }
        }
        Log.e("Surface", "Thread ends");
    }

    @Override
    protected void onPause(){
        super.onPause();
        noGameActivity = true;
        Log.e("Game","OnPause");
        //mediaPlayer3.pause();
    }

    protected void onStop(){
        super.onStop();
        Log.e("Game","OnStop");
        //finish();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mediaPlayer3.stop();
        mediaPlayer3.reset();
        mediaPlayer3.release();
        Log.e("Game","onDestroy");
    }

    protected void onRestart(){
        super.onRestart();
        Log.e("Game","OnRestart");
        mSurfaceView = new SurfaceCreate(this);
        setContentView(mSurfaceView);
        //mediaPlayer3.start();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


    //@Override
    //public void onBackPressed() {
    //}

    public void update(){
        try {

            if(count >= 4) {
                if(velocity_x!=0 || velocity_y!=0){
                    mSquirrel.Svelocity_x = velocity_x/200.0f;
                    mSquirrel.Svelocity_y = velocity_y/200.0f;
                    Moving = true;
                }
                for (Planet planet : mPlanetList) {
                    planet.rotation();
                    if (mSquirrel.collision(planet)) {
                        StopFlag = true;
                    }
                }
            }
            if(Moving == true){
                mSquirrel.update(mSquirrel.calcNetForceExertedByX(mBlackhallList), mSquirrel.calcNetForceExertedByY(mBlackhallList));
                for(Star star : mStarList) {
                    if(mSquirrel.GotStar(star)){
                        Gottenstar += 1;
                        star.move(-1000,-1000);
                        numStar += 1;
                    }
                }
            }

            //mSquirrel.move(velocity_x/100.0f,velocity_y/100.0f);
            if(isStop() == true){
                //if(mSquirrel.getRight() <0.0f-100.0f || mSquirrel.getLeft() > mWidth+100.0f || mSquirrel.getBottom() < 0.0f-100.0f || mSquirrel.getTop() > mHeight+100.0f){
                //mThread.sleep(1000);
                newStar(Gottenstar);
                Gottenstar = 0;
                mSquirrel.setLocate(mWidth/2, (float)mHeight-(float)mBitmapSquirrel.getHeight());
                Moving = false;
                StopFlag = false;
                mSquirrel.Svelocity_x = 0.0f;
                mSquirrel.Svelocity_y = 0.0f;
                velocity_x = 0.0f;
                velocity_y = 0.0f;
            }

            if(timer <=0.0f){
                timer = 0.0f;
                velocity_x = 0.0f;
                velocity_y = 0.0f;
                //mIsAttached = false;
                //isFinished = true;
                isLocked = true;
                while(isLocked == true){
                    //gameThread.sleep(30);
                }
                while(isFinished == true){
                    gameThread.sleep(300);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean isStop(){
        if(mSquirrel.getRight() <0.0f-100.0f || mSquirrel.getLeft() > mWidth+100.0f || mSquirrel.getBottom() < 0.0f-100.0f || mSquirrel.getTop() > mHeight+100.0f) {
            return true;
        }
        if(StopFlag == true){
            return true;
        }
        return false;
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
        //Log.e("Surface","Then here");
        mRand = new Random();
        //Log.e("Surface","Then here2");
        numStar = 0;
        //Log.e("Surface","Then here3");
        newBlackhall();
        //Log.e("Surface","Then here4");
        newStar(-1);
        //Log.e("Surface","Then here5");
        newPlanet();
        //Log.e("Surface","Then here6");
        newSquirrel();
        //Log.e("Surface","Then here7");
        Moving = false;
        //Log.e("Surface","Then here8");
        isFinished = false;
        //Log.e("Surface","Then here9");
        timer = 10.0f;
        //Log.e("Surface","Then here10");
        notStartYet = true;
        //Log.e("Surface","Then here11");
        count = 0;
        portion = 1;
        //Log.e("Surface","Then here12");
    }

    @Override
    public  boolean onTouchEvent(MotionEvent event){
        if (isFinished == true) {
            isFinished = false;
            //mSurfaceView = new SurfaceCreate(this);
            //setContentView(mSurfaceView);
            replay = true;
            initialize();
            //finish();
            //Intent intent = new Intent(GameActivity.this, Result.class);
            //startActivity(intent);
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
