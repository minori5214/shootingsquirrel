package com.giraffe.minori.squirrelshooting;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import icepick.Icepick;

import static android.os.SystemClock.sleep;
import static android.os.SystemClock.uptimeMillis;
import static com.giraffe.minori.squirrelshooting.GameActivity.Gottenstarsum;
import static com.giraffe.minori.squirrelshooting.GameActivity.count;
import static com.giraffe.minori.squirrelshooting.GameActivity.isFinished;
import static com.giraffe.minori.squirrelshooting.GameActivity.mBitmapBlackhall;
import static com.giraffe.minori.squirrelshooting.GameActivity.mBitmapSquirrel;
import static com.giraffe.minori.squirrelshooting.GameActivity.mBitmapStar;
import static com.giraffe.minori.squirrelshooting.GameActivity.mBlackhallList;
import static com.giraffe.minori.squirrelshooting.GameActivity.mPlanetList;
import static com.giraffe.minori.squirrelshooting.GameActivity.mSquirrel;
import static com.giraffe.minori.squirrelshooting.GameActivity.mStarList;
import static com.giraffe.minori.squirrelshooting.GameActivity.numStar;
import static com.giraffe.minori.squirrelshooting.GameActivity.surfacefinish;
import static com.giraffe.minori.squirrelshooting.GameActivity.timer;
import static com.giraffe.minori.squirrelshooting.GameActivity.velocity_x;
import static com.giraffe.minori.squirrelshooting.GameActivity.velocity_y;
import static com.giraffe.minori.squirrelshooting.R.id.time;


