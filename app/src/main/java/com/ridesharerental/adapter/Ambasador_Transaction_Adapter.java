package com.ridesharerental.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Ambasador_Transaction_Bean;

import java.util.ArrayList;

/**
 * Created by user65 on 1/6/2018.
 */

public class Ambasador_Transaction_Adapter extends
        RecyclerView.Adapter<Ambasador_Transaction_Adapter.ViewHolder> {
    private ArrayList<Ambasador_Transaction_Bean> ArrayLists;
    LayoutInflater mInflater;
    Context context;
    Activity activity_main;

    public Ambasador_Transaction_Adapter(Context ctx, ArrayList<Ambasador_Transaction_Bean> items) {
        this.ArrayLists = items;
        this.context = ctx;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambasador_transaction_adapter, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)

    {
        holder.txt_driver_name.setText(ArrayLists.get(position).getDrivername());
        holder.txt_commision_percent.setText(ArrayLists.get(position).getCommission_percent()+" %");


        holder.txt_commision.setText(ArrayLists.get(position).getCommission());
        System.out.println("--------cccccccccc------>"+ArrayLists.get(position).getCommission().toString());


        holder.rank_at_time.setText(ArrayLists.get(position).getRank());
        holder.date.setText(ArrayLists.get(position).getDateAdded());
        holder.driver_level.setText(ArrayLists.get(position).getLevel());
        holder.txt_type.setText(ArrayLists.get(position).getType());
        holder.payment_status.setText(ArrayLists.get(position).getStatus());

        holder.itemView.setTag("");
    }

    @Override
    public int getItemCount()
    {
        return ArrayLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txt_driver_name,txt_commision_percent,txt_commision,rank_at_time,date,driver_level,txt_type,payment_status;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_driver_name = (TextView) itemView.findViewById(R.id.txt_driver_name);
            txt_commision_percent = (TextView) itemView.findViewById(R.id.commsio_percent);
            txt_commision = (TextView) itemView.findViewById(R.id.txt_commision);
            rank_at_time = (TextView) itemView.findViewById(R.id.txt_rank_time);
            date = (TextView) itemView.findViewById(R.id.txt_date);
            driver_level = (TextView) itemView.findViewById(R.id.txt_driver_level);
            txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            payment_status = (TextView) itemView.findViewById(R.id.txt_payment_status);
        }
    }
}
