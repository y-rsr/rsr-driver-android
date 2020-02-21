package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.PricingPojo;

import java.util.ArrayList;

/**
 * Created by CAS59 on 1/10/2018.
 */

public class Extend_date_Adapter extends BaseAdapter {

    ArrayList<PricingPojo> mypriceARR;
    LayoutInflater mlayout;
    Context ctx;
    String currSym="";
    public Extend_date_Adapter(Context priceBreakdown, ArrayList<PricingPojo> priceARR,String currencySymbol) {
        this.ctx = priceBreakdown;
        this.mypriceARR=priceARR;
        this.mlayout = LayoutInflater.from(ctx);
        this.currSym = currencySymbol;
        //notifyDataSetChanged();
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
            view = mlayout.inflate(R.layout.price_extend_adapter, null);

            holder.price_heading = (TextView)view.findViewById(R.id.txt_lable);
            holder.priceAmt = (TextView)view.findViewById(R.id.value);

            view.setTag(holder);

        } else {
            holder = (ViewHolderPricing) view.getTag();
        }

        if (!mypriceARR.get(i).getAmount().equalsIgnoreCase("0.00") && !mypriceARR.get(i).getAmount().equalsIgnoreCase("0.0") && !mypriceARR.get(i).getAmount().equalsIgnoreCase("null") && !mypriceARR.get(i).getAmount().equalsIgnoreCase(null) && !mypriceARR.get(i).getAmount().equalsIgnoreCase(""))
        {
            if (!mypriceARR.get(i).getKey().equalsIgnoreCase("Rental Length"))
            {
                holder.priceAmt.setText(currSym+" "+mypriceARR.get(i).getAmount().toString());
            }else
                {
                    holder.price_heading.setText(mypriceARR.get(i).getKey().toString());

                if(mypriceARR.get(i).getAmount().toString().equals("1"))
                {
                    holder.priceAmt.setText(mypriceARR.get(i).getAmount().toString()+" day");
                }
                else
                {
                    holder.priceAmt.setText(mypriceARR.get(i).getAmount().toString()+" days");
                }
            }

            if (!mypriceARR.get(i).getKey().equalsIgnoreCase("null") && !mypriceARR.get(i).getKey().equalsIgnoreCase(null) && !mypriceARR.get(i).getKey().equalsIgnoreCase(""))
            {
                holder.price_heading.setText(mypriceARR.get(i).getKey().toString());
            }
        }
        else
        {
            holder.price_heading.setText(mypriceARR.get(i).getKey().toString());
        }
        return view;
    }

    class ViewHolderPricing
    {
        TextView price_heading,priceAmt;
    }
}
