package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ridesharerental.app.CarDetailPage;
import com.ridesharerental.app.R;
import com.ridesharerental.pojo.ReviewPojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CAS59 on 12/7/2017.
 */

public class Review_Adapter extends BaseAdapter {

    private ArrayList<ReviewPojo> My_reviewARR;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Review_Adapter(CarDetailPage carDetailPage, ArrayList<ReviewPojo> myreviewARR) {


        this.Mycontext = carDetailPage;
        this.myinflater = LayoutInflater.from(carDetailPage);
        this.My_reviewARR = myreviewARR;

    }



    @Override
    public int getCount() {
        return My_reviewARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_reviewARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolderReview holder;

        if (view == null) {
            holder = new ViewHolderReview();
            view = myinflater.inflate(R.layout.review_adapter, null);
            holder.img_profile=(ImageView)view.findViewById(R.id.review_adapterprofileIMG);
            holder.User_rating=(RatingBar)view.findViewById(R.id.review_adapter_ratingBAR);
            holder.txt_user_name=(TextView)view.findViewById(R.id.review_adapterusernameTXT);
            holder.txt_user_review_desc=(TextView)view.findViewById(R.id.review_adapteruse_decTXT);


            view.setTag(holder);

        } else {
            holder = (ViewHolderReview) view.getTag();
        }


        Picasso.with(Mycontext).load(My_reviewARR.get(i).getProfileImage()).fit().centerCrop()
                .placeholder(R.drawable.icn_profile)
                .error(R.drawable.icn_profile)
                .into(holder.img_profile);
        try {
            Float rate = Float.valueOf(My_reviewARR.get(i).getRating());
            if (rate != null) {
                holder.User_rating.setRating(rate);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        holder.txt_user_name.setText(My_reviewARR.get(i).getName());
        holder.txt_user_review_desc.setText(My_reviewARR.get(i).getInfo());


        return view;
    }


    class ViewHolderReview
    {
            ImageView img_profile;
            RatingBar User_rating;
            TextView txt_user_name,txt_user_review_desc;
    }


}
