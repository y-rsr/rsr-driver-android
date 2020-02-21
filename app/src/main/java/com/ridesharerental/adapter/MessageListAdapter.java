package com.ridesharerental.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Inbox_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user110 on 3/26/2019.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder>{
    private ArrayList<Inbox_Bean> My_InboxARR;
    private List<String> selectedIds = new ArrayList<>();
    private Context Mycontext;
    private LayoutInflater myinflater;

    public MessageListAdapter(FragmentActivity activity, ArrayList<Inbox_Bean> myInboxARR) {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_InboxARR = myInboxARR;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = myinflater.inflate(R.layout.layout_inflator_inboxadapt, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String id = My_InboxARR.get(i).getBooking_no();
        if(My_InboxARR.get(i).getSender_pic()!=null && !My_InboxARR.get(i).getSender_pic().equals(""))
        {
            Picasso.with(Mycontext).load(My_InboxARR.get(i).getSender_pic()).fit().centerCrop()
                    .placeholder(R.drawable.icn_profile)
                    .error(R.drawable.icn_profile)
                    .into(holder.profile_image);
        }
        if(My_InboxARR.get(i).getSender_name()!=null && !My_InboxARR.get(i).getSender_name().equals(""))
        {
            holder.txt_name.setText(My_InboxARR.get(i).getSender_name());
        }

        if(My_InboxARR.get(i).getCar_make()!=null && !My_InboxARR.get(i).getCar_make().equals("") && !My_InboxARR.get(i).getCar_make().equals(null) && !My_InboxARR.get(i).getCar_make().equals("null") &&
                My_InboxARR.get(i).getCar_model()!=null && !My_InboxARR.get(i).getCar_model().equals("")  && !My_InboxARR.get(i).getCar_model().equals(null) && !My_InboxARR.get(i).getCar_model().equals("null") &&
                My_InboxARR.get(i).getYear()!=null && !My_InboxARR.get(i).getYear().equals("") && !My_InboxARR.get(i).getYear().equals(null) && !My_InboxARR.get(i).getYear().equals("null")  )
        {
            holder.txt_message.setText(My_InboxARR.get(i).getCar_make()+" "+My_InboxARR.get(i).getCar_model()+" "+My_InboxARR.get(i).getYear());
        }
        else
        {
            holder.txt_message.setText("");
        }

        if(My_InboxARR.get(i).getBooking_no()!=null && !My_InboxARR.get(i).getBooking_no().equals("") && !My_InboxARR.get(i).getBooking_no().equals(null) && !My_InboxARR.get(i).getBooking_no().equals("null"))
        {
            holder.txt_date.setText(Mycontext.getResources().getString(R.string.booking_request)+My_InboxARR.get(i).getBooking_no());
        }
        else
        {
            holder.txt_date.setText("");
        }

        if(My_InboxARR.get(i).getDateAdded()!=null && !My_InboxARR.get(i).getDateAdded().equals(""))
        {
            holder.txt_date_past.setText(My_InboxARR.get(i).getDateAdded());
        }


        if(My_InboxARR.get(i).getRead_status()!=null && !My_InboxARR.get(i).getRead_status().equals(""))
        {
            if(My_InboxARR.get(i).getRead_status().equalsIgnoreCase("Yes"))
            {
                holder.Rel_all.setBackgroundColor(Mycontext.getResources().getColor(R.color.white_color));
            }
            else
            {
                holder.Rel_all.setBackgroundColor(Mycontext.getResources().getColor(R.color.thin_light_another));
            }
        }

        if (selectedIds.contains(id)){
            //if item is selected then,set foreground color of FrameLayout.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(Mycontext,R.color.colorControlActivated)));
        }
        else {
            //else remove selected item color.
            holder.rootView.setForeground(new ColorDrawable(ContextCompat.getColor(Mycontext,android.R.color.transparent)));
        }

    }

    @Override
    public int getItemCount() {
        return My_InboxARR.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_image;
        TextView txt_name,txt_date_past,txt_message,txt_date;
        RelativeLayout Rel_all,rootView;
        MyViewHolder(View view) {
            super(view);
            profile_image=(CircleImageView)view.findViewById(R.id.layout_inflate_inboxadapt_userprofile) ;
            txt_name=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_username);
            txt_date_past=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_datepast);
            txt_message=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_data);
            txt_date=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_statusdate);
            Rel_all=(RelativeLayout)view.findViewById(R.id.rel_allll);
            rootView=(RelativeLayout)view.findViewById(R.id.rootView);
        }
    }

    public Inbox_Bean getItem(int position){
        return My_InboxARR.get(position);
    }

    public void setSelectedIds(List<String> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }
}
