package com.rvn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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

        String nameText=  hs.getName(); //context.getString(R.string.name) + " " +hs.getName();
        String scoreText= Integer.toString(hs.getScore());//context.getString(R.string.score) + " " + hs.getScore();

        imageTextView.setImageResource(R.drawable.meteore);

        nameTextView.setText(nameText);     nameTextView.setGravity(Gravity.CENTER);
        nameTextView.setTextColor(Color.WHITE);
        nameTextView.setTextSize(25);  nameTextView.setGravity(Gravity.CENTER);

        scoreTextView.setText(scoreText);   scoreTextView.setGravity(Gravity.CENTER);
        scoreTextView.setTextColor(Color.WHITE);
        scoreTextView.setTextSize(20);  scoreTextView.setGravity(Gravity.CENTER);

        RotateAnimation rotate = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(3000);
        rotate.setRepeatCount(Animation.INFINITE); rotate.setInterpolator(new LinearInterpolator());
        imageTextView.startAnimation(rotate);
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
