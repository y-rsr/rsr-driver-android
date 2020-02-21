package com.ridesharerental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ridesharerental.ambasador.Updated_Tree;
import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Rank_Pojo;

import java.util.ArrayList;

/**
 * Created by user65 on 1/6/2018.
 */

public class Ambasador_Rank_Adapter extends
        RecyclerView.Adapter<Ambasador_Rank_Adapter.ViewHolder> {
    private ArrayList<Rank_Pojo> ArrayLists;
    LayoutInflater mInflater;
    Context context;
    Activity activity_main;

    public Ambasador_Rank_Adapter(Context ctx, ArrayList<Rank_Pojo> items) {
        this.ArrayLists = items;
        this.context = ctx;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ambasadror_rank_adapter, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)

    {
        holder.txt_driver_name.setText(ArrayLists.get(position).getDateAdded());
        holder.txt_commision_percent.setText(ArrayLists.get(position).getRank());
        holder.txt_commision.setText(ArrayLists.get(position).getType());
      //  holder.rank_at_time.setText(ArrayLists.get(position).getRank());


        holder.rank_at_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String id_=ArrayLists.get(position).getId();
                Intent my_intent=new Intent(context, Updated_Tree.class);
                my_intent.putExtra("ID",id_);
                context.startActivity(my_intent);
            }
        });

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
