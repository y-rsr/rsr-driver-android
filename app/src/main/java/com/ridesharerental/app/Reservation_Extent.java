package com.ridesharerental.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.adapter.Extend_date_Adapter;
import com.ridesharerental.pojo.PricingPojo;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ridesharerental.app.R.id.reservation_extent_dateTXT;

public class Reservation_Extent extends Activity
{
    RelativeLayout Rel_back;
    RelativeLayout Rel_Done;
    String str_check_in="",str_check_out="",str_booking_no="",str_car_name="";
    TextView txt_action_title;
    TextView txt_check_In,txt_check_out;
    RelativeLayout Rel_change;
    TextView txt_entend_date;
    Common_Loader loader;
    String str_extent_date="";



    SessionManager sessionManager;
    String user_id="";
    ArrayList<PricingPojo> PriceARR = new ArrayList<PricingPojo>();
    ExpandableHeightListView exten_list;
    LinearLayout linear_empty_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_extent);
        init();
        loader=new Common_Loader(Reservation_Extent.this);

        sessionManager = new SessionManager(Reservation_Extent.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);

        Rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Reservation_Extent.this.finish();

            }
        });

        Rel_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(txt_entend_date.getText().toString().trim().length()>0)
                {
                    proceed_extent_booking();
                }
                else
                {
                    snack_bar(getResources().getString(R.string.select_extend_date),"");
                }
            }
        });

        Rel_change.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setdate_end();
            }
        });
    }


    public void init()
    {


        Rel_back=(RelativeLayout)findViewById(R.id.reservation_extent_backLAY);
        Rel_Done=(RelativeLayout)findViewById(R.id.rel_continue);

        txt_check_In=(TextView)findViewById(R.id.txt_check_In);
        txt_check_out=(TextView)findViewById(R.id.txt_check_out);

        Rel_change=(RelativeLayout)findViewById(R.id.rel_change_date);
        txt_entend_date=(TextView)findViewById(reservation_extent_dateTXT);

        exten_list=(ExpandableHeightListView)findViewById(R.id.extend_listview) ;
        linear_empty_data=(LinearLayout)findViewById(R.id.linear_empty_data);

        txt_action_title=(TextView)findViewById(R.id.action_title);

        exten_list.setVisibility(View.GONE);
        linear_empty_data.setVisibility(View.VISIBLE);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_check_in=bundle.getString("check_in");
            str_check_out=bundle.getString("check_out");
            str_booking_no=bundle.getString("booking_no");
            str_car_name=bundle.getString("car_name");

            /*txt_check_In.setText(str_check_in);
            txt_check_out.setText(str_check_out);*/


            txt_check_In.setText(str_check_out);
            txt_check_out.setText("");


            txt_action_title.setText(str_car_name);
        }

    }





    private void setdate_start()
    {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(Reservation_Extent.this, datePickerListener_start, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        long maxdate = 0;
        Date date = null;
        Calendar mac = Calendar.getInstance();
        mac.add(Calendar.DATE, 7);
        date = mac.getTime();
        maxdate = date.getTime();
        dialog.setTitle("");
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.getDatePicker().setMaxDate(maxdate);


        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener_start = new DatePickerDialog.OnDateSetListener()
    {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            String getStart_Date_Time = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
            Log.d("Date------->", getStart_Date_Time);
            str_extent_date=getStart_Date_Time;
            txt_entend_date.setText(getStart_Date_Time);
            load_data();
        }
    };

    public void load_data()
    {
        loader.show();
        PriceARR.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("booking_no", str_booking_no);
        post.put("extend_to",str_extent_date);
        Call<ResponseBody> call = apiService.extent_date(header,post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        Log.e("----Extend Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            JSONArray array=response_obj.getJSONArray("pricingArr");
                            if(array.length()>0)
                            {
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject obj=array.getJSONObject(i);
                                    PricingPojo pricepojo = new PricingPojo();
                                    pricepojo.setKey(obj.getString("key"));
                                    pricepojo.setAmount(obj.getString("value"));
                                    PriceARR.add(pricepojo);

                                }
                            }

                            if(PriceARR.size()>0)
                            {
                                exten_list.setVisibility(View.VISIBLE);
                                linear_empty_data.setVisibility(View.GONE);
                                Extend_date_Adapter padapter = new Extend_date_Adapter(Reservation_Extent.this, PriceARR , "$");
                                exten_list.setAdapter(padapter);
                                exten_list.setExpanded(true);
                                padapter.notifyDataSetChanged();
                            }
                            else
                            {
                                exten_list.setVisibility(View.GONE);
                                linear_empty_data.setVisibility(View.VISIBLE);
                            }

                        }
                        else
                        {
                            exten_list.setVisibility(View.GONE);
                            linear_empty_data.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        exten_list.setVisibility(View.GONE);
                        linear_empty_data.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e)
                {
                    exten_list.setVisibility(View.GONE);
                    linear_empty_data.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }




    public void proceed_extent_booking()
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("booking_no", str_booking_no);
        post.put("extend_to",str_extent_date);


        Set keys = post.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) post.get(key);
            System.out.println(""+key+":"+value);
        }


        Call<ResponseBody> call = apiService.extent_proceed_booking(header,post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        loader.dismiss();
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            String str_extend_no=response_obj.getString("extend_no");
                             Intent intent=new Intent(Reservation_Extent.this,Payment.class);
                            intent.putExtra("Booking_number",str_extend_no);
                            intent.putExtra("calling","extend");
                             startActivity(intent);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }


    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.LENGTH_SHORT);
        // snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }





    private void setdate_end()
    {

        try {

            final Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(Reservation_Extent.this, datePickerListener_end, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.setTitle("");

            long maxdate = 0;
            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date date1 = sdf.parse(str_check_out);
                System.out.println("---------checkout date---------->"+str_check_out);
                maxdate = date1.getTime();

                selectionmodeday(maxdate,dialog);
                // selectionmodeday(maxdate, dialog);
               // dialog.getDatePicker().setMinDate(maxdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // dialog.getDatePicker().setMinDate(new Date().getTime());
           // dialog.show();
        }catch (Exception e)
        {

        }

    }


    private DatePickerDialog.OnDateSetListener datePickerListener_end = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            //String getStart_Date_Time = String.valueOf(selectedDay) + "/" + String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedYear);
            String getStart_Date_Time = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);

            try {

                DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date date = originalFormat.parse(getStart_Date_Time);
                String formattedDate = targetFormat.format(date);
                System.out.println("----------target format---->"+formattedDate);
                str_extent_date=formattedDate;
                txt_entend_date.setText(formattedDate);
                txt_check_out.setText(str_extent_date);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            load_data();
        }
    };



    private void selectionmodeday(long date1, DatePickerDialog dialog)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE,1);
        long end = calendar.getTimeInMillis();
        System.out.println("----Ranjith Date----->"+String.valueOf(end));
        dialog.getDatePicker().setMinDate(end);
        //dialog.getDatePicker().setMaxDate(end);
        dialog.show();

    }

}
