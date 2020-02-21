package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Reservation_pojo;

import java.util.ArrayList;

/**
 * Created by CAS59 on 12/5/2017.
 */

public class Reservation_Adapter_Past extends BaseAdapter
{
    private ArrayList<Reservation_pojo> My_reservationARR ;

    private Context Mycontext;
    private LayoutInflater myinflater;

    public Reservation_Adapter_Past(Context mycontext, ArrayList<Reservation_pojo> my_reservationARR)
    {
        this.Mycontext = mycontext;
        this.myinflater = LayoutInflater.from(mycontext);
        this.My_reservationARR = my_reservationARR;
        //notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return My_reservationARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_reservationARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final  viewHolder holder;

        if (view == null) {
            holder = new viewHolder();
            view = myinflater.inflate(R.layout.past_reservation_adapter, null);
            holder.txt_car_name=(TextView)view.findViewById(R.id.txt_car_name);
            view.setTag(holder);

        } else {
            holder = (viewHolder) view.getTag();
        }
            holder.txt_car_name.setText("Rental Started");

        return view;
    }
    class viewHolder
    {
        TextView txt_car_name;
    }
}
