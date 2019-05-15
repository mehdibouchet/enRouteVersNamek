package com.rvn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

public class HighscoreActivity extends AppCompatActivity {
    private DatabaseAdapter dbAdapter;
    private ListView mListView;
    private HighscoreAdapter hsAdapter;


    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);

        dbAdapter= new DatabaseAdapter(this);
        dbAdapter.open();
        
        setView();
    
        setContentView(R.layout.highscore_layout);
    }
    private void setView(){
        dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();

        mListView = findViewById(R.id.highscoreList);
        registerForContextMenu(mListView);

        hsAdapter = new HighscoreAdapter(this, dbAdapter.getBestHighscore());
        mListView.setAdapter(hsAdapter);
    }

    private void deleteScore(Highscore hs){
        deleteScore(hs.getID());
    }
    private void deleteScore(int hsID){
        dbAdapter.removeHighscore(hsID);
    }
    private void tryAgain(){
        Intent g= new Intent(this, InGameActivity.class);
    }
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

    @Override
    public void onDestroy() {
        dbAdapter.close();
        super.onDestroy();
    }
}
