package com.ridesharerental.temp_code;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;

import static com.ridesharerental.app.R.id.ew_tv_node_key;

/**
 * Created by user65 on 2/23/2018.
 */

public class Tree_Item extends LinearLayout
{
    public LinearLayout rootLayout;
    private TextView tvNodeKey;
    public Tree_Item(Context context)
    {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.smple_tree_layout, this, true);
        initView();
    }

    public Tree_Item(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void initView()
    {
        rootLayout = (LinearLayout) getRootView();
        tvNodeKey = (TextView) this.findViewById(ew_tv_node_key);

    }
    public void my_set_data(String name,Boolean isExpend)
    {
        tvNodeKey.setText(name);

        View childView = rootLayout.getChildAt(2);
        if (childView != null)
        {
            childView.setVisibility(isExpend ? VISIBLE : GONE);
        }
    }
}
