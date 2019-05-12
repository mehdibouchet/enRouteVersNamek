package com.rvn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;

public class InGameActivity extends AppCompatActivity {
    public static Context mContext;
    public static float HEIGHT;
    public static float WIDTH;

    public RelativeLayout gameLayout;
    private Game g;
    private Handler handler;
    private TextView scoreView, levelView;
    private Chronometer chronoView;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.ingame_layout);
        gameLayout= findViewById(R.id.gameLayout);

        WIDTH= Resources.getSystem().getDisplayMetrics().widthPixels;
        HEIGHT= Resources.getSystem().getDisplayMetrics().heightPixels;

        mContext= gameLayout.getContext();

        handler= new Handler(Looper.getMainLooper());
        timer= new Timer();
        setGameViews();

        g= new Game(this, handler, chronoView);
        startGame();

        }

    private void startGame(){
        g.startGame();
        showLevel(); showScore();
    }
    public void setGameViews(){
        scoreView= new TextView(this);
        scoreView.setText(R.string.chrono); scoreView.setTextColor(Color.WHITE);
        scoreView.setTextSize(20);  scoreView.setGravity(Gravity.CENTER);
        scoreView.setX(pixelsToDp(50) ); scoreView.setY(10);
        gameLayout.addView(scoreView);

        levelView= new TextView(this);
        levelView.setText(R.string.level); levelView.setTextColor(Color.WHITE);
        levelView.setTextSize(25);  levelView.setGravity(Gravity.CENTER);
        levelView.setX(WIDTH/2 - 100); levelView.setY(10);
        gameLayout.addView(levelView);

        chronoView= new Chronometer(this);
        chronoView.setFormat("Temps \n %s"); chronoView.setTextColor(Color.WHITE);
        chronoView.setTextSize(20); chronoView.setGravity(Gravity.CENTER);
        chronoView.setX(WIDTH-200); chronoView.setY(pixelsToDp(10));
        gameLayout.addView(chronoView);
    }
    public void showEndGame(){
        Intent eg= new Intent(this, HighscoreActivity.class);
        startActivity(eg);
    }

    public void addView(ObjectView view){ gameLayout.addView(view); }
    public void removeView(ObjectView view){ gameLayout.removeView(view); }

    public void showScore(){
        scoreView.setText( "Score\n"+g.getScore() );
    }
    public void showLevel(){
        levelView.setText( "Level\n"+g.getLevel() );
    }
    public void repaint(){
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                gameLayout.invalidate();
                showScore(); showLevel();
            }
         });
    }

    public void updateMeteoresPosition(){
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                g.updateMeteoresPosition();
            }
        });
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

}

/*

https://abhiandroid.com/ui/chronometer

 */