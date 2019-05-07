package com.rvn;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MainMenuActivity extends AppCompatActivity {
    public static int WIDTH, HEIGHT;
    public static String TAG="MainMenuActivity";
    @Override

    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.menu_main_layout);
        Log.d(TAG,Integer.toString(WIDTH));
    }

    public void onChangeView(View view) {
        Intent intent = new Intent();
        Log.d("TAG", "coucou"+Integer.toString(view.getId()));
        switch (view.getId()) {
            case R.id.startButton:
                intent.setClass(this, InGameActivity.class);
                startActivity(intent);
                break;
            case R.id.highscoreButton:
                intent.setClass(this, HighscoreActivity.class);
                startActivity(intent);
                break;
        }
    }
}
