package com.rvn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.RelativeLayout;

import static com.rvn.InGameActivity.HEIGHT;
import static com.rvn.InGameActivity.WIDTH;


public abstract class ObjectView extends View {
    protected int h,w;
    protected float x,y;
    protected Bitmap img;
    protected Game game;

    private final Paint mPaint = new Paint();

    private RelativeLayout.LayoutParams params;

    public ObjectView(Context context) {
        super(context);
    }
    public ObjectView(Context context, Game g, int img, int h, int w){
        super(context); init(g, img, h, w);
    }
    public ObjectView(Context context, Game g, int img, int h, int w, float x, float y){
        super(context); init(g, img, h, w, x, y);
    }

    public void init(Game g, int img, int h, int w){
        Bitmap image= BitmapFactory.decodeResource(getResources(), img);
        params= new RelativeLayout.LayoutParams(h, w);
        this.game= g;
        setImage(image);
        setSize(h, w);
        setPosition((WIDTH-w)/2, (HEIGHT-2*h)/2);
    }
    public void init(Game g, int img, int h, int w, float x, float y){
        init(g,img, h, w); setPosition(x,y);
    }

    protected void setImage(Bitmap img){
        this.img= img;
        int w= img.getWidth(); int h= img.getHeight();
        setSize(h,w);
    }
    protected void setImage(int img){
        setImage(BitmapFactory.decodeResource(getResources(), img));
    }

    protected void setSize(int h, int w){
        RelativeLayout.LayoutParams newParams= new RelativeLayout.LayoutParams( params );
        newParams.height= h; newParams.width= w;
        setLayoutParams(newParams);
        this.img= Bitmap.createScaledBitmap(img, w, h, false);
        this.w= w; this.h= h;
    }
    protected void setPosition(float x, float y){
        this.x= x; this.y= y;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.params); //WRAP_CONTENT param can be FILL_PARENT
        params.leftMargin = (int) x; //XCOORD
        params.topMargin = (int) y; //YCOORD
        setLayoutParams(params);
    }

    public boolean hasCollision(){                                   //méthode pour savoir si le vaisseau touche un météore
        return hasCollision(game.vaisseau);
    }
    public boolean hasCollision(ObjectView m){                                   //méthode pour savoir si le vaisseau touche un météore
    float vx= m.x; float vy= m.y;
        int vw= m.w, vh= m.h;
        return x + w / 2 > vx - vw / 2
                && x - w / 2 < vx + vw / 2
                && y + h / 2 > vy - vh / 2
                && y - h / 2 < vy + vh / 2;
    }
    public boolean isOut(){
        return y - h / 2 > HEIGHT ||
                y + h / 2 < 0 ||
                x + w / 2 < 0 ||
                x - w / 2 > WIDTH;
    }

    public void repaint(){ this.invalidate(); }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawBitmap(img, 0, 0, mPaint);
    }
}

/*
    https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
    https://stackoverflow.com/questions/16782705/android-setx-and-sety-behaving-weird
 */
