package com.ridesharerental.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.Main_homepage;
import com.ridesharerental.app.R;
import com.ridesharerental.app.Review_Page;
import com.ridesharerental.pojo.Past_Reservation_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by user65 on 1/6/2018.
 */

public class Past_Reservation_Recycler_Adapter extends
        RecyclerView.Adapter<Past_Reservation_Recycler_Adapter.ViewHolder>
{
    private ArrayList<Past_Reservation_Bean> ArrayLists;
    LayoutInflater mInflater;
    public Context context;
    Common_Loader loader;
    public Past_Reservation_Recycler_Adapter(Context ctx, ArrayList<Past_Reservation_Bean> items)
    {
        this.ArrayLists = items;
        this.context = ctx;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_reservation_adapter, parent, false);
        loader=new Common_Loader(context);

        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {


        holder.txt_title.setText(ArrayLists.get(position).getCar_make()+" "+ArrayLists.get(position).getCar_model()+" "+ArrayLists.get(position).getYear());
        holder.txt_start_date.setText(ArrayLists.get(position).getDate_from());
        holder.txt_end_date.setText(ArrayLists.get(position).getDate_to());
        holder.txt_Host_Name.setText(ArrayLists.get(position).getOwnername());
        holder.txt_total_amt.setText("$"+ArrayLists.get(position).getTotal_amount());

        holder.txt_vin.setText(ArrayLists.get(position).getVin_no());
        holder.txt_plate.setText(ArrayLists.get(position).getPlat_no());
        holder.txt_make.setText(ArrayLists.get(position).getCar_make());
        holder.txt_model.setText(ArrayLists.get(position).getCar_model());
        holder.txt_year.setText(ArrayLists.get(position).getYear());
        System.out.println("------Status---------->"+ArrayLists.get(position).getStatus());

        if(ArrayLists.get(position).getStatus().toString()!=null  && !ArrayLists.get(position).getStatus().toString().equals(""))
        {
            holder.txt_status.setVisibility(View.VISIBLE);
            if(ArrayLists.get(position).getStatus().toString().equalsIgnoreCase("Declined"))
            {
                holder.txt_status.setText(ArrayLists.get(position).getStatus().toString());
                holder.txt_status.setBackgroundColor(context.getResources().getColor(R.color.red));
                holder.txt_reviews.setVisibility(View.GONE);
            }
            else
            {
                holder.txt_status.setText(ArrayLists.get(position).getStatus().toString());
                holder.txt_status.setBackgroundColor(context.getResources().getColor(R.color.orange_color));
                holder.txt_reviews.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.txt_status.setVisibility(View.GONE);
            holder.txt_reviews.setVisibility(View.GONE);
        }

        holder.txt_vehival_no.setText(ArrayLists.get(position).getV_no());
        holder.txt_notes.setText(ArrayLists.get(position).getNotes());



        holder.txt_my_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent val = new Intent(context, Main_homepage.class);
                val.putExtra("calling_type","transaction");
                context.startActivity(val);
            }
        });
        holder.Rel_Similar_Car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String str_car_id="",str_address="",str_car_make="",str_car_model="",str_car_year="";

                str_car_id=ArrayLists.get(position).getCarId();
                str_address=ArrayLists.get(position).getStreet();
                str_car_make=ArrayLists.get(position).getCar_make();
                str_car_model=ArrayLists.get(position).getCar_model();


                Intent val = new Intent(context, Main_homepage.class);
                val.putExtra("calling_type","findcar");
                val.putExtra("carid",str_car_id);
                val.putExtra("address",str_address);
                val.putExtra("car_make",str_car_make);
                val.putExtra("car_model",str_car_model);
                context.startActivity(val);
            }
        });

        holder.txt_reviews.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent val = new Intent(context, Review_Page.class);
                val.putExtra("booking_no",ArrayLists.get(position).getBooking_no());
                context.startActivity(val);
            }
        });

        if(ArrayLists.get(position).getCar_image()!=null && !ArrayLists.get(position).getCar_image().equals(""))
        {
            Picasso.with(context).load(ArrayLists.get(position).getCar_image())
                    .placeholder(R.drawable.placeholdercar)
                    .error(R.drawable.placeholdercar)
                    .into(holder.img_car_IMage);
        }


        if(ArrayLists.get(position).getProfile_pic()!=null && !ArrayLists.get(position).getProfile_pic().equals(""))
        {
            Picasso.with(context).load(ArrayLists.get(position).getProfile_pic())
                    .placeholder(R.drawable.icn_profile)
                    .error(R.drawable.icn_profile)
                    .into(holder.img_Profile);
        }

        holder.itemView.setTag("");
        //holder.type_title.setText(str_calling_title);
    }
    @Override
    public int getItemCount()
    {
        return ArrayLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_car_name;
        TextView txt_status;
        ImageView img_car_IMage;
        CircleImageView img_Profile;
        TextView txt_title;
        TextView txt_start_date,txt_end_date;
        TextView txt_Host_Name;
        TextView txt_total_amt;
        TextView txt_vin,txt_plate,txt_make,txt_model,txt_year;
        TextView txt_my_transaction;
        TextView txt_reviews;
        RelativeLayout Rel_Similar_Car;

        TextView txt_vehival_no,txt_notes;

        public ViewHolder(View itemView)
        {
            super(itemView);
            txt_car_name=(TextView)itemView.findViewById(R.id.txt_car_name);
            txt_status=(TextView)itemView.findViewById(R.id.txt_status);
            img_car_IMage=(ImageView)itemView.findViewById(R.id.img_car);
            img_Profile=(CircleImageView) itemView.findViewById(R.id.past_reservation_adapterprofileIMG);
            txt_title=(TextView)itemView.findViewById(R.id.title);
            txt_start_date=(TextView)itemView.findViewById(R.id.txt_start_date);
            txt_end_date=(TextView)itemView.findViewById(R.id.txt_end_date);
            txt_Host_Name=(TextView)itemView.findViewById(R.id.txt_host_name);
            txt_total_amt=(TextView)itemView.findViewById(R.id.txt_total_amt);
            txt_vin=(TextView)itemView.findViewById(R.id.txt_vin_val);
            txt_plate=(TextView)itemView.findViewById(R.id.txt_plat_no_value);
            txt_make=(TextView)itemView.findViewById(R.id.txt_make_value);
            txt_model=(TextView)itemView.findViewById(R.id.txt_model_value);
            txt_year=(TextView)itemView.findViewById(R.id.txt_year_value);
            txt_my_transaction=(TextView)itemView.findViewById(R.id.txt_my_Transactions);
            txt_reviews=(TextView)itemView.findViewById(R.id.txt_reviews);
            Rel_Similar_Car=(RelativeLayout)itemView.findViewById(R.id.rel_similar_cars);

            txt_vehival_no = (TextView) itemView.findViewById(R.id.txt_vehical_no);
            txt_notes = (TextView) itemView.findViewById(R.id.txt_notes);
        }
    }
}