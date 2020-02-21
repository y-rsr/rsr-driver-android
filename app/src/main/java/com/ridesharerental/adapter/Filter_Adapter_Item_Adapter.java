package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Fliter_Bean;

import java.util.ArrayList;

/**
 * Created by user65 on 1/4/2018.
 */

public class Filter_Adapter_Item_Adapter extends BaseAdapter
{

    private ArrayList<Fliter_Bean> My_InboxARR;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Filter_Adapter_Item_Adapter(Context activity, ArrayList<Fliter_Bean> myInboxARR)
    {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_InboxARR = myInboxARR;
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
            view = myinflater.inflate(R.layout.filter_adapter_single_item, null);
            holder.txt_single_item=(TextView)view.findViewById(R.id.txt_single_item);
            view.setTag(holder);

        } else {
            holder = (ViewHolderInbox) view.getTag();
        }

        holder.txt_single_item.setText(My_InboxARR.get(i).getName());
        return view;
    }


    class ViewHolderInbox
    {
        TextView txt_single_item;
    }
}
