package com.ridesharerental.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Find_Car_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by user65 on 1/6/2018.
 */

public class Discover_Recycler_adapter extends
        RecyclerView.Adapter<Discover_Recycler_adapter.ViewHolder>
{
    private ArrayList<Find_Car_Bean> ArrayLists;
    LayoutInflater mInflater;
    static Context context;
    Common_Loader loader;
    String str_timer = "";

    public Discover_Recycler_adapter(Context ctx, ArrayList<Find_Car_Bean> items) {
        this.ArrayLists = items;
        this.context = ctx;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_adapter_layout, parent, false);
        loader = new Common_Loader(context);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)

    {

        if(ArrayLists.get(position).getRating()!=null && !ArrayLists.get(position).getRating().equals(""))
        {
            holder.Car_Rating.setRating(Float.parseFloat(ArrayLists.get(position).getRating()));
        }


        holder.Tv_countryname.setText(ArrayLists.get(position).getCar_make()+" "
                +ArrayLists.get(position).getCar_model()+" "+ArrayLists.get(position).getYear());

        holder.txt_perday.setText("$"+ArrayLists.get(position).getRent_daily());
        holder.txt_per_week.setText("$"+ArrayLists.get(position).getRent_weekly());
        holder.txt_per_month.setText("$"+ArrayLists.get(position).getRent_monthly());

        if(ArrayLists.get(position).getCar_image()!=null && !ArrayLists.get(position).getCar_image().equals(""))
        {
            Picasso.with(context).load(ArrayLists.get(position).getCar_image()).fit().centerCrop()
                    .placeholder(R.drawable.placeholdercar)
                    .error(R.drawable.placeholdercar)
                    .into(holder.Iv_country_image);
        }
        else
        {
            Picasso.with(context).load(R.drawable.placeholdercar).fit().centerCrop()
                    .placeholder(R.drawable.placeholdercar)
                    .error(R.drawable.placeholdercar)
                    .into(holder.Iv_country_image);
        }


        if(ArrayLists.get(position).getProfile_pic()!=null && !ArrayLists.get(position).getProfile_pic().equals(""))
        {
            Picasso.with(context).load(ArrayLists.get(position).getProfile_pic()).fit().centerCrop()
                    .placeholder(R.drawable.icn_profile)
                    .error(R.drawable.icn_profile)
                    .into(holder.img_profile);
        }


        holder.itemView.setTag("");

    }

    @Override
    public int getItemCount() {
        return ArrayLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Tv_countryname;
        ImageView Iv_country_image;
        MaterialRatingBar Car_Rating;
        TextView txt_perday,txt_per_week,txt_per_month;
        CircleImageView img_profile;

        TextView txt_invoice;

        public ViewHolder(View itemView) {
            super(itemView);
            Tv_countryname=(TextView)itemView.findViewById(R.id.txt_country_name);
          Iv_country_image=(ImageView)itemView.findViewById(R.id.img_pro);
            Car_Rating=(MaterialRatingBar) itemView.findViewById(R.id.review_adapter_ratingBAR);
            txt_perday=(TextView)itemView.findViewById(R.id.day_price);
            txt_perday=(TextView)itemView.findViewById(R.id.day_price);
            txt_per_week=(TextView)itemView.findViewById(R.id.week_price);
           txt_per_month=(TextView)itemView.findViewById(R.id.month_price);
          img_profile=(CircleImageView)itemView.findViewById(R.id.map_img);


        }
    }

}
