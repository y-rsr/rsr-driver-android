package com.ridesharerental.widgets.textviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Custom_TextView extends TextView {

	public Custom_TextView(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public Custom_TextView(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public Custom_TextView(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Sofia Pro Regular.ttf");
	    setTypeface(tf);
	}
}
