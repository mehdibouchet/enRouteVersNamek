package com.rvn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class HighscoreActivity extends AppCompatActivity {
    private DatabaseAdapter dbAdapter;
    private ListView mListView;
    private HighscoreAdapter hsAdapter;
    private ImageView replayButton;

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        setContentView(R.layout.highscore_layout);

        dbAdapter= new DatabaseAdapter(this);
        dbAdapter.open();

        setView();

    }
    private void setView(){

        mListView = findViewById(R.id.highscoreList);
        registerForContextMenu(mListView);

        hsAdapter = new HighscoreAdapter(this, dbAdapter.getBestHighscore(), this);
        mListView.setAdapter(hsAdapter);

        replayButton= new ImageView(this);
        replayButton.setBackgroundResource(R.drawable.playagain);
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(800,400);
        replayButton.setLayoutParams(layoutParams);
        replayButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) { tryAgain(); }
        });
        //replayButton.align(Gravity.CENTER);


        LinearLayout replayLayout = new LinearLayout(this);
        replayLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        replayLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        replayLayout.addView(replayButton);

        mListView.addFooterView(replayLayout);
        //repaint();
    }

    public void removeScore(Highscore hs){
        removeScore(hs.getID());
    }
    public void removeScore(int hsID){
        dbAdapter.removeHighscore(hsID);
        hsAdapter = new HighscoreAdapter(this, dbAdapter.getBestHighscore(), this);
        mListView.setAdapter(hsAdapter);
        repaint();
    }

    private void tryAgain(){ Intent g= new Intent(this, InGameActivity.class); startActivity(g); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Highscore hs = hsAdapter.getItem(menuInfo.position);
        switch(item.getItemId()) {
            /*
            case R.id.replayButton:
                deleteScore(hs)
                return true;
            case R.id.action_quit:
                tryAgain();
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
    private void repaint(){ mListView.invalidate(); }
    @Override
    public void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }
}
