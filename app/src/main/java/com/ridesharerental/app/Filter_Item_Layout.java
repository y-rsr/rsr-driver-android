package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ridesharerental.adapter.Filter_Adapter_Item_Adapter;
import com.ridesharerental.pojo.Fliter_Bean;

import java.util.ArrayList;

/**
 * Created by user65 on 1/4/2018.
 */

public class Filter_Item_Layout extends Activity
{
    ArrayList<Fliter_Bean> arrayList = new ArrayList<Fliter_Bean>();
    ListView listView;
    Filter_Adapter_Item_Adapter adapter;
    ImageView img_close;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_item_layout);
        init();

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Filter_Item_Layout.this.finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            if(arrayList.size()>0)
            {
                String make_id=arrayList.get(position).getId();
                String car_make=arrayList.get(position).getName();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("make_id", make_id);
                returnIntent.putExtra("car_make", car_make);
                setResult(Activity.RESULT_OK, returnIntent);
                Filter_Item_Layout.this.finish();
            }
            }
        });
    }

    public void  init()
    {
        listView=(ListView)findViewById(R.id.list_item);
        img_close=(ImageView)findViewById(R.id.img_close);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            arrayList = (ArrayList<Fliter_Bean>) bundle.getSerializable("Contact_list");
            if(arrayList.size()>0)
            {
                adapter=new Filter_Adapter_Item_Adapter(Filter_Item_Layout.this,arrayList);
                listView.setAdapter(adapter);
            }
        }
    }
}
