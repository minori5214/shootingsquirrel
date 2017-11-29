package com.giraffe.minori.squirrelshooting;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.Timer;

/**
 * Created by minori on 2017/09/28.
 */

public class Planet extends GameObject {
    private Bitmap image;
    private float degrees;
    private float Pvelocity_x;
    private float Pvelocity_y;
    private float magx;
    private float magy;
    private int speed;
    public Planet(float left, float top, float width, float height, float mass, float degrees, float magx, float magy, int speed, Bitmap image){
        super(left, top, width, height, mass);
        this.image = image;
        this.degrees = degrees;
        this.magx = magx;
        this.magy = magy;
        this.speed = speed;

    }
    public Bitmap getImage(){
        return image;
    }
    public void rotation(){
        double rad = degrees * (Math.PI / 180F);
        Pvelocity_x = magx * (float)Math.cos(rad);
        Pvelocity_y = magy * (float)Math.sin(rad);
        this.move(Pvelocity_x,Pvelocity_y);
        degrees += this.speed;
    }
}
