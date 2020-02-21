package com.ridesharerental.widgets.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by CAS59 on 12/4/2017.
 */

public class Custom_Buttton_Bold extends Button{
    public Custom_Buttton_Bold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Custom_Buttton_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Custom_Buttton_Bold(Context context) {
        super(context);
        init();
    }


    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sofia Pro Bold.ttf");
        setTypeface(tf);
    }
}
