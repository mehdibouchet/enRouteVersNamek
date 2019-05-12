package com.rvn;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import static com.rvn.InGameActivity.HEIGHT;
import static com.rvn.InGameActivity.WIDTH;
import static com.rvn.InGameActivity.mContext;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Meteores extends ObjectView {
    public float speed, size;

    public Meteores(Game g, float sp, float size){
        super(mContext, g, R.drawable.meteore, (int) size*150, (int) size*150);
        this.size= size; this.speed= sp;
        startRotation(4000);

        final Meteores met= this;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) { clearAnimation(); game.onClickMeteore(met); }
        });

        generatePosition();
    }
    public Meteores(Game g){  this(g,1,1);  }

    public void generatePosition(){
        boolean is_left_and_right_pop=  true;//( ((int) (Math.random() * 3)) == 1);
        boolean is_top_or_left_pop=     ( ((int) (Math.random() * 2)) == 1);
        float x,y;
        if(is_left_and_right_pop){
            //Si le meteore pop a gauche
            if(is_top_or_left_pop){     x= -w; y= (float) (Math.random()*(HEIGHT)); }
            //Si le meteore pop a droite
            else{ x= WIDTH+w; y= (float) (Math.random()*(HEIGHT)); }
        }
        else{
            //Si le meteore pop au top
            if(is_top_or_left_pop){     x= (int) (Math.random()*(HEIGHT)); y=-h; }
            //Si le meteore pop a droite
            else{ x= (int) (Math.random()*(HEIGHT)); y=HEIGHT+h; }
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
    public boolean isOut(){
        if( y-h/2 > HEIGHT ||
                y+h/2 < 0 ||
                x+w/2 < 0 ||
                x-w/2 > WIDTH)
            return true;
        return false;
    }

    public void startRotation(int ms){
        RotateAnimation rotate = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(ms);
        rotate.setRepeatCount(Animation.INFINITE); rotate.setInterpolator(new LinearInterpolator());
        startAnimation(rotate);
    }
}

/*

https://codeday.me/bug/20180521/167079.html


 */