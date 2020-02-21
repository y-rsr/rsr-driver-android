package com.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by CAS61 on 11/7/2017.
 */
public class CustomButton extends Button {


    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        // TODO Auto-generated constructor stub
    }

    public CustomButton(Context context) {
        super(context);
        init();
        // TODO Auto-generated constructor stub
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        // TODO Auto-generated constructor stub
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sofia Pro Regular.ttf");
        setTypeface(tf);
    }
}
