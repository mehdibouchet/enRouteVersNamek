package com.rvn;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import static com.rvn.InGameActivity.*;
import static com.rvn.Functions.*;
import static java.lang.Math.abs;

public class Meteores extends ObjectView {
    public float speed, size;

    public Meteores(Game g, float sp, float size){
        super(mContext, g, R.drawable.meteore, (int) size*150, (int) size*150, 0, 0);
        this.size= size; this.speed= sp;
        //startRotation(3000);

        final Meteores met= this;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) { game.onClickMeteore(met ); }
        });

        //generatePosition();
    }
    public Meteores(Game g){  this(g,1,1);  }

    public void generatePosition(){
        //boolean is_left_and_right_pop=  true;//( ((int) (Math.random() * 4)) == 1);
        //boolean is_top_or_left_pop=     true;//( ((int) (Math.random() * 2)) == 1);
        /*
        if(is_left_and_right_pop){
            //Si le meteore pop a gauche
            if(is_top_or_left_pop){     x= -w; y=(int) (Math.random()*(HEIGHT)); }
            //Si le meteore pop a droite
            else{ x= WIDTH+w; y=(int) (Math.random()*(HEIGHT)); }
        }
        else{
            //Si le meteore pop au top
            if(is_top_or_left_pop){     x= (int) (Math.random()*(HEIGHT)); y=-h; }
            //Si le meteore pop a droite
            else{ x= (int) (Math.random()*(HEIGHT)); y=HEIGHT+h; }
        }*/
    }
    public void updatePosition() {
        int xPos= this.x;
        int yPos= this.y;
        int xvais = game.vaisseau.x;    float dx= xvais - this.x;
        int yvais = game.vaisseau.y;    float dy= yvais - this.y;
        float a= dy/dx;

        float x2= 1, y2= a;

        if(abs(y2)<1){ float b= 1/y2; x2*= b; y2= 1; }

        x2*= speed*1; y2*= speed*1;
        if( dx < 0) xPos-= Math.round(x2);
        else        xPos+= Math.round(x2);

        if( dy < 0) yPos-= Math.round(y2);
        else        yPos+= Math.round(y2);

        Log.i("CC", Float.toString(x2));
        Log.i("CC", Float.toString(y2));
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
        RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,
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