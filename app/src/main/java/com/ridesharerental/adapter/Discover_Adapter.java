package com.ridesharerental.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Find_Car_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by user65 on 12/4/2017.
 */

public class Discover_Adapter extends BaseAdapter {
    LayoutInflater mInflater;
    Context context;
    ArrayList<Find_Car_Bean> City_ArrayList;
    String is_wishlist;
    OnListItemClick onListItemClick;
    public interface OnListItemClick
    {
        void OnWishClicked(String value);
    }

    private int angle = 0;

    public Discover_Adapter(Context context1, ArrayList<Find_Car_Bean> C_List, String is_wishlist) {
        this.context = context1;
        this.mInflater = LayoutInflater.from(context1);
        this.City_ArrayList = C_List;
        this.is_wishlist = is_wishlist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return City_ArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        // Count=Size of ArrayList.
        return 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder1 holder;
        if (convertView == null) {
            holder = new ViewHolder1();
            convertView = mInflater.inflate(R.layout.discover_adapter_layout, null);
            holder.Tv_countryname = (TextView) convertView.findViewById(R.id.txt_country_name);
            holder.Iv_country_image = (ImageView) convertView.findViewById(R.id.img_pro);
            holder.Car_Rating = (MaterialRatingBar) convertView.findViewById(R.id.review_adapter_ratingBAR);
            holder.txt_perday = (TextView) convertView.findViewById(R.id.day_price);
            holder.txt_perday = (TextView) convertView.findViewById(R.id.day_price);
            holder.txt_per_week = (TextView) convertView.findViewById(R.id.week_price);
            holder.txt_per_month = (TextView) convertView.findViewById(R.id.month_price);
            holder.img_profile = (CircleImageView) convertView.findViewById(R.id.map_img);

            holder.txt_vin_number = (TextView) convertView.findViewById(R.id.txt_vin_number);
            holder.txt_vehicle_number = (TextView) convertView.findViewById(R.id.txt_vehi_number);
            holder.txt_marquee = (TextView) convertView.findViewById(R.id.txt_marq);
            holder.Rel_tag = (RelativeLayout) convertView.findViewById(R.id.rel_tag);
            holder.rl_wishlist = (RelativeLayout) convertView.findViewById(R.id.rl_wishlist);
            holder.like_wish = (LikeButton) convertView.findViewById(R.id.like_wish);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder1) convertView.getTag();
        }


        if(is_wishlist.equalsIgnoreCase("yes")) {
            holder.rl_wishlist.setVisibility(View.VISIBLE);
            holder.like_wish.setLiked(true);
            holder.like_wish.setLikeDrawable(context.getResources().getDrawable(R.drawable.icn_unlike_icon));
        }
        else
            holder.rl_wishlist.setVisibility(View.GONE);

        if (City_ArrayList.get(position).getTag() != null && !City_ArrayList.get(position).getTag().equals("")) {
            holder.Rel_tag.setVisibility(View.VISIBLE);
//
              String val=City_ArrayList.get(position).getTag();


            holder.txt_marquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.txt_marquee.setSelected(true);
            holder.txt_marquee.setText(val);

//
//            if (val.length() > 5) {
//                holder.txt_marquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//                holder.txt_marquee.setText(" "+val);
//                holder.txt_marquee.setSelected(true);
//                holder.txt_marquee.setSingleLine(true);
//            } else {
//
//                holder.txt_marquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//                holder.txt_marquee.setText(val+"          ");
//                holder.txt_marquee.setSelected(true);
//                holder.txt_marquee.setSingleLine(true);
//            }
//
        } else {
            holder.Rel_tag.setVisibility(View.GONE);
        }







        holder.Car_Rating.setRating(Float.parseFloat(City_ArrayList.get(position).getRating()));


        holder.txt_vin_number.setText(City_ArrayList.get(position).getVin_no());
        holder.txt_vehicle_number.setText(context.getResources().getString(R.string.vehicle_number) + City_ArrayList.get(position).getV_no());


        holder.Tv_countryname.setText(City_ArrayList.get(position).getCar_make() + "\n"
                + City_ArrayList.get(position).getCar_model() + " " + City_ArrayList.get(position).getYear());

        holder.txt_perday.setText("$" + City_ArrayList.get(position).getRent_daily());
        holder.txt_per_week.setText("$" + City_ArrayList.get(position).getRent_weekly());
        holder.txt_per_month.setText("$" + City_ArrayList.get(position).getRent_monthly());


//        if (City_ArrayList.get(position).getCar_image() != null && !City_ArrayList.get(position).getCar_image().equals("")) {
        Picasso.with(context).load(City_ArrayList.get(position).getCar_image()).fit().centerCrop()
                .placeholder(R.drawable.placeholdercar)
                .error(R.drawable.placeholdercar)
                .into(holder.Iv_country_image);
//        } else {
//            Picasso.with(context).load(R.drawable.placeholdercar).fit().centerCrop()
//                    .placeholder(R.drawable.placeholdercar)
//                    .error(R.drawable.placeholdercar)
//                    .into(holder.Iv_country_image);
//        }


//        if (City_ArrayList.get(position).getProfile_pic() != null && !City_ArrayList.get(position).getProfile_pic().equals("")) {
        Picasso.with(context).load(City_ArrayList.get(position).getProfile_pic()).fit().centerCrop()
                .placeholder(R.drawable.icn_profile)
                .error(R.drawable.icn_profile)
                .into(holder.img_profile);
//        }

        holder.like_wish.setOnLikeListener(new OnLikeListener()
        {
            @Override
            public void liked(LikeButton likeButton)
            {
                onListItemClick.OnWishClicked(City_ArrayList.get(position).getId());

            }

            @Override
            public void unLiked(LikeButton likeButton)
            {
                onListItemClick.OnWishClicked(City_ArrayList.get(position).getId());

            }
        });

        return convertView;
    }


    class ViewHolder1 {
        TextView Tv_countryname,txt_week_offer,txt_month_offer;
        ImageView Iv_country_image;
        MaterialRatingBar Car_Rating;
        TextView txt_perday, txt_per_week, txt_per_month;
        CircleImageView img_profile;

        TextView txt_vin_number, txt_vehicle_number;
        TextView txt_marquee;
        LikeButton like_wish;
        RelativeLayout Rel_tag,rl_wishlist,week_offer_ll,month_offer_ll;
    }

    public void setOnListItemClick(OnListItemClick onListItemClick)
    {
        this.onListItemClick = onListItemClick;
    }

}
