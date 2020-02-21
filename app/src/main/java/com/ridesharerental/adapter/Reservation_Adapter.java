package com.ridesharerental.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.app.Reservation_Extent;
import com.ridesharerental.pojo.Reservation_pojo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CAS59 on 12/5/2017.
 */

public class Reservation_Adapter extends BaseAdapter
{


    private ArrayList<Reservation_pojo> My_reservationARR ;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Reservation_Adapter(Context mycontext, ArrayList<Reservation_pojo> my_reservationARR)
    {
        this.Mycontext = mycontext;
        this.myinflater = LayoutInflater.from(mycontext);
        this.My_reservationARR = my_reservationARR;
       // notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return My_reservationARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_reservationARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       final  viewHolder holder;

        if (view == null) {
            holder = new viewHolder();
            view = myinflater.inflate(R.layout.layout_inflator_reservation, null);
            holder.MyExtentBTN = (TextView) view.findViewById(R.id.layout_inflator_reservation_extentBTN);
            holder.rel_details=(RelativeLayout)view.findViewById(R.id.rel_details);
            holder.img_car=(ImageView)view.findViewById(R.id.img_car);

            view.setTag(holder);

        } else {
            holder = (viewHolder) view.getTag();
        }



        Picasso.with(Mycontext).load(R.drawable.placeholdercar)
                .placeholder(R.drawable.placeholdercar)
                .error(R.drawable.placeholdercar)
                .into(holder.img_car);

        holder.MyExtentBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent val = new Intent(Mycontext,Reservation_Extent.class);
                Mycontext.startActivity(val);
            }
        });



        holder.rel_details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopUp();
            }
        });
        return view;
    }


    class viewHolder
    {

        private TextView MyExtentBTN;
        private RelativeLayout rel_details;
        private ImageView img_car;

    }




    public void showPopUp()
    {
        final Dialog dialog = new Dialog(Mycontext);
        dialog.setContentView(R.layout.reservation_details_document);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout Rel_close=(RelativeLayout)dialog.findViewById(R.id.rel_close);
        dialog.show();
        Rel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });


    }
}
