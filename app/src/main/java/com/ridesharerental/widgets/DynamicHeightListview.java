package com.ridesharerental.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by CAS61 on 10/10/2017.
 */
public class DynamicHeightListview extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int old_count = 0;
    Context ctx;
    boolean expanded = false;

    public DynamicHeightListview(Context context) {
        super(context);
        this.ctx=context;

    }

    public boolean isExpanded()
    {
        return expanded;
    }


    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    public DynamicHeightListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public DynamicHeightListview(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }



    @Override
    protected void onDraw(Canvas canvas)
    {
        try {
            if (getCount() != old_count)
            {
                params=new LayoutParams(params);
                old_count = getCount();
                if(params!=null)
                {
                    params = getLayoutParams();
                    params.height = getCount() * (old_count > 0 ? getChildAt(0).getHeight() + 0 : 0);
                    setLayoutParams(params);

                }

            }
        }catch (Exception e)
        {

        }
        super.onDraw(canvas);
    }




    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded())
        {
            // Calculate entire height by providing a very large height hint.
            // But do not use the highest 2 bits of this integer; those are
            // reserved for the MeasureSpec mode.

            int expandSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
