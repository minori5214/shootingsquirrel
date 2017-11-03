package com.giraffe.minori.squirrelshooting;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.giraffe.minori.squirrelshooting.GameActivity.velocity_x;
import static com.giraffe.minori.squirrelshooting.GameActivity.velocity_y;


public class SurfaceCreate extends SurfaceView implements SurfaceHolder.Callback, Runnable{

    private int count;

    public static int mWidth;
    public static int mHeight;

    private SurfaceHolder mHolder;
    private Canvas mCanvas = null;

    public static boolean mIsAttached;
    public static Thread mThread;

    private Paint mPaint = null;

    private Bitmap bmp;

    private Squirrel mSquirrel;
    private Bitmap mBitmapSquirrel;

    private Blackhall mBlackhall;
    private Bitmap mBitmapBlackhall;

    private Planet mPlanet;
    private Bitmap mBitmapEarth;
    private Bitmap mBitmapMars;
    private Bitmap mBitmapUranus;
    private Bitmap mBitmapSaturn;

    private Star mStar;
    private Bitmap mBitmapStar;

    private List<Blackhall> mBlackhallList = new ArrayList<>(2);
    private List<Star> mStarList = new ArrayList<>(15);
    private List<Planet> mPlanetList = new ArrayList<>(4);

    private Random mRand;
    public static int numStar;

    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    public static boolean Moving;
    public boolean isStop;
    public static boolean StopFlag;

    private long time;
    private float timer;
    public static boolean isFinished;
    //public static boolean replay;

    public static int Gottenstar = 0;

    private static boolean notStartYet;

    public SurfaceCreate(Context context){
        super(context);
        Log.e("Surface","surfaceCreateConstructor"+i);
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        requestFocus();
    }

