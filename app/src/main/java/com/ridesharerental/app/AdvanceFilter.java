package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.adapter.Car_Features_Adapter;
import com.ridesharerental.pojo.Fliter_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightGridView;
import com.ridesharerental.widgets.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvanceFilter extends Activity
{
    ImageView img_close;
    Common_Loader loader;
    SessionManager sessionManager;
    String user_id="";
    String str_min_mileage="",str_max_mileage="",str_min_price="",str_max_price="";

    String str_min_year="",str_max_year="";
    TextView txt_min_year,txt_max_year;


    TextView tx_min_mileage,txt_max_mileage,txt_min_price,txt_max_price;
    RangeSeekBar seekBar_mileage,seekBar_price,seekBar_Year;
    ArrayList<Fliter_Bean> arrayList_car_Make=new ArrayList<>();
    ArrayList<Fliter_Bean> arrayList_car_Model=new ArrayList<>();

    ArrayList<Fliter_Bean> arrayList_car_features=new ArrayList<>();
    JSONObject jsonObject_Car_Model;

    RelativeLayout Rel_car_Make,Rel_Car_Model;
    TextView txt_car_make,txt_car_model;

    Car_Features_Adapter adapter;
    ExpandableHeightGridView gridView_features;
    ArrayList<String> selected_Value=new ArrayList<>();

    RelativeLayout Rel_Done;
    String str_location="",str_car_make_id="",str_car_model_id="",str_post_min_mileage="",str_post_max_mileage="",
            str_post_min_price="",str_post_max_price="",str_post_min_year="",str_post_max_year="",str_post_features="";

    TextView txt_Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advance_filter);
        loader=new Common_Loader(AdvanceFilter.this);
        sessionManager = new SessionManager(AdvanceFilter.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);
        init();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                AdvanceFilter.this.finish();
            }
        });

        gridView_features.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(arrayList_car_features.size()>0)
                {

                    if(arrayList_car_features.get(position).isselected()==true)
                    {
                        String select_id=arrayList_car_features.get(position).getId();
                        selected_Value.remove(select_id);
                        arrayList_car_features.get(position).setIsselected(false);
                        adapter.notifyDataSetChanged();
                    }
                    else if(arrayList_car_features.get(position).isselected()==false)
                    {
                        String select_id=arrayList_car_features.get(position).getId();
                        selected_Value.add(select_id);
                        arrayList_car_features.get(position).setIsselected(true);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });


        seekBar_mileage.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener()
        {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar,
                                                    Object minValue, Object maxValue) {
                Log.e("bar", "" + maxValue.toString() + "\n" + minValue.toString());
                System.out.println("------------------------>");

                tx_min_mileage.setText(String.valueOf(minValue));
                txt_max_mileage.setText(String.valueOf(maxValue));

            }
        });




        seekBar_Year.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener()
        {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar,
                                                    Object minValue, Object maxValue) {
                Log.e("bar", "" + maxValue.toString() + "\n" + minValue.toString());
                System.out.println("------------------------>");

                txt_min_year.setText(String.valueOf(minValue));
                txt_max_year.setText(String.valueOf(maxValue));

            }
        });


        seekBar_price.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener()
        {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar,
                                                    Object minValue, Object maxValue) {
                Log.e("bar", "" + maxValue.toString() + "\n" + minValue.toString());
                System.out.println("------------------------>");

                txt_min_price.setText("$"+String.valueOf(minValue));
                txt_max_price.setText("$"+String.valueOf(maxValue));

            }
        });


        Rel_car_Make.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(AdvanceFilter.this,Filter_Item_Layout.class);
                intent.putExtra("Contact_list", arrayList_car_Make);
                startActivityForResult(intent, 10);
            }
        });

        Rel_Car_Model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!txt_car_make.getText().toString().equals(getResources().getString(R.string.select)))
                {
                    Intent intent=new Intent(AdvanceFilter.this,Filter_Item_Layout.class);
                    intent.putExtra("Contact_list", arrayList_car_Model);
                    startActivityForResult(intent, 11);
                }
                else
                {
                    snack_bar(getResources().getString(R.string.select_car_make),"");
                }

            }
        });

        Rel_Done.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                str_post_min_mileage=tx_min_mileage.getText().toString();
                str_post_max_mileage=txt_max_mileage.getText().toString();

                str_post_min_price=txt_min_price.getText().toString();
                str_post_max_price=txt_max_price.getText().toString();

                str_post_min_year=txt_min_year.getText().toString();
                str_post_max_year=txt_max_year.getText().toString();

                if(selected_Value.size()>0)
                {
                    System.out.println("--------Seleted value--------->"+selected_Value.toString());
                    str_post_features=selected_Value.toString();
                    str_post_features=(str_post_features.substring(1, str_post_features.length()-1).replaceAll("\\s",""));
                }

                Apply_Filter();
            }
        });

        txt_Reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();

                overridePendingTransition(0, 0);
                startActivity(intent);
               /* tx_min_mileage.setText(str_min_mileage);
                txt_max_mileage.setText(str_max_mileage);
                seekBar_mileage.clearFocus();
                seekBar_mileage.clearColorFilter();

                txt_min_price.setText(str_min_price);
                txt_max_price.setText(str_max_price);*/
            }
        });

    }

    public void init()
    {

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_location=bundle.getString("location");
        }

        txt_Reset=(TextView)findViewById(R.id.txt_Reset);
        Rel_Done=(RelativeLayout)findViewById(R.id.advance_filter_DoneLAY);

        seekBar_mileage=(RangeSeekBar)findViewById(R.id.range_seeker);
        seekBar_price=(RangeSeekBar)findViewById(R.id.advance_filter_rangeseekpricebar);
        seekBar_Year=(RangeSeekBar)findViewById(R.id.advance_filter_rangeseekDistancebar);
        img_close=(ImageView)findViewById(R.id.advance_filter_closeIMG);

        tx_min_mileage=(TextView)findViewById(R.id.txt_min_val);
        txt_max_mileage=(TextView)findViewById(R.id.txt_max_value);

        txt_min_price=(TextView)findViewById(R.id.advance_filter_minpriceTXT);
        txt_max_price=(TextView)findViewById(R.id.advance_filter_maxPriceTXT);

        txt_min_year=(TextView)findViewById(R.id.advance_filter_minDistTXT);
        txt_max_year=(TextView)findViewById(R.id.advance_filter_maxDistTXT);

        Rel_car_Make=(RelativeLayout)findViewById(R.id.rel_car_make);
        Rel_Car_Model=(RelativeLayout)findViewById(R.id.rel_car_model);

        txt_car_make=(TextView)findViewById(R.id.txtt_car_make);
        txt_car_model=(TextView)findViewById(R.id.advance_filter_reservationheadvalueTXT);

        gridView_features=(ExpandableHeightGridView)findViewById(R.id.gride_data);
        load_filter_data();
    }

    public void load_filter_data()
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        //map.put("language", "en");
        map.put("commonId", user_id);
        Call<ResponseBody> call = apiService.show_filter_data(map);
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
                        JSONObject response_obj=object.getJSONObject("responseArr");



                        str_min_year=response_obj.getString("min_year");
                        str_max_year=response_obj.getString("max_year");
                        txt_min_year.setText(str_min_year);
                        txt_max_year.setText(str_max_year);
                        seekBar_Year.setRangeValues(Integer.parseInt(str_min_year),Integer.parseInt(str_max_year));

                        str_min_mileage=response_obj.getString("min_milege");
                        str_max_mileage=response_obj.getString("max_milege");
                        seekBar_mileage.setRangeValues(Float.parseFloat(str_min_mileage),Float.parseFloat(str_max_mileage));

                        System.out.println("-----Min mileage----------->"+str_min_mileage+"--------Max Mileage------->"+str_max_mileage);

                        str_min_price=response_obj.getString("min_price");
                        str_max_price=response_obj.getString("max_price");

                        seekBar_price.setRangeValues(Double.parseDouble(str_min_price),Double.parseDouble(str_max_price));

                        System.out.println("-----Min Price----------->"+str_min_price+"--------Max Price------->"+str_max_price);

                        tx_min_mileage.setText(str_min_mileage);
                        txt_max_mileage.setText(str_max_mileage);

                        txt_min_price.setText("$"+str_min_price);
                        txt_max_price.setText("$"+str_max_price);

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
                        JSONArray jsonArray_car_features=response_obj.getJSONArray("carFeature");
                        if(jsonArray_car_features.length()>0)
                        {
                            arrayList_car_features.clear();
                            for(int i=0;i<jsonArray_car_features.length();i++)
                            {
                                JSONObject obj=jsonArray_car_features.getJSONObject(i);
                                Fliter_Bean bean=new Fliter_Bean();
                                bean.setId(obj.getString("id"));
                                bean.setName(obj.getString("name"));
                                bean.setIsselected(false);
                                arrayList_car_features.add(bean);
                            }
                        }

                        if(arrayList_car_features.size()>0)
                        {
                            adapter=new Car_Features_Adapter(AdvanceFilter.this,arrayList_car_features);
                            gridView_features.setAdapter(adapter);
                            gridView_features.setExpanded(true);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
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
                txt_car_model.setText(getResources().getString(R.string.select));
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
    }




    public void Apply_Filter()
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        System.out.println(""+"-------------CommonId----------"+":"+user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("location", str_location);
        post.put("car_make", str_car_make_id);
        post.put("car_model", str_car_model_id);

        post.put("min_price", str_post_min_price);
        post.put("max_price", str_post_max_price);

        post.put("min_milege", str_post_min_mileage);
        post.put("max_milege", str_post_max_mileage);

        post.put("min_year", str_post_min_year);
        post.put("max_year", str_post_max_year);


        if(str_post_features!=null)
        {
            post.put("features ", str_post_features);
        }

        Set keys = post.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) post.get(key);
            System.out.println(""+key+":"+value);
        }

        Call<ResponseBody> call = apiService.apply_filter(header,post);
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
                        Log.e("----Country Response-->", Str_response);
                        //JSONObject object = new JSONObject(Str_response);
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("filter_data",Str_response);
                        setResult(Activity.RESULT_OK, returnIntent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        AdvanceFilter.this.finish();

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
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(AdvanceFilter.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }
}
