package com.ridesharerental.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.FeaturePojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CAS59 on 1/6/2018.
 */

public class FeatureCarDetailPage_Adapter extends
        RecyclerView.Adapter<FeatureCarDetailPage_Adapter.ViewHolder> {



    public Context context;
    ArrayList<FeaturePojo> myfeatureARR;
    RecyclerView myRecycleList;
    public FeatureCarDetailPage_Adapter(Context ctx, ArrayList<FeaturePojo> featureARR, RecyclerView RecycleList) {
        this.context = ctx;
        this.myfeatureARR=featureARR;
        this.myRecycleList = RecycleList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_detailspage_adapter, parent, false);


        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        if(myfeatureARR.get(position).getFeatureName()!=null)
        {
            System.out.println("--getName-->"+ myfeatureARR.get(position).getFeatureName());
            holder.txt_feature_name.setText(myfeatureARR.get(position).getFeatureName().toString());
        }

        if(myfeatureARR.get(position).getFeatureImage()!=null && !myfeatureARR.get(position).getFeatureImage().equals(""))
        {
            Picasso.with(context)
                    .load(myfeatureARR.get(position).getFeatureImage())
                    .placeholder(context.getResources().getDrawable(R.drawable.placeholdercar))
                    .into(holder.img_feature);
        }


        Display display =  ((Activity)context).getWindowManager().getDefaultDisplay();
        if (myfeatureARR.size() == 1) {
            System.out.println("--------------jai-----------category size"+myfeatureARR.size());
            ViewGroup.LayoutParams params = holder.Ll_car.getLayoutParams();
            params.width = (display.getWidth()) - 8;
            holder.Ll_car.setLayoutParams(params);
        } else if (myfeatureARR.size() == 2) {
            System.out.println("--------------jai-----------category size"+myfeatureARR.size());

            ViewGroup.LayoutParams params = holder.Ll_car.getLayoutParams();
            params.width = (display.getWidth() / 2) - 8;
            holder.Ll_car.setLayoutParams(params);
        }
        else if (myfeatureARR.size() == 3) {
            System.out.println("--------------jai-----------category size"+myfeatureARR.size());
            ViewGroup.LayoutParams params = holder.Ll_car.getLayoutParams();
            params.width = (display.getWidth() / 3) - 8;
            holder.Ll_car.setLayoutParams(params);
        }else {
            System.out.println("--------------jai-----------category size"+myfeatureARR.size());
            ViewGroup.LayoutParams params = holder.Ll_car.getLayoutParams();
            params.width = (display.getWidth() / 4) - 8;
            holder.Ll_car.setLayoutParams(params);
        }



    }

    @Override
    public int getItemCount() {
        return myfeatureARR.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_feature;
        public TextView txt_feature_name;
        LinearLayout Ll_car;

        public ViewHolder(View itemView) {
            super(itemView);
            img_feature = (ImageView) itemView.findViewById(R.id.recycle_feature_img);
            txt_feature_name = (TextView) itemView.findViewById(R.id.recycle_feature_name);
            Ll_car=(LinearLayout)itemView.findViewById(R.id.linear_item);
        }
    }

}


