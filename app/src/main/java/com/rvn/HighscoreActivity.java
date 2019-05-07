package com.rvn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class HighscoreActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        showHighscore();

        setContentView(R.layout.highscore_layout);
    }
    private void showHighscore(){}
}
