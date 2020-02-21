package com.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by CAS61 on 11/1/2017.
 */
public class BoldCustomTextView extends TextView {

    public BoldCustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BoldCustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldCustomTextView(Context context) {
        super(context);
        init();
    }


    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sofia Pro Regular.ttf");
        setTypeface(tf);
    }
}
