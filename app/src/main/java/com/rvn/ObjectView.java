package com.rvn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static com.rvn.InGameActivity.mContext;


public abstract class ObjectView extends View {
    protected int h,w;
    protected int x,y;
    protected Bitmap img;
    protected Game game;

    private final Paint mPaint = new Paint();
    private final Point mSize = new Point();
    private final Point mStartPosition = new Point();

    private RelativeLayout.LayoutParams params;

    public ObjectView(Context context) {
        super(context);
    }
    public ObjectView(Context context, Game g, int img, int h, int w){
        super(context); init(g, img, h, w);
    }
    public ObjectView(Context context, Game g, int img, int h, int w, int x, int y){
        super(context); init(g, img, h, w, x, y);
    }

    public void init(Game g, int img, int h, int w){
        Bitmap image= BitmapFactory.decodeResource(getResources(), img);
        params= new RelativeLayout.LayoutParams(h, w);
        this.game= g;
        setImage(image);
        setSize(h, w);
        setBackgroundColor(0xFF00FF00);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        setPosition(metrics.widthPixels/2, metrics.heightPixels/2);
    }
    public void init(Game g, int img, int h, int w, int x, int y){
        init(g,img, h, w); setPosition(x,y);
    }

    protected void setImage(Bitmap img){ this.img= img; int w= img.getWidth(); int h= img.getHeight(); setSize(h,w); }
    protected void setImage(int img){ setImage(BitmapFactory.decodeResource(getResources(), img)); }
    protected void setSize(int h, int w){
        RelativeLayout.LayoutParams newParams= new RelativeLayout.LayoutParams( params );
        newParams.height= h; newParams.width= w;
        setLayoutParams(newParams);
        this.img= Bitmap.createScaledBitmap(img, w, h, false);
    }
    protected void setPosition(int x, int y){
        this.x= x; this.y= y;
        setX(x); setY(y);
    }

    public boolean hasCollision(Game g)                                   //méthode pour savoir si le vaisseau touche un météore
    {
        VaisseauView vaisseau= g.vaisseau;
        if(x+w/2>vaisseau.x-vaisseau.w/2
                && x-w/2<vaisseau.x+vaisseau.w/2
                && y+h/2>vaisseau.y-vaisseau.h/4
                && y-h/2<vaisseau.y+vaisseau.h/8 && !g.invincible)
        {
            g.state=false;
            return true;
        }
        return false;
    }

    public void repaint(){ this.invalidate(); }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawBitmap(img, 0, 0, mPaint);
    }
}

/*
    https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
 */
