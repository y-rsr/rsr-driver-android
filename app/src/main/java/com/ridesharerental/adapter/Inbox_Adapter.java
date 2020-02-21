package com.ridesharerental.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Inbox_Bean;
import com.ridesharerental.widgets.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Inbox_Adapter extends BaseAdapter {

    private ArrayList<Inbox_Bean> My_InboxARR;

    private Context Mycontext;
    private LayoutInflater myinflater;
    private SparseBooleanArray mSelectedItemsIds;
    public ArrayList<Inbox_Bean> temp_list;
    public Inbox_Adapter(FragmentActivity activity, ArrayList<Inbox_Bean> myInboxARR) {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_InboxARR = myInboxARR;
        temp_list = new ArrayList<>();
        temp_list.addAll(this.My_InboxARR);
        mSelectedItemsIds = new SparseBooleanArray();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolderInbox holder;

        if (view == null) {
            holder = new ViewHolderInbox();
            view = myinflater.inflate(R.layout.layout_inflator_inboxadapt, null);
            holder.profile_image=(CircleImageView)view.findViewById(R.id.layout_inflate_inboxadapt_userprofile) ;
            holder.txt_name=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_username);
            holder.txt_date_past=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_datepast);
            holder.txt_message=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_data);
            holder.txt_date=(TextView)view.findViewById(R.id.layout_inflate_inboxadapt_statusdate);
            holder.Rel_all=(RelativeLayout)view.findViewById(R.id.rel_allll);
            holder.rootView=(RelativeLayout)view.findViewById(R.id.rootView);


            view.setTag(holder);

        } else {
            holder = (ViewHolderInbox) view.getTag();
        }



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
            holder.txt_message.setText(My_InboxARR.get(i).getStr_message().length()>30?My_InboxARR.get(i).getStr_message().substring(1,27)+"..":My_InboxARR.get(i).getStr_message());
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






                if (My_InboxARR.get(i).getSelected() != null && !My_InboxARR.get(i).getSelected().isEmpty() && My_InboxARR.get(i).getSelected().equalsIgnoreCase("true")) {
                    holder.Rel_all.setBackground(ContextCompat.getDrawable(Mycontext, R.drawable.blur_effect));
                }else
                {
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

                }



        return view;
    }


    class ViewHolderInbox
    {
        CircleImageView profile_image;
        TextView txt_name,txt_date_past,txt_message,txt_date;
        RelativeLayout Rel_all,rootView;
    }


    public void remove(Inbox_Bean object) {
        My_InboxARR.remove(object);
        notifyDataSetChanged();
    }

    public List<Inbox_Bean> getWorldPopulation() {
        return My_InboxARR;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
            My_InboxARR.get(position).setSelected("true");
        }
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    public void filter(String letters)
    {
        String charactors = letters.toLowerCase(Locale.getDefault());
        My_InboxARR.clear();
        if(charactors.length() == 0)
        {
            My_InboxARR.addAll(temp_list);

        }else
        {
            for(Inbox_Bean inbox_bean:temp_list)
            {
                if((inbox_bean.getBooking_no() != null &&inbox_bean.getBooking_no().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getCar_make() != null && inbox_bean.getCar_make().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getCar_model() != null && inbox_bean.getCar_model().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getSender_name() != null && inbox_bean.getSender_name().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getDate_from() != null && inbox_bean.getDate_from().toLowerCase(Locale.getDefault()).contains(charactors)) || (inbox_bean.getStr_message() != null &&inbox_bean.getStr_message().toLowerCase(Locale.getDefault()).contains(charactors)))
                    My_InboxARR.add(inbox_bean);
            }
        }
        notifyDataSetChanged();
    }
}