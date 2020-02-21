package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.widgets.dialog.PkDialog;

public class Booking_Step3 extends Activity implements View.OnClickListener
{
    private RelativeLayout mybooknowLAY,Rel_back;
    private TextView txt_car_name,txt_host_name;
    String str_Booking_Number="",str_car_name="",str_owner_name;
    String str_msg="",str_booking_amount="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking__step3);
        init();
    }

    private void init()
    {
        mybooknowLAY = (RelativeLayout)findViewById(R.id.booking_step3booknowLAY);
        Rel_back=(RelativeLayout)findViewById(R.id.chat_detail_backLAY);
        txt_car_name=(TextView)findViewById(R.id.txt_car_name);
        txt_host_name=(TextView)findViewById(R.id.txt_host_name);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_Booking_Number =bundle.getString("Booking_number");
            str_car_name=bundle.getString("car_name");
            str_owner_name=bundle.getString("owner_name");
            str_msg=bundle.getString("msg");
            str_booking_amount=bundle.getString("total_amount");

            txt_car_name.setText(str_car_name);
            txt_host_name.setText(str_owner_name);
        }


       /*  on clinck listener     */
        mybooknowLAY.setOnClickListener(this);
        Rel_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.booking_step3booknowLAY:

                if(!str_Booking_Number.equals("0"))
                {
                    Intent payment_intent=new Intent(Booking_Step3.this,Payment.class);
                    payment_intent.putExtra("Booking_number",str_Booking_Number);
                    payment_intent.putExtra("total_amount",str_booking_amount);
                    startActivity(payment_intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                else
                {
                    if(str_msg!=null && !str_msg.equals(""))
                    {
                        Alert(getResources().getString(R.string.action_opps),str_msg);
                    }

                }

                break;
            case  R.id.chat_detail_backLAY:
                onBackPressed();
                Booking_Step3.this.finish();
                break;
        }
    }


    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(Booking_Step3.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }
}
