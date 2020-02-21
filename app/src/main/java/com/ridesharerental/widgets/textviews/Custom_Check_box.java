package com.ridesharerental.widgets.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

public class Custom_Check_box extends CheckBox {

    public Custom_Check_box(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Custom_Check_box(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Custom_Check_box(Context context) {
        super(context);
        init();
    }


    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sofia Pro Regular.ttf");
        setTypeface(tf);
    }
}
