package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Fliter_Bean;

import java.util.ArrayList;

/**
 * Created by user65 on 1/4/2018.
 */

public class Car_Features_Adapter extends BaseAdapter
{

    private ArrayList<Fliter_Bean> My_InboxARR;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Car_Features_Adapter(Context activity, ArrayList<Fliter_Bean> myInboxARR)
    {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_InboxARR = myInboxARR;
        notifyDataSetChanged();
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
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final ViewHolderInbox holder;

        if (view == null) {
            holder = new ViewHolderInbox();
            view = myinflater.inflate(R.layout.features_item_adapter_layout, null);
            holder.txt_single_item=(CheckBox) view.findViewById(R.id.check_features);
            holder.txt_single=(TextView)view.findViewById(R.id.txt_single);
            holder.img_single=(ImageView)view.findViewById(R.id.img_item);
            view.setTag(holder);

        } else {
            holder = (ViewHolderInbox) view.getTag();
        }

        holder.txt_single_item.setText(My_InboxARR.get(i).getName());
        holder.txt_single.setText(My_InboxARR.get(i).getName());

        if(My_InboxARR.get(i).isselected()==true)
        {
            holder.txt_single_item.setChecked(true);
            holder.img_single.setBackground(Mycontext.getResources().getDrawable(R.drawable.check_new));
            notifyDataSetChanged();
        }
        else {
            holder.txt_single_item.setChecked(false);
            holder.img_single.setBackground(Mycontext.getResources().getDrawable(R.drawable.uncheck_new));
            notifyDataSetChanged();
        }
        return view;
    }


    class ViewHolderInbox
    {
        CheckBox txt_single_item;
        TextView txt_single;
        ImageView img_single;
    }
}
