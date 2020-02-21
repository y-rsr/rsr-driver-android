package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Extend_details_bean;

import java.util.ArrayList;

/**
 * Created by CAS59 on 1/10/2018.
 */

public class Extent_details_adapter extends BaseAdapter {

    ArrayList<Extend_details_bean> mypriceARR;
    LayoutInflater mlayout;
    Context ctx;
    String currSym="";
    public Extent_details_adapter(Context priceBreakdown, ArrayList<Extend_details_bean> priceARR, String currencySymbol)
    {
        this.ctx = priceBreakdown;
        this.mypriceARR=priceARR;
        this.mlayout = LayoutInflater.from(ctx);
        this.currSym = currencySymbol;
    }



    @Override
    public int getCount() {
        return mypriceARR.size();
    }

    @Override
    public Object getItem(int i) {
        return mypriceARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolderPricing holder;

        if (view == null) {
            holder = new ViewHolderPricing();
            view = mlayout.inflate(R.layout.extend_details_adapter, null);

            holder.txt_chec_in = (TextView)view.findViewById(R.id.txt_check_In);
            holder.txt_check_out = (TextView)view.findViewById(R.id.txt_check_out);
            holder.txt_value=(TextView)view.findViewById(R.id.value);
            holder.txt_label=(TextView)view.findViewById(R.id.value_tot);

            view.setTag(holder);

        } else {
            holder = (ViewHolderPricing) view.getTag();
        }

        holder.txt_chec_in.setText(mypriceARR.get(i).getDate_from());
        holder.txt_check_out.setText(mypriceARR.get(i).getDate_to());
        holder.txt_label.setText("$"+mypriceARR.get(i).getTotal_amount());

        //holder.txt_value.setText("$"+mypriceARR.get(i).getNo_of_days());
        if(mypriceARR.get(i).getNo_of_days().equalsIgnoreCase("1"))
        {
            holder.txt_value.setText(mypriceARR.get(i).getNo_of_days()+" day");
        }
        else
        {
            holder.txt_value.setText(mypriceARR.get(i).getNo_of_days()+" days");
        }





        return view;
    }

    class ViewHolderPricing
    {
        TextView txt_chec_in,txt_check_out,txt_value;
        TextView txt_label;
    }
}
