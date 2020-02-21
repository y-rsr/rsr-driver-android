package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharerental.app.R;

import java.util.ArrayList;

/**
 * Created by user65 on 11/28/2017.
 */

public class Menu_Adapter extends BaseAdapter
{
    Context context;
   ArrayList<String> mTitle;
    int[] mIcon;
    LayoutInflater inflater;
    Animation Ani_Show;

    public Menu_Adapter(Context context, ArrayList<String> title)
    {
        this.context = context;
        this.mTitle = title;
    }

    @Override
    public int getCount()
    {
        return mTitle.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Declare Variables
        TextView txtTitle;
        ImageView imgIcon;


        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.menu_item, parent, false);
        Ani_Show = AnimationUtils.loadAnimation(context, R.anim.show_view);

        // Locate the TextViews in drawer_list_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.menu_title);

        // Set the results into TextViews
        txtTitle.setText(mTitle.get(position).toString());
        txtTitle.startAnimation(Ani_Show);


        // Set the results into ImageView
        //imgIcon.setImageResource(mIcon[position]);

        return itemView;
    }
}
