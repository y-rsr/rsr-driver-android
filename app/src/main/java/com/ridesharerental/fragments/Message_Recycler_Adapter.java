package com.ridesharerental.fragments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Inbox_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by user110 on 5/15/2019.
 */

public class Message_Recycler_Adapter extends
        RecyclerView.Adapter<Message_Recycler_Adapter.ViewHolder>{
    public ArrayList<Inbox_Bean> ArrayLists;
    public ArrayList<Inbox_Bean> selected_usersList;
    public ArrayList<Inbox_Bean> temp_list;
    LayoutInflater mInflater;
    static Context context;
    Common_Loader loader;
    String str_timer = "";
    String str_show_bookingNO = "";

    public Message_Recycler_Adapter(Context ctx, ArrayList<Inbox_Bean> items,ArrayList<Inbox_Bean> selected_usersList,String str_show_bookingNO)
    {
        this.ArrayLists = items;
        this.str_show_bookingNO = str_show_bookingNO;
        this.selected_usersList = selected_usersList;
        temp_list = new ArrayList<>();
        temp_list.addAll(this.ArrayLists);
        this.context = ctx;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_adapter, parent, false);
        loader = new Common_Loader(context);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i)
    {
        if(ArrayLists.get(i).getSender_pic()!=null && !ArrayLists.get(i).getSender_pic().equals(""))
        {
            Picasso.with(context).load(ArrayLists.get(i).getSender_pic()).fit().centerCrop()
                    .placeholder(R.drawable.icn_profile)
                    .error(R.drawable.icn_profile)
                    .into(holder.profile_image);
        }


        if(ArrayLists.get(i).getSender_name()!=null && !ArrayLists.get(i).getSender_name().equals(""))
        {
            holder.txt_name.setText(ArrayLists.get(i).getSender_name());
        }


        if(ArrayLists.get(i).getCar_make()!=null && !ArrayLists.get(i).getCar_make().equals("") && !ArrayLists.get(i).getCar_make().equals(null) && !ArrayLists.get(i).getCar_make().equals("null") &&
                ArrayLists.get(i).getCar_model()!=null && !ArrayLists.get(i).getCar_model().equals("")  && !ArrayLists.get(i).getCar_model().equals(null) && !ArrayLists.get(i).getCar_model().equals("null") &&
                ArrayLists.get(i).getYear()!=null && !ArrayLists.get(i).getYear().equals("") && !ArrayLists.get(i).getYear().equals(null) && !ArrayLists.get(i).getYear().equals("null")  )
        {
            holder.txt_message.setText(ArrayLists.get(i).getCar_make()+" "+ArrayLists.get(i).getCar_model()+" "+ArrayLists.get(i).getYear());
        }
        else
        {
            holder.txt_message.setText(ArrayLists.get(i).getStr_message());
        }


        if(ArrayLists.get(i).getBooking_no()!=null && !ArrayLists.get(i).getBooking_no().equals("") && !ArrayLists.get(i).getBooking_no().equals(null) && !ArrayLists.get(i).getBooking_no().equals("null") && str_show_bookingNO.equalsIgnoreCase("no"))
        {
            holder.txt_date.setText(context.getResources().getString(R.string.booking_request)+ArrayLists.get(i).getBooking_no());
        }
        else
        {
            holder.txt_date.setText("");
        }

        if(ArrayLists.get(i).getDateAdded()!=null && !ArrayLists.get(i).getDateAdded().equals(""))
        {
            holder.txt_date_past.setText(ArrayLists.get(i).getDateAdded());
        }



        if(selected_usersList.contains(ArrayLists.get(i))) {
            holder.Rel_all.setBackground(ContextCompat.getDrawable(context, R.drawable.blur_effect));
            //holder.Rel_all.getBackground().setAlpha(10);
        }else
        {
            if(ArrayLists.get(i).getRead_status()!=null && !ArrayLists.get(i).getRead_status().equals(""))
            {
                if(ArrayLists.get(i).getRead_status().equalsIgnoreCase("Yes"))
                {
                    holder.Rel_all.setBackgroundColor(context.getResources().getColor(R.color.white_color));
                }
                else
                {
                    holder.Rel_all.setBackgroundColor(context.getResources().getColor(R.color.thin_light_another));
                }
            }
        }
        //holder.Rel_all.setBackgroundColor(ContextCompat.getColor(context, R.color.white_color));
        holder.itemView.setTag("");
    }
    @Override
    public int getItemCount()
    {
        return ArrayLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        //TextView Tv_car_name;
        CircleImageView profile_image;
        TextView txt_name,txt_date_past,txt_message,txt_date;
        RelativeLayout Rel_all;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_name=(TextView)itemView.findViewById(R.id.layout_inflate_inboxadapt_username);
            txt_date_past=(TextView)itemView.findViewById(R.id.layout_inflate_inboxadapt_datepast);
            txt_message=(TextView)itemView.findViewById(R.id.layout_inflate_inboxadapt_data);
            txt_date=(TextView)itemView.findViewById(R.id.layout_inflate_inboxadapt_statusdate);
            Rel_all=(RelativeLayout)itemView.findViewById(R.id.rel_allll);
            profile_image=(CircleImageView)itemView.findViewById(R.id.layout_inflate_inboxadapt_userprofile) ;


        }
    }


    public void filter(String letters)
    {
        String charactors = letters.toLowerCase(Locale.getDefault());
        ArrayLists.clear();
        if(charactors.length() == 0)
        {
            ArrayLists.addAll(temp_list);

        }else
        {
            for(Inbox_Bean inbox_bean:temp_list)
            {
                if((inbox_bean.getBooking_no() != null &&inbox_bean.getBooking_no().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getCar_make() != null && inbox_bean.getCar_make().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getCar_model() != null && inbox_bean.getCar_model().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getSender_name() != null && inbox_bean.getSender_name().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getDate_from() != null && inbox_bean.getDate_from().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getStr_message() != null &&inbox_bean.getStr_message().toLowerCase(Locale.getDefault()).contains(charactors)))
                    ArrayLists.add(inbox_bean);
            }
        }
        notifyDataSetChanged();
    }


}
