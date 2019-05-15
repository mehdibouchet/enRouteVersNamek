package com.rvn;

import android.content.Context;
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

    public HighscoreAdapter(Context context, ArrayList<Highscore> values) {
        super(context, R.layout.highscore_cell_layout, values);
        this.context = context;
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
        cellView.setTag(hs.getID());
        String nameText= context.getString(R.string.saveName) + " " +hs.getName();
        String scoreText= context.getString(R.string.score) + " " + hs.getScore();

        nameTextView.setText(nameText);
        scoreTextView.setText(scoreText);
        imageTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                del
            }
        });
        return cellView;
    }
}
