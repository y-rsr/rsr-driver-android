package com.ridesharerental.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.CarDetailPage;
import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Find_Car_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by user65 on 1/8/2018.
 */

public class Map_Property_Adapter extends PagerAdapter
{
    LayoutInflater mInflater;
    Context ctx;
    ArrayList<Find_Car_Bean> arrayList;

    public Map_Property_Adapter(Context context, ArrayList<Find_Car_Bean> arraylist2)
    {
        this.mInflater = LayoutInflater.from(context);
        this.ctx = context;
        this.arrayList = arraylist2;
        System.out.println("--------ArrayData--------->"+arrayList.toString());
    }

    @Override
    public int getCount()
    {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position)
    {
       // final ViewHolder holder=new ViewHolder();
        View itemView = mInflater.inflate(R.layout.map_list_recycler_adapter, container, false);

        TextView MyExtentBTN = (TextView) itemView.findViewById(R.id.txt_country_name);
        ImageView Iv_country_image=(ImageView)itemView.findViewById(R.id.img_pro);
        MaterialRatingBar Car_Rating=(MaterialRatingBar) itemView.findViewById(R.id.review_adapter_ratingBAR);
        TextView txt_perday=(TextView)itemView.findViewById(R.id.day_price);
        TextView txt_per_week=(TextView)itemView.findViewById(R.id.week_price);
        TextView txt_per_month=(TextView)itemView.findViewById(R.id.month_price);
        CircleImageView img_profile=(CircleImageView)itemView.findViewById(R.id.map_img);
        TextView txt_marquee = (TextView) itemView.findViewById(R.id.txt_marq);
        RelativeLayout Rel_tag = (RelativeLayout) itemView.findViewById(R.id.rel_tag);





        if (arrayList.get(position).getVal_tag().toString() != null && !arrayList.get(position).getVal_tag().toString().equals("")) {
            Rel_tag.setVisibility(View.VISIBLE);

            String val=arrayList.get(position).getVal_tag().toString();
            //String val = "New Tag";
            //val="New Tag";
            if (val.toString().length() > 10) {
                txt_marquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                txt_marquee.setText("" + val);
                txt_marquee.setSelected(true);
                txt_marquee.setSingleLine(true);
            } else {

                txt_marquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                txt_marquee.setText("          " + val);
                txt_marquee.setSelected(true);
                txt_marquee.setSingleLine(true);
            }

        } else {
            Rel_tag.setVisibility(View.GONE);
        }


        ImageView image_car =  Iv_country_image;
        ImageView image_profile =  img_profile;
        if(arrayList.get(position).getCar_image()!=null && !arrayList.get(position).getCar_image().equals(""))
        {
            Picasso.with(ctx).load(arrayList.get(position).getCar_image()).fit().centerCrop()
                    .placeholder(R.drawable.placeholdercar)
                    .error(R.drawable.placeholdercar)
                    .into(image_car);
        }



        if(arrayList.get(position).getProfile_pic()!=null && !arrayList.get(position).getProfile_pic().equals(""))
        {
            Picasso.with(ctx).load(arrayList.get(position).getProfile_pic()).fit().centerCrop()
                    .placeholder(R.drawable.icn_profile)
                    .error(R.drawable.icn_profile)
                    .into(image_profile);
        }






        System.out.println("--------------------->"+arrayList.get(position).getRent_daily());
        System.out.println("--------------------->"+arrayList.get(position).getRent_daily());
        System.out.println("--------------------->"+arrayList.get(position).getRent_daily());
        System.out.println("--------------------->"+arrayList.get(position).getRent_daily());
        if(arrayList.get(position).getRating()!=null && !arrayList.get(position).getRating().equals(""))
        {
            Car_Rating.setRating(Float.parseFloat(arrayList.get(position).getRating()));
        }

        try {
            System.out.println("-----------Name ssssssssssss---------->"+arrayList.get(position).getCar_make()+" "
                    +arrayList.get(position).getCar_model()+" "+arrayList.get(position).getYear());
            MyExtentBTN.setText(arrayList.get(position).getCar_make()+" "
                    +arrayList.get(position).getCar_model()+" "+arrayList.get(position).getYear());
            txt_perday.setText("$"+arrayList.get(position).getRent_daily());
        txt_per_week.setText("$"+arrayList.get(position).getRent_weekly());
            txt_per_month.setText("$"+arrayList.get(position).getRent_monthly());

            System.out.println("-------------val-------------"+arrayList.get(position).getRent_daily());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                System.out.println("------------Click Position--------->"+position);
                Intent int_sub_dis = new Intent(ctx,CarDetailPage.class);
                int_sub_dis.putExtra("CarId",arrayList.get(position).getId().toString());
                int_sub_dis.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(int_sub_dis);
                // ((Activity) ctx).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        //container.setTag(holder);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((RelativeLayout) object);
    }

    public class ViewHolder
    {
        private TextView MyExtentBTN;
        ImageView Iv_country_image;
        MaterialRatingBar Car_Rating;
        TextView txt_perday,txt_per_week,txt_per_month;
        CircleImageView img_profile;

        TextView txt_marquee;
        RelativeLayout Rel_tag;
    }
}

