package com.giraffe.minori.squirrelshooting;

/**
 * Created by minori on 2017/09/26.
 */

public class GameObject {
    private float left;
    private float top;
    private float width;
    private float height;
    private float mass;
    public GameObject(float left, float top, float width, float height, float mass){
        setLocate(left, top);
        this.width = width;
        this.height = height;
        this.mass = mass;
    }
    public void setLocate(float left, float top){
        this.left = left;
        this.top = top;
    }
    public void move(float left, float top){
        this.left = getLeft() - left;
        this.top = getTop() - top;
    }
    public float getLeft(){
        return left;
    }
    public float getRight(){
        return left + width;
    }
    public float getTop(){
        return top;
    }
    public float getBottom(){
        return top + height;
    }
    public float getMass(){
        return mass;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public float getCenterX(){
        return (getLeft() + width / 2);
    }
    public float getCenterY(){
        return (getTop() + height / 2);
    }
}
