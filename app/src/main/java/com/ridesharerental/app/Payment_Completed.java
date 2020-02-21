package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by user65 on 12/12/2017.
 */

public class Payment_Completed extends Activity
{
    RelativeLayout Rel_back,Rel_done;
    TextView txt_price;
    String str_total_amount="";
    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_completed);
        init();

        Rel_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Payment_Completed.this.finish();
            }
        });

        Rel_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent my_Edit=new Intent(Payment_Completed.this,Main_homepage.class);
                my_Edit.putExtra("calling_type","reservation");
                startActivity(my_Edit);
                Payment_Completed.this.finish();
            }
        });
    }
    public void init()
    {
        Rel_back=(RelativeLayout)findViewById(R.id.chat_detail_backLAY);
        Rel_done=(RelativeLayout)findViewById(R.id.booking_step2leftLAY);
        txt_price=(TextView)findViewById(R.id.txt_price);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_total_amount=bundle.getString("total_amount");
            txt_price.setText(str_total_amount);
        }
    }
}
