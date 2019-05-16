package com.rvn;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import static com.rvn.InGameActivity.HEIGHT;
import static com.rvn.InGameActivity.WIDTH;
import static com.rvn.InGameActivity.mContext;
import static java.lang.Math.sqrt;

public class Meteores extends ObjectView {
    public float speed, size;
    private int rotateMs;

    private RotateAnimation rotate;


    public Meteores(Game g, float sp, float size){
        super(mContext, g, R.drawable.meteore, (int) size*150, (int) size*150);
        this.size= size; this.speed= sp; this.rotate = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        this.rotateMs= g.getRotateMs();
        initRotate();

        do
            generatePosition();
        while(g.hasCollision(this));

        final Meteores met= this;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) { clearAnimation(); game.onClickMeteore(met); }
        });
    }
    public Meteores(Game g){
        this(g,1,1);
    }

    public void generatePosition(){
        boolean is_left_and_right_pop=  ( ((int) (Math.random() * 3)) == 1);
        boolean is_top_or_left_pop=     ( ((int) (Math.random() * 2)) == 1);

        float x,y;
        float randX= (float)(Math.random()*(WIDTH/3));
        float randY= (float)(Math.random()*(HEIGHT/3));

        if(is_left_and_right_pop){
            //Si le meteore pop a gauche
            if(is_top_or_left_pop){     x= -w- randX; y= (float) (Math.random()*(HEIGHT)); }
            //Si le meteore pop a droite
            else{ x= WIDTH+w+randX; y= (float) (Math.random()*(HEIGHT)); }
        }
        else{
            //Si le meteore pop au top
            if(is_top_or_left_pop){     x= (int) (Math.random()*(HEIGHT)); y=-h-randY; }
            //Si le meteore pop a droite
            else{ x= (int) (Math.random()*(HEIGHT)); y=HEIGHT+h+randY; }
        }
        setPosition(x,y);
    }
    public void updatePosition() {
        float x= this.x;
        float y= this.y;

        double dx = game.vaisseau.x-x, dy = game.vaisseau.y-y;
        double dist = Math.pow(dx,2)+Math.pow(dy,2);

        double normx = dx/sqrt(dist);
        double normy = dy/sqrt(dist);

        float xPos = x + ( (float) ((normx)*speed ));
        float yPos = y + ( (float) ((normy)*speed ));

        setPosition(xPos, yPos);
    }
    public void updateSpeed(double sp){
        this.speed= (float) sp;
        setRotate( game.getRotateMs() );
    }
    public void initRotate(){
        rotate.setDuration(rotateMs);
        rotate.setRepeatCount(Animation.INFINITE); rotate.setInterpolator(new LinearInterpolator());
        startAnimation(rotate);
    }
    private void setRotate(int ms){ rotate.setDuration(ms); }
}

/*

https://openclassrooms.com/forum/sujet/java-2d-deplacer-un-personnage-d-un-point-a-un-autre-78794
https://codeday.me/bug/20180521/167079.html


 */
