package com.ridesharerental.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ridesharerental.adapter.Extent_details_adapter;
import com.ridesharerental.pojo.Extend_details_bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 1/12/2018.
 */

public class Extent_Details extends Activity
{
    Common_Loader loader;
    SessionManager sessionManager;
    String user_id="",str_booking_no="";
    ArrayList<Extend_details_bean> arrayList=new ArrayList<Extend_details_bean>();
    ExpandableHeightListView lit_view;
    RelativeLayout Rel_Back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.extend_detail_layout);
        loader=new Common_Loader(Extent_Details.this);


        sessionManager = new SessionManager(Extent_Details.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);
        init();

        Rel_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Extent_Details.this.finish();
            }
        });
    }
    public void init()
    {
        lit_view=(ExpandableHeightListView)findViewById(R.id.listview);
        Rel_Back=(RelativeLayout)findViewById(R.id.reservation_extent_backLAY);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_booking_no = bundle.getString("booking_no");
            load_data();
        }
        }

    public void load_data()
    {
        loader.show();
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        arrayList.clear();
        HashMap<String, String> post = new HashMap<>();
        post.put("booking_no", str_booking_no);
        Call<ResponseBody> call = apiService.show_extent_details(header,post);
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
                        String status_code=response_obj.getString("status");
                        if(status_code.equals("1"))
                        {
                            if(response_obj.has("exteded_details"))
                            {
                                JSONArray array=response_obj.getJSONArray("exteded_details");
                                if(array.length()>0)
                                {
                                    for(int i=0;i<array.length();i++)
                                    {
                                        JSONObject obj=array.getJSONObject(i);
                                        Extend_details_bean bean=new Extend_details_bean();
                                        bean.setDate_from(obj.getString("date_from"));
                                        bean.setDate_to(obj.getString("date_to"));
                                        bean.setNo_of_days(obj.getString("no_of_days"));
                                        bean.setTotal_amount(obj.getString("total_amount"));
                                        arrayList.add(bean);

                                    }
                                }
                            }

                        }

                        if(arrayList.size()>0)
                        {
                            Extent_details_adapter adapter=new Extent_details_adapter(Extent_Details.this,arrayList,"");
                            lit_view.setAdapter(adapter);
                            lit_view.setExpanded(true);
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
}