    public static int i=0;
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Log.e("Surface","surfaceCreated"+i);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Resources rsc = getResources();
        bmp = BitmapFactory.decodeResource(rsc,  R.drawable.space);
        mBitmapBlackhall = BitmapFactory.decodeResource(rsc, R.drawable.blackhall);
        mBitmapSquirrel = BitmapFactory.decodeResource(rsc, R.drawable.squirrel);
        mBitmapStar = BitmapFactory.decodeResource(rsc, R.drawable.star);
        mBitmapEarth = BitmapFactory.decodeResource(rsc, R.drawable.earth);
        mBitmapMars = BitmapFactory.decodeResource(rsc, R.drawable.mars);
        mBitmapSaturn = BitmapFactory.decodeResource(rsc, R.drawable.saturn);
        mBitmapUranus = BitmapFactory.decodeResource(rsc, R.drawable.uranus);

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
        mIsAttached = true;
        //replay = true;
        initialize();
        Log.e("Surface","or here?");
        Log.e("Surface","Run starts now");
        while(mIsAttached) {
            //if(replay == true) {
            //    replay = false;
            //    initialize();
            //}
            while(isFinished == false) {
                time = System.currentTimeMillis();
                drawGameBoard();
                while (System.currentTimeMillis() - time <= 20) {

                }
            }
        }
        Log.e("Surface","Thread ends");

    }

    public void drawGameBoard(){
        try {
            if(notStartYet == false) {
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
                timer -= 20.0f / 1000.0f;
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

            mCanvas = getHolder().lockCanvas();
            Thread.sleep(3);
            mCanvas.drawColor(Color.rgb(42,42,90));
            //mCanvas.drawBitmap(bmp, 0, 0, null);
            mPaint.setColor(Color.WHITE);
            mPaint.setTextSize(100);
            mCanvas.drawText("Score : " + String.valueOf(numStar),100,100,mPaint);
            //mCanvas.drawText("velocityY : " + String.valueOf(velocity_y),100,300,mPaint);
            //mCanvas.drawText(String.valueOf(mSquirrel.calcNetForceExertedByY(mBlackhallList)),100,100,mPaint);
            //mCanvas.drawText(String.valueOf(velocity_x),100,500,mPaint);
            //mCanvas.drawText(String.valueOf(mSquirrel.Svelocity_x),100,600,mPaint);
            //mCanvas.drawText(String.valueOf(velocity_y),100,700,mPaint);
            //mCanvas.drawText(String.valueOf(mSquirrel.Svelocity_y),100,800,mPaint);

            for (Blackhall blackhall : mBlackhallList){
                mCanvas.drawBitmap(mBitmapBlackhall, blackhall.getLeft(), blackhall.getTop(), null);
            }
            for (Star star : mStarList){
                mCanvas.drawBitmap(mBitmapStar, star.getLeft(), star.getTop(), null);
            }
            for (Planet planet : mPlanetList){
                mCanvas.drawBitmap(planet.getImage(), planet.getLeft(), planet.getTop(), null);
            }
            //mCanvas.drawBitmap(mBitmapPlanet, mPlanet.getLeft(), mPlanet.getTop(), null);
            mCanvas.drawBitmap(mBitmapSquirrel, mSquirrel.getLeft(), mSquirrel.getTop(), null);

            if(timer <=0.0f){
                timer = 0.0f;
                //mCanvas = getHolder().lockCanvas();
                mCanvas.drawText("Time : 0.00",100,200,mPaint);
                //mThread.sleep(1000);
                mPaint.setTextSize(200);
                mCanvas.drawText("Finished!!!",mWidth/4-140,mHeight/2-100,mPaint);
                getHolder().unlockCanvasAndPost(mCanvas);
                mThread.sleep(2000);
                getHolder().lockCanvas();
                mCanvas.drawColor(Color.WHITE);
                mPaint.setTextSize(100);
                mPaint.setColor(Color.BLACK);
                mCanvas.drawText("Your Score : " + String.valueOf(numStar), mWidth/4-50, mHeight/2,mPaint);
                mCanvas.drawText("Touch screen", mWidth/4-40, mHeight/2+300,mPaint);
                mCanvas.drawBitmap(mBitmapSquirrel, mWidth/2-50, mHeight/2+30, null);
                getHolder().unlockCanvasAndPost(mCanvas);
                mThread.sleep(500);
                velocity_x = 0.0f;
                velocity_y = 0.0f;
                mIsAttached = false;
                isFinished = true;
            }

            String displaytime = String.format("%.2f", timer);
            mCanvas.drawText("Time : " + displaytime,100,200,mPaint);

            if(notStartYet == true){
                if(count < 3) {
                    mPaint.setTextSize(500);
                    mCanvas.drawText(String.valueOf(3-count), mWidth / 2 - 80 , mHeight / 2, mPaint);
                }
                if(count == 3) {
                    mPaint.setTextSize(300);
                    mCanvas.drawText("Start!!", mWidth / 4 - 80, mHeight / 2 - 100, mPaint);
                    notStartYet = false;
                }
            }

            getHolder().unlockCanvasAndPost(mCanvas);

            if(count < 4){
                mThread.sleep(1000);
                count += 1;
            }

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
        if(mBitmapBlackhall!=null){
            mBitmapBlackhall.recycle();
            mBitmapBlackhall=null;
        }
        if(mBitmapEarth!=null){
            mBitmapEarth.recycle();
            mBitmapEarth=null;
        }
        if(mBitmapMars!=null){
            mBitmapMars.recycle();
            mBitmapMars=null;
        }
        if(mBitmapUranus!=null){
            mBitmapUranus.recycle();
            mBitmapUranus=null;
        }
        if(mBitmapSaturn!=null){
            mBitmapSaturn.recycle();
            mBitmapSaturn=null;
        }
        if(mBitmapStar!=null){
            mBitmapStar.recycle();
            mBitmapStar=null;
        }
        if(mBitmapSquirrel!=null){
            mBitmapSquirrel.recycle();
            mBitmapSquirrel=null;
        }
        mIsAttached = false;
        while(mThread.isAlive());
        mThread = null;
        Log.e("Surface","Thread is killed");
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
        mSquirrel = new Squirrel(mWidth/2, mHeight-mBitmapSquirrel.getHeight(), mBitmapSquirrel.getWidth(), mBitmapSquirrel.getHeight(), 1000);
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
        //Log.e("Surface","Then here12");
    }
}