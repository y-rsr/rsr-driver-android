package com.ridesharerental.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Inbox_Bean;
import com.ridesharerental.widgets.CircleImageView;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class Wishlist_Adapter extends BaseAdapter {

    private ArrayList<Inbox_Bean> My_InboxARR;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Wishlist_Adapter(FragmentActivity activity, ArrayList<Inbox_Bean> myInboxARR) {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_InboxARR = myInboxARR;
    }

    @Override
    public int getCount() {
        return My_InboxARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_InboxARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolderInbox holder;

        if (convertView == null) {
            holder = new ViewHolderInbox();
            convertView = myinflater.inflate(R.layout.wishlist_adapter_layout, null);
            holder.Tv_countryname=(TextView)convertView.findViewById(R.id.txt_country_name);
            holder.Iv_country_image=(ImageView)convertView.findViewById(R.id.img_pro);
            holder.Car_Rating=(MaterialRatingBar) convertView.findViewById(R.id.review_adapter_ratingBAR);
            holder.txt_perday=(TextView)convertView.findViewById(R.id.day_price);
            holder.txt_perday=(TextView)convertView.findViewById(R.id.day_price);
            holder.txt_per_week=(TextView)convertView.findViewById(R.id.week_price);
            holder.txt_per_month=(TextView)convertView.findViewById(R.id.month_price);
            holder.img_profile=(CircleImageView)convertView.findViewById(R.id.map_img);

            holder.txt_vin_number=(TextView)convertView.findViewById(R.id.txt_vin_number);
            holder.txt_vehicle_number=(TextView)convertView.findViewById(R.id.txt_vehi_number);



            convertView.setTag(holder);

        } else {
            holder = (ViewHolderInbox) convertView.getTag();
        }





        return convertView;
    }


    class ViewHolderInbox
    {
        TextView Tv_countryname;
        ImageView Iv_country_image;
        MaterialRatingBar Car_Rating;
        TextView txt_perday,txt_per_week,txt_per_month;
        CircleImageView img_profile;

        TextView txt_vin_number,txt_vehicle_number;
    }
}