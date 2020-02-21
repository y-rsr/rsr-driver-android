package com.ridesharerental.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Bonus_Pojo;

import java.util.ArrayList;

/**
 * Created by user65 on 1/6/2018.
 */

public class Bonus_Adapter extends
        RecyclerView.Adapter<Bonus_Adapter.ViewHolder> {
    private ArrayList<Bonus_Pojo> ArrayLists;
    LayoutInflater mInflater;
    Context context;
    Activity activity_main;

    public Bonus_Adapter(Context ctx, ArrayList<Bonus_Pojo> items) {
        this.ArrayLists = items;
        this.context = ctx;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bonus_adapter, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)

    {
        holder.txt_driver_name.setText(ArrayLists.get(position).getDateAdded());
        holder.txt_commision_percent.setText((ArrayLists.get(position).getType().equalsIgnoreCase("Rank")?ArrayLists.get(position).getRank():ArrayLists.get(position).getType())+"("+ArrayLists.get(position).getDrivername()+")");
        holder.txt_commision.setText("$"+ArrayLists.get(position).getAmount());
        holder.rank_at_time.setText(ArrayLists.get(position).getStatus());

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

        }
    }
}
