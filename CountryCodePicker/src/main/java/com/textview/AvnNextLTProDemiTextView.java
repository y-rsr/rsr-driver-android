package com.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by CAS56 on 6/21/2017.
 */

public class AvnNextLTProDemiTextView extends TextView {

    public AvnNextLTProDemiTextView(Context context) {
        super(context);
        init();
    }

    public AvnNextLTProDemiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvnNextLTProDemiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sofia Pro Regular.ttf");
        setTypeface(tf);

    }
}
