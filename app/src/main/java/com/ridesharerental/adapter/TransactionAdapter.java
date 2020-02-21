package com.ridesharerental.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.My_Transaction_Bean;
import com.ridesharerental.pojo.Transaction_Extend_Bean;
import com.ridesharerental.widgets.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by CAS59 on 12/6/2017.
 */

public class TransactionAdapter  extends BaseAdapter {

    private ArrayList<My_Transaction_Bean> My_transARR;

    private Context Mycontext;
    private LayoutInflater myinflater;


    public TransactionAdapter(FragmentActivity activity, ArrayList<My_Transaction_Bean> myTransactionARR)
    {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_transARR = myTransactionARR;
    }
    @Override
    public int getCount()
    {
        return My_transARR.size();
    }

    @Override
    public Object getItem(int i)
    {
        return My_transARR.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        final ViewHolderTransaction holder;

        if (view == null)
        {
            holder = new ViewHolderTransaction();
            view = myinflater.inflate(R.layout.transaction_adapter, null);
            holder.txt_details=(TextView)view.findViewById(R.id.transaction_adapter_hdTXT1);
            holder.cardView_Details=(CardView)view.findViewById(R.id.card_deatils);
            holder.txt_car_name=(TextView)view.findViewById(R.id.txt_car_name);
            holder.txt_rental_cost=(TextView)view.findViewById(R.id.transaction_adapter_priceTXT);
            holder.txt_total=(TextView)view.findViewById(R.id.transaction_adapter_totalvalTXT);
            holder.txt_no_of_days=(TextView)view.findViewById(R.id.txt_no_days);
            holder.expand_list=(ExpandableHeightListView)view.findViewById(R.id.my_transaction_layout_listview_extent);



            holder.cardView_Details.setVisibility(View.GONE);
            holder.expand_list.setVisibility(View.GONE);
            view.setTag(holder);

        } else
            {
            holder = (ViewHolderTransaction) view.getTag();
        }

        holder.txt_car_name.setText(My_transARR.get(i).getCarInfo());
        holder.txt_rental_cost.setText("$"+My_transARR.get(i).getRental_cost());
        holder.txt_total.setText("$"+My_transARR.get(i).getTotal_amount());

        holder.txt_no_of_days.setText(Mycontext.getResources().getString(R.string.rented_car_for)+" "+My_transARR.get(i).getNo_of_days()+" "+Mycontext.getResources().getString(R.string.days));
        try {
            if(My_transARR.get(i).getExtendDetails()!=null && My_transARR.get(i).getExtendDetails().length()>0)
            {
                ArrayList<Transaction_Extend_Bean> arrayList=new ArrayList<>();
                arrayList.clear();
                JSONArray array=My_transARR.get(i).getExtendDetails();
                for(int j=0;j<array.length();j++)
                {
                    JSONObject obj=array.getJSONObject(j);
                    Transaction_Extend_Bean bean=new Transaction_Extend_Bean();
                    bean.setNo_of_days(obj.getString("no_of_days"));
                    bean.setTotal_amount(obj.getString("total_amount"));
                    bean.setDateAdded(obj.getString("dateAdded"));
                    bean.setLablel(obj.getString("label"));
                    arrayList.add(bean);
                    Transaction_Extend_Adapter adapter=new Transaction_Extend_Adapter(Mycontext,arrayList);
                    holder.expand_list.setAdapter(adapter);
                    holder.expand_list.setExpanded(true);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        holder.txt_details.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if( holder.expand_list.getVisibility() == View.VISIBLE)
                {
                    holder.txt_details.setText(Mycontext.getResources().getString(R.string.details));
                    holder.expand_list.setVisibility(View.GONE);
                }
                else
                {
                    holder.txt_details.setText(Mycontext.getResources().getString(R.string.close));
                    holder.expand_list.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
    class ViewHolderTransaction
    {
        TextView txt_details;
        CardView cardView_Details;
        TextView txt_car_name;
        TextView txt_rental_cost;
        TextView txt_total;
        TextView txt_no_of_days;
        ExpandableHeightListView expand_list;
    }
}