public class SurfaceCreate extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    public static int mWidth;
    public static int mHeight;

    private SurfaceHolder mHolder;
    private Canvas mCanvas = null;

    private boolean mIsAttached2;
    public static Thread mThread;

    private Paint mPaint = null;

    private Bitmap bmp;

    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SWIPE_MAX_OFF_PATH = 250;

    private long time;
    private long time2;
    public static boolean replay;


    public SurfaceCreate(Context context){
        super(context);
        Log.e("Surface","surfaceCreateConstructor"+i);
        mHolder = getHolder();
        mHolder.addCallback(this);
        //setFocusable(true);
        //requestFocus();
    }

    public static int i=0;
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Log.e("Surface","surfaceCreated"+i);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Resources rsc2 = getResources();
        //bmp = BitmapFactory.decodeResource(rsc2,  R.drawable.space);
        mIsAttached2 = true;
        mWidth = getWidth();
        mHeight = getHeight();
        mThread = new Thread(this);
        Log.e("Surface","Thread starts"+i);
        mThread.start();
        Log.e("Surface","Thread tachiagatta");
        i++;
    }

    @Override
    public void run(){
        Log.e("Surface","here?");
        //replay = true;
        Log.e("Surface","or here?");
        Log.e("Surface","Run starts now");
        while(mIsAttached2) {
            time = uptimeMillis();
            drawGameBoard();
            //Log.e("Surface2", String.valueOf(time));
            //sleep(20-time2);
            while (uptimeMillis() - time <= 20) {

            }
        }
        Log.e("Surface","Thread ends");

    }

    public void drawGameBoard(){
        try {
            mCanvas = getHolder().lockCanvas();
            time2 = uptimeMillis();
            Log.e("Surface_Phase", String.valueOf(time2));
            //sleep(3);
            if(mCanvas != null) {
                mCanvas.drawColor(Color.rgb(42, 42, 90));
                //mCanvas.drawBitmap(bmp, 0, 0, null);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(100);
                mCanvas.drawText("Score : " + String.valueOf(numStar), 100, 100, mPaint);
                //mCanvas.drawText("velocityY : " + String.valueOf(velocity_y),100,300,mPaint);
                //mCanvas.drawText(String.valueOf(mSquirrel.calcNetForceExertedByY(mBlackhallList)),100,100,mPaint);
                //mCanvas.drawText(String.valueOf(velocity_x),100,500,mPaint);
                //mCanvas.drawText(String.valueOf(mSquirrel.Svelocity_x),100,600,mPaint);
                //mCanvas.drawText(String.valueOf(velocity_y),100,700,mPaint);
                //mCanvas.drawText(String.valueOf(mSquirrel.Svelocity_y),100,800,mPaint);
                Log.e("Surface_Phase", "Phase---1");
                Log.e("Surface_Phase", String.valueOf(mCanvas==null));
                for (Blackhall blackhall : mBlackhallList) {
                    mCanvas.drawBitmap(mBitmapBlackhall, blackhall.getLeft(), blackhall.getTop(), null);
                }
                synchronized (mStarList) {
                    Log.e("Surface_Phase", "Phase---2");
                    for (Star star : mStarList) {
                        mCanvas.drawBitmap(mBitmapStar, star.getLeft(), star.getTop(), null);
                    }
                    Log.e("Surface_Phase", "Phase---3");
                }
                for (Planet planet : mPlanetList) {
                    mCanvas.drawBitmap(planet.getImage(), planet.getLeft(), planet.getTop(), null);
                }
                Log.e("Surface_Phase", "Phase---4");
                //mCanvas.drawBitmap(mBitmapPlanet, mPlanet.getLeft(), mPlanet.getTop(), null);
                mCanvas.drawBitmap(mBitmapSquirrel, mSquirrel.getLeft(), mSquirrel.getTop(), null);
                Log.e("Surface_Phase", "Phase---5");
                if (timer <= 0.0f && surfacefinish == false) {
                    //mCanvas = getHolder().lockCanvas();
                    mCanvas.drawText("Time : 0.00", 100, 200, mPaint);
                    //mThread.sleep(1000);
                    mPaint.setTextSize(200);
                    mCanvas.drawText("Finished!!!", mWidth / 4 - 140, mHeight / 2 - 100, mPaint);
                    if (timer <= -2.0f){
                        surfacefinish = true;
                    }
                }
                Log.e("Surface_Phase", "Phase---6");
                if (surfacefinish == true) {
                    mCanvas.drawColor(Color.WHITE);
                    mPaint.setTextSize(100);
                    mPaint.setColor(Color.BLACK);
                    mCanvas.drawBitmap(mBitmapSquirrel, mWidth / 2 - 50, mHeight / 2 - 30, null);
                    mCanvas.drawText("Your Score : " + String.valueOf(numStar), mWidth / 4 - 50, mHeight / 2 - 30, mPaint);
                    mCanvas.drawText("Touch screen", mWidth / 4 - 30, mHeight / 2 + 270, mPaint);
                    mPaint.setTextSize(80);
                    mCanvas.drawText("Your Star : " + String.valueOf(Gottenstarsum), mWidth / 4 - 10, mHeight / 2 + 370, mPaint);
                    mCanvas.drawText("Back to Menu" , mWidth / 4, mHeight / 2 + 570, mPaint);
                    Log.e("Surface", String.valueOf(timer));
                    if(timer <= -2.7f && numStar >= 30){
                        mPaint.setTextSize(80);
                        mCanvas.drawText("< Great!!", mWidth / 2 +150, mHeight / 2 + 110, mPaint);
                    }
                }
                if (count >= 4 && timer > -2.0f) {
                    if (timer > 0.0f) {
                        String displaytime = String.format("%.2f", timer);
                        mCanvas.drawText("Time : " + displaytime, 100, 200, mPaint);
                    } else if (timer >= 10.0f) {
                        mCanvas.drawText("Time : " + 10.0, 100, 200, mPaint);
                    }
                }
                if (count < 4) {
                    mCanvas.drawText("Time : " + 10.0, 100, 200, mPaint);
                    if (count < 3) {
                        mPaint.setTextSize(500);
                        mCanvas.drawText(String.valueOf(3 - count), mWidth / 2 - 80, mHeight / 2, mPaint);
                    }
                    if (count == 3) {
                        mPaint.setTextSize(300);
                        mCanvas.drawText("Start!!", mWidth / 4 - 80, mHeight / 2 - 100, mPaint);
                    }
                }
            }
            getHolder().unlockCanvasAndPost(mCanvas);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        Log.e("Surface","surfaceChanged");

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        Log.e("Surface","surfaceDestroyed");
        mIsAttached2 = false;
        Log.e("Surface", "mIsAttached was changed to false");
        mThread = null;
        Log.e("Surface", "Thread is killed");
        }
    }
