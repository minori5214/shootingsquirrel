package com.giraffe.minori.squirrelshooting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import static com.giraffe.minori.squirrelshooting.MainActivity.noGameActivity;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.mHeight;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.mWidth;

public class Result extends AppCompatActivity {
    private Canvas mCanvas = null;
    private Paint mPaint = null;
    private Bitmap mBitmapSquirrel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Result","onCreate");
        setContentView(R.layout.activity_result);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("Result","onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.e("Result","onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.e("Result","onStop");
    }
    protected void onDestroy(){
        super.onDestroy();
        Log.e("Result","onDestroy");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.e("Result","You touched resultactivity!");
        if(noGameActivity==true) {
            noGameActivity = false;
            Intent intent = new Intent(Result.this, GameActivity.class);
            startActivity(intent);
        }
        return super.onTouchEvent(event);
    }
}
