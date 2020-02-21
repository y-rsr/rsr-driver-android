package com.ridesharerental.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.pojo.Contract_Bean;
import com.ridesharerental.pojo.Documents_Bean;
import com.ridesharerental.widgets.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class Contract_History_Adapter extends BaseAdapter
{

    private ArrayList<Contract_Bean> My_InboxARR;
    private ArrayList<Contract_Bean> arraylist;

    public Context Mycontext;
    private LayoutInflater myinflater;

    public Contract_History_Adapter(Context activity, ArrayList<Contract_Bean> myInboxARR)
    {
        this.Mycontext = activity;
        this.myinflater = LayoutInflater.from(activity);
        this.My_InboxARR = myInboxARR;
        this.arraylist = new ArrayList<Contract_Bean>();
        this.arraylist.addAll(My_InboxARR);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return My_InboxARR.size();
    }

    @Override
    public Object getItem(int i) {
        return My_InboxARR.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        final ViewHolderInbox holder;

        if (view == null)
        {
            holder = new ViewHolderInbox();
            view = myinflater.inflate(R.layout.contract_history_adapter_layout, null);
            holder.txt_owner_info=(TextView)view.findViewById(R.id.txt_owner_info);
            holder.txt_car_info=(TextView)view.findViewById(R.id.txt_car_info);
            holder.txt_book_info=(TextView)view.findViewById(R.id.txt_book_info);
            holder.txt_spend=(TextView)view.findViewById(R.id.txt_spend);
            holder.txt_insurance=(TextView)view.findViewById(R.id.txt_insurance);
            holder.txt_insurance.setVisibility(View.GONE);

            view.setTag(holder);

        } else {
            holder = (ViewHolderInbox) view.getTag();
        }

        holder.txt_owner_info.setText(My_InboxARR.get(i).getOwnername());
        holder.txt_car_info.setText(My_InboxARR.get(i).getCar_make()+" "+My_InboxARR.get(i).getCar_model()+" "+My_InboxARR.get(i).getYear());
        holder.txt_book_info.setText(My_InboxARR.get(i).getDate_from()+"-"+My_InboxARR.get(i).getDate_to());
        holder.txt_spend.setText("$ "+My_InboxARR.get(i).getTotal_amount());

        if(My_InboxARR.get(i).getDocuments()!=null && My_InboxARR.get(i).getDocuments().length()>0 )
        {
            holder.txt_insurance.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txt_insurance.setVisibility(View.GONE);
        }

        holder.txt_insurance.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(My_InboxARR.get(i).getDocuments()!=null)
                {
                    if(My_InboxARR.get(i).getDocuments().length()>0)
                    {
                        JSONArray array_Documents=My_InboxARR.get(i).getDocuments();
                        showPopUp(array_Documents,Mycontext);
                    }
                }
            }
        });

        return view;
    }


    class ViewHolderInbox
    {
        TextView txt_owner_info,txt_car_info,txt_book_info,txt_spend,txt_insurance;
    }

    public void showPopUp(JSONArray array,Context ctx)
    {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.reservation_details_document);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout Rel_close = (RelativeLayout) dialog.findViewById(R.id.rel_close);
        ExpandableHeightListView rel_documets=(ExpandableHeightListView)dialog.findViewById(R.id.list_documents);
        dialog.show();
        ArrayList<Documents_Bean> arrayLists_doc=new ArrayList<>();
        arrayLists_doc.clear();

        try {

            if(array.length()>0)
            {
                for(int i=0;i<array.length();i++)
                {
                    JSONObject obj=array.getJSONObject(i);
                    Documents_Bean bean=new Documents_Bean();
                    bean.setLabel(obj.getString("label"));
                    bean.setLink(obj.getString("link"));
                    arrayLists_doc.add(bean);
                }
            }

            if(arrayLists_doc.size()>0)
            {
                Document_Adapter adapter=new Document_Adapter(ctx,arrayLists_doc,"Docs");
                rel_documets.setAdapter(adapter);
                rel_documets.setExpanded(true);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Rel_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void filter(String charText) {

        String a = "*";
        charText = charText.toLowerCase(Locale.getDefault());
        My_InboxARR.clear();
        if (charText.length() == 0) {
            My_InboxARR.addAll(arraylist);
        } else {
            for (Contract_Bean wp : arraylist) {

                if(wp.getCar_model() != null){

                    a = wp.getCar_model();

                }

                if (wp.getOwnername().toLowerCase(Locale.getDefault()).contains(charText)|| wp.getCar_make().toLowerCase(Locale.getDefault()).contains(charText) || wp.getVin_no().toLowerCase(Locale.getDefault()).contains(charText) || wp.getPlat_no().toLowerCase(Locale.getDefault()).contains(charText) || wp.getCar_make().toLowerCase(Locale.getDefault()).contains(charText) || wp.getCar_model().toLowerCase(Locale.getDefault()).contains(charText) || wp.getYear().toLowerCase(Locale.getDefault()).contains(charText) || a.toLowerCase(Locale.getDefault()).contains(charText)) {
                    My_InboxARR.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}