package com.giraffe.minori.squirrelshooting;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout tapLayout;
    private Handler mHandler = new Handler();
    private ScheduledExecutorService mScheduledExecutor;
    private TextView mLblMeasuring;
    public static boolean noGameActivity;
    MediaPlayer mediaPlayer;
    public static MediaPlayer mediaPlayer2;
    public static boolean isGameBGMeEnded;

    private void startMeasure() {
        mLblMeasuring = (TextView) findViewById(R.id.title);

        /**
         * 第一引数: 繰り返し実行したい処理
         * 第二引数: 指定時間後に第一引数の処理を開始
         * 第三引数: 第一引数の処理完了後、指定時間後に再実行
         * 第四引数: 第二、第三引数の単位
         *
         * new Runnable（無名オブジェクト）をすぐに（0秒後に）実行し、完了後1700ミリ秒ごとに繰り返す。
         * （ただしアニメーションの完了からではない。Handler#postが即時実行だから？？）
         */
        mScheduledExecutor = Executors.newScheduledThreadPool(2);

        mScheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mLblMeasuring.setVisibility(View.VISIBLE);

                        // HONEYCOMBより前のAndroid SDKがProperty Animation非対応のため
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            animateAlpha();
                        }
                    }
                });
            }


            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            private void animateAlpha() {

                // 実行するAnimatorのリスト
                List<Animator> animatorList = new ArrayList<Animator>();

                // alpha値を0から1へ1000ミリ秒かけて変化させる。
                ObjectAnimator animeFadeIn = ObjectAnimator.ofFloat(mLblMeasuring, "alpha", 0f, 1f);
                animeFadeIn.setDuration(1000);

                // alpha値を1から0へ600ミリ秒かけて変化させる。
                ObjectAnimator animeFadeOut = ObjectAnimator.ofFloat(mLblMeasuring, "alpha", 1f, 0f);
                animeFadeOut.setDuration(600);

                // 実行対象Animatorリストに追加。
                animatorList.add(animeFadeIn);
                animatorList.add(animeFadeOut);

                final AnimatorSet animatorSet = new AnimatorSet();

                // リストの順番に実行
                animatorSet.playSequentially(animatorList);

                animatorSet.start();
            }
        }, 0, 1700, TimeUnit.MILLISECONDS);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Main","OnCreate");
        noGameActivity = true;
        //isGameBGMeEnded = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit13);
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit11);
        mediaPlayer.start();
        startMeasure();
        //tapLayout = (ConstraintLayout)findViewById(R.id.tapLayout);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.e("Main","OnResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.pause();
        Log.e("Main","onPause");
    }

    @Override
    protected void onStop(){
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        super.onStop();
        Log.e("Main","OnStop");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        mediaPlayer2.stop();
        mediaPlayer2.reset();
        mediaPlayer2.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit13);
        mediaPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.bgm_maoudamashii_8bit11);
        mediaPlayer.start();
        Log.e("Main","onRestart");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer2.release();
        Log.e("Main","onDestroy");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.e("Main","You touched mainactivity!");
        if(noGameActivity==true) {
            noGameActivity = false;
            //mediaPlayer.pause();
            //isGameBGMeEnded = false;
            mediaPlayer2.start();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        }
        return super.onTouchEvent(event);
    }
}
