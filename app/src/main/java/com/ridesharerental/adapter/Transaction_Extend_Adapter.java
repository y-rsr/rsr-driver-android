package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Transaction_Extend_Bean;

import java.util.ArrayList;

/**
 * Created by user65 on 1/10/2018.
 */

public class Transaction_Extend_Adapter extends BaseAdapter {

    private ArrayList<Transaction_Extend_Bean> My_transARR;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Transaction_Extend_Adapter(Context activity, ArrayList<Transaction_Extend_Bean> myTransactionARR) {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_transARR = myTransactionARR;
    }

    @Override
    public int getCount() {
        return My_transARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_transARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolderTransaction holder;

        if (view == null) {
            holder = new ViewHolderTransaction();
            view = myinflater.inflate(R.layout.transaction_extend_adapter, null);
            holder.txt_extend_days = (TextView) view.findViewById(R.id.txt_rental_extent_days);
            holder.getTxt_extend_price = (TextView) view.findViewById(R.id.txt_ented_price);
            holder.txt_date = (TextView) view.findViewById(R.id.txt_date);
            holder.txt_label = (TextView) view.findViewById(R.id.transaction_rental_cost_1);
            view.setTag(holder);

        } else {
            holder = (ViewHolderTransaction) view.getTag();
        }
        holder.getTxt_extend_price.setText("$" + My_transARR.get(i).getTotal_amount());
        // holder.txt_extend_days.setText("$"+My_transARR.get(i).getTot);
        holder.txt_extend_days.setText("Rented car for " + My_transARR.get(i).getNo_of_days() + " days");
        holder.txt_label.setText(My_transARR.get(i).getLablel());
        holder.txt_date.setText(My_transARR.get(i).getDateAdded());

        return view;
    }


    class ViewHolderTransaction {
        TextView txt_extend_days,txt_date;
        TextView getTxt_extend_price;
        TextView txt_label;

    }
}
