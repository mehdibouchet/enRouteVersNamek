package com.rvn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InGameActivity extends AppCompatActivity {
    public static Context mContext;
    public static int HEIGHT;
    public static int WIDTH;

    public RelativeLayout gameLayout;
    private Game g;
    private Handler handler;
    private TextView scoreView;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.ingame_layout);
        gameLayout= findViewById(R.id.gameLayout);

        WIDTH= Resources.getSystem().getDisplayMetrics().widthPixels;
        HEIGHT= Resources.getSystem().getDisplayMetrics().heightPixels;

       // WIDTH= getWindowManager().getDefaultDisplay().getWidth();

        mContext= gameLayout.getContext();

        handler= new Handler();
        g= new Game(this, handler);
        /*
        Timer timer= new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() { repaint(); }
            }, 00);*/
        }

    public void showEndGame(){
        Intent eg= new Intent(this, HighscoreActivity.class);
        startActivity(eg);
    }
    public static void resizeImage(ImageView image, int w, int h) {
        Drawable dr= image.getDrawable();
        w= pixelsToDp(w); h= pixelsToDp(h);
        Bitmap b = ((BitmapDrawable)dr).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, w, h, false);
        image.setImageBitmap(bitmapResized);
    }

    public static int dpToPixel(float dp){
        return (int)( dp * ((float) mContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT) );
    }
    public static int pixelsToDp(float px){
        return (int)( px / ((float) mContext.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT) );
    }

    public void addView(ObjectView view){ gameLayout.addView(view); }
    public void removeView(ObjectView view){ gameLayout.removeView(view); }
    public void showScore(){}
    public void repaint(){ gameLayout.invalidate(); }
}
