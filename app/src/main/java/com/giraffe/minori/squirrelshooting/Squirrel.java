package com.giraffe.minori.squirrelshooting;

import java.util.List;

import static com.giraffe.minori.squirrelshooting.GameActivity.velocity_x;
import static com.giraffe.minori.squirrelshooting.GameActivity.velocity_y;
import static com.giraffe.minori.squirrelshooting.SurfaceCreate.StopFlag;

/**
 * Created by minori on 2017/09/26.
 */

public class Squirrel extends GameObject {
    public float Svelocity_x;
    public float Svelocity_y;
    public Squirrel(float left, float top, float width, float height, float mass)
    {
        super(left, top, width, height, mass);
        Svelocity_x = 0.0f;
        Svelocity_y = 0.0f;
    }
    public void move(float velocityX, float velocityY){
        super.move(velocityX, velocityY);
    }
    public float calcDistance(GameObject B){
        float dx = (B.getCenterX() - this.getCenterX());
        float dy = (B.getCenterY() - this.getCenterY());
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
    public float calcForceExertedBy(Blackhall B){
        float G = 300.0f;
        float r = calcDistance(B);
        if (r <= 30.0f){
            r = 30.0f;
            StopFlag = true;
        }
        return G * this.getMass() * B.getMass() / (r * 150);
    }
    public float calcForceExertedByX(Blackhall B){
        float dx = (B.getCenterX() - this.getCenterX());
        float r = calcDistance(B);
        if (r <= 30.0f){
            r = 30.0f;
        }
        return calcForceExertedBy(B) * dx / r - this.getMass() * this.Svelocity_x * this.Svelocity_x / r;
    }
    public float calcForceExertedByY(Blackhall B){
        float dy = (B.getCenterY() - this.getCenterY());
        float r = calcDistance(B);
        if (r <= 30.0f){
            r = 30.0f;
        }
        return calcForceExertedBy(B) * dy / r - this.getMass() * this.Svelocity_y * this.Svelocity_y / r;
    }
    public float calcNetForceExertedByX(List<Blackhall> B){
        float netxForce = 0.0f;
        for (Blackhall blackhall : B){
            if (this.equals(blackhall)){
            } else {
                netxForce += this.calcForceExertedByX(blackhall);
            }
        }
        return netxForce * this.Svelocity_x/10.0f;
    }
    public float calcNetForceExertedByY(List<Blackhall> B){
        float netyForce = 0.0f;
        for (Blackhall blackhall : B){
            if (this.equals(blackhall)){
            } else {
                netyForce += this.calcForceExertedByY(blackhall);
            }
        }
        return netyForce * this.Svelocity_y/10.0f;
    }
    public void update(float Fx, float Fy){
        float ax = Fx / this.getMass();
        float ay = Fy / this.getMass();
        if (this.Svelocity_x < 100.0f || this.Svelocity_y < 100.0f) {
            this.Svelocity_x -= ax;
            this.Svelocity_y -= ay;
        }
        this.move(Svelocity_x,Svelocity_y);
    }
    public boolean GotStar(Star star){
        if(calcDistance(star) <= 70.0f){
            return true;
        }
        return false;
    }
    public boolean collision(Planet planet){
        if(calcDistance(planet) <= planet.getHeight()/2){
            return true;
        }
        return false;
    }
}
