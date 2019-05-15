package com.rvn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HighscoreAdapter extends ArrayAdapter<Highscore> {

    private final Context context;
    private TextView nameTextView, scoreTextView;
    private ImageView imageTextView;
    private HighscoreActivity parent;

    public HighscoreAdapter(Context context, ArrayList<Highscore> values, HighscoreActivity parent) {
        super(context, R.layout.highscore_cell_layout, values);
        this.context = context; this.parent= parent;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;

        if (cellView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cellView = inflater.inflate(R.layout.highscore_cell_layout, parent, false);
        }

        nameTextView =  cellView.findViewById(R.id.highscoreName);
        scoreTextView = cellView.findViewById(R.id.highscoreScore);
        imageTextView = cellView.findViewById(R.id.highscoreImage);

        Highscore hs = getItem(position);
        final HighscoreActivity actParent= this.parent;
        final int ID= hs.getID();

        String nameText= context.getString(R.string.name) + " " +hs.getName();
        String scoreText= context.getString(R.string.score) + " " + hs.getScore();

        imageTextView.setImageResource(R.drawable.meteore);

        nameTextView.setText(nameText);     nameTextView.setGravity(Gravity.CENTER);
        nameTextView.setTextColor(Color.WHITE);
        nameTextView.setTextSize(25);  nameTextView.setGravity(Gravity.CENTER);

        scoreTextView.setText(scoreText);   scoreTextView.setGravity(Gravity.CENTER);
        scoreTextView.setTextColor(Color.WHITE);
        scoreTextView.setTextSize(25);  scoreTextView.setGravity(Gravity.CENTER);

        imageTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                actParent.removeScore(ID);
            }
        });
        cellView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return cellView;
    }
}
