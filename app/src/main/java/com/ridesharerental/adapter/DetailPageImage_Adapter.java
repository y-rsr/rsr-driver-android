package com.ridesharerental.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ridesharerental.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CAS59 on 1/5/2018.
 */

public class DetailPageImage_Adapter extends PagerAdapter{

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> myCarImages;

    public DetailPageImage_Adapter(Context carDetailPage, ArrayList<String> CarImages) {
       this.mContext=carDetailPage;
        this.myCarImages = CarImages;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return myCarImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        View itemView = mLayoutInflater.inflate(R.layout.detailpage_image_adapter, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        if (myCarImages.get(position)!=null)
        {
            Picasso.with(mContext)
                    .load(myCarImages.get(position))
                    .placeholder(R.drawable.placeholdercar)
                    .fit()
                    .into(imageView);

        }


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
