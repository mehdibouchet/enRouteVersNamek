package com.rvn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class InGameActivity extends AppCompatActivity {
    public static Context mContext;
    public RelativeLayout gameLayout;
    private Game g;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.ingame_layout);



        gameLayout= findViewById(R.id.gameLayout);
        mContext= gameLayout.getContext();
        g= new Game(this);
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

    public void addView(ObjectView view){ gameLayout.addView(view); repaint();   }
    public void removeView(ObjectView view){ gameLayout.removeView(view); repaint(); }
    public void repaint(){ gameLayout.invalidate(); }
}
