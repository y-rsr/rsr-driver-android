package com.ridesharerental.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.pojo.Fliter_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by user65 on 11/29/2017.
 */

public class Contact_Us extends Fragment
{
    private ActionBar actionBar;
    Context context;
    Common_Loader Loader;
    EditText edit_first_name,edit_last_name,edi_email,edit_phone;
    TextView txt_car_make,txt_car_model,txt_car_year,txt_pickup_date;
    EditText edit_message;
    String str_email_match_key="";

    SessionManager sessionManager;
    String user_id = "";

    RelativeLayout Rel_continue;
    ArrayList<Fliter_Bean> arrayList_car_Make=new ArrayList<>();
    ArrayList<Fliter_Bean> arrayList_car_Model=new ArrayList<>();
    ArrayList<Fliter_Bean> arrayList_year=new ArrayList<>();
    JSONObject jsonObject_Car_Model;
    String str_car_make_id="",str_car_model_id="",str_year="",str_date_of_birth="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.contact_us_layout, container, false);
        context=getActivity();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Loader=new Common_Loader(context);
        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(rootView);



        txt_car_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context,Filter_Item_Layout.class);
                intent.putExtra("Contact_list", arrayList_car_Make);
                startActivityForResult(intent, 10);
            }
        });


        txt_car_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!txt_car_make.getText().toString().equals("Car Model"))
                {
                    Intent intent=new Intent(context,Filter_Item_Layout.class);
                    intent.putExtra("Contact_list", arrayList_car_Model);
                    startActivityForResult(intent, 11);
                }
                else
                {
                   // snack_bar("Select car make","");
                }

            }
        });



        txt_car_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(context,Filter_Item_Layout.class);
                intent.putExtra("Contact_list", arrayList_year);
                startActivityForResult(intent, 12);
            }
        });


        txt_pickup_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDatePicker();
            }
        });



        Rel_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            if(edit_first_name.getText().toString().trim().length()>0)
            {
                if(edit_last_name.getText().toString().trim().length()>0)
                {
                    if(edi_email.getText().toString().trim().length()>0)
                    {

                        if(edi_email.getText().toString().matches(str_email_match_key))
                        {
                            if(edit_phone.getText().toString().trim().length()>0)
                            {
                                if(txt_car_make.getText().toString().trim().length()>0)
                                {
                                    if(txt_car_model.getText().toString().trim().length()>0)
                                    {
                                        if(txt_car_year.getText().toString().trim().length()>0)
                                        {
                                            if(txt_pickup_date.getText().toString().trim().length()>0)
                                            {
                                                if(edit_message.getText().toString().trim().length()>0)
                                                {
                                                    load_data();
                                                }
                                                else
                                                {
                                                    System.out.println("------Enter message--------");
                                                    snack_bar(getResources().getString(R.string.eneter_message),"");
                                                }
                                            }
                                            else
                                            {
                                                System.out.println("------choose pickup date--------");
                                                snack_bar(getResources().getString(R.string.choose_pickup_date),"");
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("------Choose manufacture year--------");
                                            snack_bar(getResources().getString(R.string.choose_manufacture_year),"");
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("------Choose Car Model--------");
                                        snack_bar(getResources().getString(R.string.choose_model),"");
                                    }

                                }
                                else {
                                    System.out.println("------Choose Car Make--------");
                                    snack_bar(getResources().getString(R.string.choose_make),"");
                                }
                            }
                            else
                            {
                                System.out.println("------Enter Phone Number--------");
                                snack_bar(getResources().getString(R.string.enter_phone_number),"");
                            }
                        }
                        else
                        {
                            snack_bar(getResources().getString(R.string.enter_vali_email),"");
                        }


                    }
                    else
                    {
                        System.out.println("------Enter Email--------");
                        snack_bar(getResources().getString(R.string.enter_email_hint),"");
                    }
                }
                else
                {
                    System.out.println("------Enter last name--------");
                    snack_bar(getResources().getString(R.string.enter_second_name),"");
                }
            }
            else
            {
                System.out.println("------Enter first name--------");
                snack_bar(getResources().getString(R.string.enter_first_name_),"");
            }


            }
        });


        //Load_Data();
        return rootView;
    }

    public void init(View rootView)
    {
        str_email_match_key = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        edit_first_name=(EditText)rootView.findViewById(R.id.edit_first_name);
        edit_last_name=(EditText)rootView.findViewById(R.id.edit_last_name);
        edi_email=(EditText)rootView.findViewById(R.id.edit_email);
        edit_phone=(EditText)rootView.findViewById(R.id.edit_phone);

        txt_car_make=(TextView)rootView.findViewById(R.id.txt_car_make);
        txt_car_model=(TextView)rootView.findViewById(R.id.txt_car_model);
        txt_car_year=(TextView)rootView.findViewById(R.id.txt_car_year);
        txt_pickup_date=(TextView)rootView.findViewById(R.id.txt_pickup_date);
        Rel_continue=(RelativeLayout)rootView.findViewById(R.id.rel_continue);
        edit_message=(EditText)rootView.findViewById(R.id.txt_mes_value);


        load_filter_data();
    }




    //-----------------service---------------------
    public void Load_Data1()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("language", "en");
        map.put("currency", "USD");
        Call<ResponseBody> call = apiService.post_amenties();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);


                        Loader.dismiss();

                    }
                } catch (Exception e) {
                    Loader.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }











    public void load_data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("firstname", edit_first_name.getText().toString());
        post.put("lastname", edit_last_name.getText().toString());
        post.put("email", edi_email.getText().toString());
        post.put("make", str_car_make_id);
        post.put("model",str_car_model_id);
        post.put("year", txt_car_year.getText().toString());
        post.put("pick_up_date", txt_pickup_date.getText().toString());
        post.put("phone_no", edit_phone.getText().toString());
        post.put("message", edit_message.getText().toString());


        Set keys = post.keySet();

        for (Iterator i = keys.iterator(); i.hasNext();)
        {
            String key = (String) i.next();
            String value = (String) post.get(key);
            System.out.println(""+key+":"+value);
            Log.e("",key+":"+value);
        }

        Call<ResponseBody> call = apiService.save_contact_us(header,post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject respnse_obj=object.getJSONObject("responseArr");
                        String str_status_code=respnse_obj.getString("status");
                        String str_message=respnse_obj.getString("msg");
                        if(str_status_code.equalsIgnoreCase("1"))
                        {
                            Alert(getResources().getString(R.string.action_success),str_message);
                        }
                        else
                        {
                            Alert(getResources().getString(R.string.action_opps),str_message);
                        }
                    }
                } catch (Exception e) {
                    Loader.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }






    public void load_filter_data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        //map.put("language", "en");
        map.put("commonId", user_id);
        Call<ResponseBody> call = apiService.show_filter_data(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
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
                        JSONObject response_obj=object.getJSONObject("responseArr");


                       String str_min_year=response_obj.getString("min_year");
                        String str_max_year=response_obj.getString("max_year");

                        if(str_min_year!=null && !str_min_year.equals("") &&
                                str_max_year!=null && !str_max_year.equals(""))
                        {
                            arrayList_year.clear();
                            int min_year=Integer.parseInt(str_min_year);
                            int max_year=Integer.parseInt(str_max_year);

                            for(int i=min_year;i<max_year;i++)
                            {
                                Fliter_Bean bean=new Fliter_Bean();
                                bean.setId("");
                                bean.setName(String.valueOf(i));
                                arrayList_year.add(bean);
                            }
                        }

                        JSONArray car_make_array=response_obj.getJSONArray("car_makes");
                        if(car_make_array.length()>0)
                        {
                            arrayList_car_Make.clear();
                            for(int i=0;i<car_make_array.length();i++)
                            {
                                JSONObject obj=car_make_array.getJSONObject(i);
                                Fliter_Bean bean=new Fliter_Bean();
                                bean.setId(obj.getString("id"));
                                bean.setName(obj.getString("name"));
                                arrayList_car_Make.add(bean);
                            }
                        }

                        jsonObject_Car_Model=response_obj.getJSONObject("car_models");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 10)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String make_id = data.getStringExtra("make_id");
                String make_name = data.getStringExtra("car_make");
                str_car_make_id=make_id;
                System.out.println("----Make ID--->" + make_id+"----------Make Name----->"+make_name);
                txt_car_make.setText(make_name);
                txt_car_model.setHint("Car Model");
                try {
                    JSONArray jsonArray=jsonObject_Car_Model.getJSONArray(make_id);
                    arrayList_car_Model.clear();
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject obj=jsonArray.getJSONObject(i);
                        Fliter_Bean bean=new Fliter_Bean();
                        bean.setId(obj.getString("id"));
                        bean.setName(obj.getString("name"));
                        System.out.println("--------Model Name-------->"+obj.getString("name"));
                        arrayList_car_Model.add(bean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else  if (requestCode == 11)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String model_id = data.getStringExtra("make_id");
                str_car_model_id=model_id;
                String make_name = data.getStringExtra("car_make");
                System.out.println("----Model ID--->" + model_id+"---------Model Name----->"+make_name);
                txt_car_model.setText(make_name);
            }
        }

        else  if (requestCode == 12)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String model_id = data.getStringExtra("make_id");
                str_year=model_id;
                String make_name = data.getStringExtra("car_make");
                System.out.println("----year ID--->" + model_id+"---------year Name----->"+make_name);
                txt_car_year.setText(make_name);
            }
        }
    }


    //-------------------------dob--------------
    private void birthDatePicker()
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context,datePickerListener_start,now.get(Calendar.YEAR) - 15, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.show();
    }


    private DatePickerDialog.OnDateSetListener datePickerListener_start = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            txt_pickup_date.setText(String.valueOf(selectedMonth+1)+ "/" +String.valueOf(selectedDay)+ "/" +String.valueOf(selectedYear));
            str_date_of_birth = String.valueOf(selectedMonth+1)+ "/" +String.valueOf(selectedDay)+ "/" +String.valueOf(selectedYear);
        }
    };




    //------------------------snack bar---------------
    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar_new, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(getActivity(), msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.LENGTH_SHORT);
        // snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }



    public void Alert(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(context);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }
}
