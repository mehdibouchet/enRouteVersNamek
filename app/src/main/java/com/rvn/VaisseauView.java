package com.rvn;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import static com.rvn.InGameActivity.mContext;

public class VaisseauView extends ObjectView {

    public VaisseauView(Context context) {
        super(context);
    }
    public VaisseauView(){ super(mContext); }
    public VaisseauView(Game g){
        super(mContext, g, R.drawable.vaisseau2, 200, 200);
    }

}
