package com.rvn;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView scoreView, levelView, highscoreView;

    private Chronometer chronoView;
    private Timer timer;

    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener saveHSListener, confirmHSListener ;
    private EditText namePlayer;

    private DatabaseAdapter dbAdapter;

    public final static String DB= "com.rvn.DB";

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.ingame_layout);
        gameLayout= findViewById(R.id.gameLayout);

        WIDTH= Resources.getSystem().getDisplayMetrics().widthPixels;
        HEIGHT= Resources.getSystem().getDisplayMetrics().heightPixels;

        mContext= gameLayout.getContext();
        builder= new AlertDialog.Builder(mContext);

        handler= new Handler(Looper.getMainLooper());
        timer= new Timer();

        dbAdapter= new DatabaseAdapter(mContext);

        setGameViews();
        setListeners();

        g= new Game(this, handler, chronoView);
        startGame();
        }

    private void startGame(){
        g.startGame();
        showLevel(); showScore();
    }
    private void setListeners(){
        confirmHSListener= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: showSaveNameModal(); break;
                    case DialogInterface.BUTTON_NEGATIVE: showEndGameActivity(); break;
                }
            }
        };
        saveHSListener= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE: saveHighscore(); break;
                    case DialogInterface.BUTTON_NEGATIVE: break;
                }
                showEndGameActivity();
            }
        };
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
        chronoView.setFormat(getString(R.string.chrono)+ "\n %s"); chronoView.setTextColor(Color.WHITE);
        chronoView.setTextSize(20); chronoView.setGravity(Gravity.CENTER);
        chronoView.setX(WIDTH-200); chronoView.setY(pixelsToDp(10));
        gameLayout.addView(chronoView);
    }

    public void updateMeteoresPosition(){
        runOnUiThread(new java.util.TimerTask() {
            @Override
            public void run() {
                g.updateMeteoresPosition();
            }
        });
    }
    private void saveHighscore(){
        String name= namePlayer.getText().toString();
        Highscore highscore= new Highscore( name, g.getScore() );
        dbAdapter.open();
        dbAdapter.addHighscore(highscore);
    }

    public void showEndGameModal(){
        builder.setMessage(R.string.saveScore).setPositiveButton(R.string.yes, confirmHSListener)
                .setNegativeButton(R.string.no, confirmHSListener).show();
    }
    private void showSaveNameModal(){
        builder.setMessage(getString(R.string.saveName))
                .setPositiveButton(R.string.save,saveHSListener)
                .setNegativeButton(R.string.cancel, saveHSListener);

        namePlayer = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        namePlayer.setLayoutParams(lp);
        builder.setView(namePlayer);

        builder.show();
    }
    public void showEndGameActivity(){
        Intent eg= new Intent(this, HighscoreActivity.class);
        startActivity(eg);
    }



    public void showScore(){
        String text= getString(R.string.score)+"\n"+g.getScore(); scoreView.setText( text );
    }
    public void showLevel(){
        String text= getString(R.string.level)+"\n"+g.getLevel(); levelView.setText( text );
    }

    public void addView(ObjectView view){ gameLayout.addView(view); }
    public void removeView(ObjectView view){ gameLayout.removeView(view); }

    public void repaint(){
        runOnUiThread(new java.util.TimerTask() {
            @Override
            public void run() {
                gameLayout.invalidate();
                showScore(); showLevel();
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
https://stackoverflow.com/questions/14678593/the-application-may-be-doing-too-much-work-on-its-main-thread
https://stackoverflow.com/questions/47041396/only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-views

 */