package com.ridesharerental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.PdfFiles;

import java.util.ArrayList;

/**
 * Created by user110 on 5/31/2019.
 */

public class PdfListAdapter extends BaseAdapter {
    private ArrayList<PdfFiles> data;
    private LayoutInflater mInflater;
    private Context context;
    OnClickItem listners;


    public interface OnClickItem{

        public void OnClickListners(int position, String path);

    }
    public PdfListAdapter(Context c, ArrayList<PdfFiles> d) {
        context = c;
        mInflater = LayoutInflater.from(context);
        data = d;
    }
    @Override
    public int getCount() {
        int size = 0;
        try {
            size =data.size();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return size;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.pdfadapter, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.pdf_name);
            holder.container = (LinearLayout) view.findViewById(R.id.container);



            view.setTag(holder);

        } else {

            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        holder.name.setText(data.get(position).getName());


        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listners.OnClickListners(position, data.get(position).getPath());

            }
        });


        return view;
    }
    public void SetonClickLinsers(OnClickItem listners){

        this.listners = listners;
    }
    public class ViewHolder {
        private TextView name;
        private LinearLayout container;
    }
}
