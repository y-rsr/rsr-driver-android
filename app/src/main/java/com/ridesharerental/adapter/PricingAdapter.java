package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.PricingPojo;

import java.util.ArrayList;

/**
 * Created by CAS59 on 1/10/2018.
 */

public class PricingAdapter extends BaseAdapter {

    ArrayList<PricingPojo> mypriceARR;
    LayoutInflater mlayout;
    Context ctx;
    String currSym = "";

    public PricingAdapter(Context priceBreakdown, ArrayList<PricingPojo> priceARR, String currencySymbol) {
        this.ctx = priceBreakdown;
        this.mypriceARR = priceARR;
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
            view = mlayout.inflate(R.layout.pricing_adapter_layout, null);

            holder.price_heading = (TextView) view.findViewById(R.id.txtview_headline);
            holder.priceAmt = (TextView) view.findViewById(R.id.txt_value);
            holder.rel_data = (RelativeLayout) view.findViewById(R.id.layout_Rentallength);
            holder.view_data = (View) view.findViewById(R.id.view_data);

            view.setTag(holder);

        } else {
            holder = (ViewHolderPricing) view.getTag();
        }


        if (!mypriceARR.get(i).getKey().equalsIgnoreCase("Rental Length")) {
            holder.priceAmt.setText(currSym + " " + mypriceARR.get(i).getAmount().toString());
        } else {
            holder.priceAmt.setText(mypriceARR.get(i).getAmount().toString() + " days");
        }

        holder.price_heading.setText(mypriceARR.get(i).getKey().toString());


        /*if (!mypriceARR.get(i).getAmount().equalsIgnoreCase("0.00") && !mypriceARR.get(i).getAmount().equalsIgnoreCase("0.0") && !mypriceARR.get(i).getAmount().equalsIgnoreCase("null") && !mypriceARR.get(i).getAmount().equalsIgnoreCase(null) && !mypriceARR.get(i).getAmount().equalsIgnoreCase("")) {
            holder.rel_data.setVisibility(View.VISIBLE);
            holder.view_data.setVisibility(View.VISIBLE);
            if (!mypriceARR.get(i).getKey().equalsIgnoreCase("Rental Length")) {
                holder.priceAmt.setText(currSym + " " + mypriceARR.get(i).getAmount().toString());
            } else {
                holder.priceAmt.setText(mypriceARR.get(i).getAmount().toString() + " days");
            }

            if (!mypriceARR.get(i).getKey().equalsIgnoreCase("null") && !mypriceARR.get(i).getKey().equalsIgnoreCase(null) && !mypriceARR.get(i).getKey().equalsIgnoreCase("")) {
                holder.price_heading.setText(mypriceARR.get(i).getKey().toString());
            }


//            System.out.println("-----kannan--- holder.priceAmt---------" +holder.priceAmt.getText().toString());
//            System.out.println("-----kannan---  holder.price_heading---------" +holder.price_heading.getText().toString());



        } else {


            if(mypriceARR.get(i).getKey().equalsIgnoreCase("Deductible Insurance") && mypriceARR.get(i).getAmount().equalsIgnoreCase("0.00")){

                holder.price_heading.setText(mypriceARR.get(i).getKey().toString());

                holder.priceAmt.setText(currSym + " " + mypriceARR.get(i).getAmount().toString());

                holder.rel_data.setVisibility(View.VISIBLE);
                holder.view_data.setVisibility(View.VISIBLE);

                System.out.println("----coming--Deductible Insurance--------");


            }else {

                holder.rel_data.setVisibility(View.GONE);
                holder.view_data.setVisibility(View.GONE);

            }



        }*/

        return view;
    }

    class ViewHolderPricing {
        TextView price_heading, priceAmt;
        RelativeLayout rel_data;
        View view_data;
    }
}
