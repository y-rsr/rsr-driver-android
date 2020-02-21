package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.price_dedution_pojo;

import java.util.ArrayList;

/**
 * Created by user129 on 5/30/2018.
 */
public class DeductionList_Adapter extends BaseAdapter {

    private ArrayList<price_dedution_pojo> data;
    private LayoutInflater mInflater;
    private Context context;

    public DeductionList_Adapter(Context c, ArrayList<price_dedution_pojo> d) {
        context = c;
        mInflater = LayoutInflater.from(context);
        data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    public class ViewHolder {
        private TextView title;
        private ImageView select;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.deduction_list_adapter, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.txt_content);
            holder.select = (ImageView) view.findViewById(R.id.Iv_indicater);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        if (data.get(position).getSelectflag().equalsIgnoreCase("yes")) {

            holder.select.setImageResource(R.drawable.card_selected);

        } else {

            holder.select.setImageResource(R.drawable.card_unselect);

        }


        holder.title.setText(data.get(position).getText());


        return view;
    }
}
