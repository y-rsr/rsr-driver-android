package com.ridesharerental.app;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ridesharerental.adapter.Contract_History_Adapter;
import com.ridesharerental.pojo.Contract_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 1/22/2018.
 */

public class Contract_History extends Fragment
{
    Context context;
    private ActionBar actionBar;
    Common_Loader loader;

    ListView MyInboxLIST;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView txt_no_data;
    ImageView img_filter, img_search, img_close, vehicle_add_im,img_city;
    EditText autoCompleteTextView_search;

    SessionManager sessionManager;
    String user_id = "";

    Contract_History_Adapter adapter;

    ArrayList<Contract_Bean> arrayList=new ArrayList<Contract_Bean>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.contract_history, container, false);
        context = getActivity();
        loader = new Common_Loader(context);

        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(rootView);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load_contract_History();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                img_close.setVisibility(View.VISIBLE);
                img_search.setVisibility(View.GONE);
                if (autoCompleteTextView_search.getVisibility() == View.VISIBLE) {
                    autoCompleteTextView_search.setVisibility(View.GONE);
                } else {
                    autoCompleteTextView_search.setVisibility(View.VISIBLE);
                }
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_close.setVisibility(View.GONE);
                autoCompleteTextView_search.setText("");
                autoCompleteTextView_search.setVisibility(View.GONE);
                img_search.setVisibility(View.VISIBLE);
            }
        });

        autoCompleteTextView_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = autoCompleteTextView_search.getText().toString().toLowerCase(Locale.getDefault());


                if(adapter != null){


                    adapter.filter(text);


                }





            }
        });
        return rootView;
    }


    public void init(View rootView)
    {
        MyInboxLIST = (ListView) rootView.findViewById(R.id.fragment_inbox_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        txt_no_data=(TextView)rootView.findViewById(R.id.txt_no_data);
        txt_no_data.setVisibility(View.GONE);
        img_search = (ImageView) rootView.findViewById(R.id.img_search);
        img_close = (ImageView) rootView.findViewById(R.id.img_close);
        img_close.setVisibility(View.GONE);

        autoCompleteTextView_search = (EditText) rootView.findViewById(R.id.auto_search);
        autoCompleteTextView_search.setVisibility(View.GONE);
        autoCompleteTextView_search.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/Sofia Pro Regular.ttf"));
        load_contract_History();
    }



    public void load_contract_History()
    {
        loader.show();

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.contract_history(header);
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
                        arrayList.clear();
                        JSONObject Response_obj=object.getJSONObject("responseArr");
                        String str_status_code=Response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            JSONArray array=Response_obj.getJSONArray("contract_history");
                            if(array.length()>0)
                            {
                             for(int i=0;i<array.length();i++)
                             {
                                 JSONObject obj=array.getJSONObject(i);
                                 Contract_Bean bean=new Contract_Bean();
                                 bean.setDocuments(obj.getJSONArray("documents"));
                                 bean.setDate_from(obj.getString("date_from"));
                                 bean.setDate_to(obj.getString("date_to"));;
                                 bean.setTotal_amount(obj.getString("total_amount"));;
                                 bean.setCar_make(obj.getString("car_make"));
                                 bean.setCar_model(obj.getString("car_model"));
                                 bean.setYear(obj.getString("year"));
                                 bean.setVin_no(obj.getString("vin_no"));
                                 bean.setPlat_no(obj.getString("plat_no"));
                                 bean.setOwnername(obj.getString("ownername"));
                                 bean.setRating(obj.getString("rating"));
                                 arrayList.add(bean);
                                 //mSwipeRefreshLayout.setRefreshing(false);
                             }
                            }

                        }
                        else
                        {
                            txt_no_data.setVisibility(View.VISIBLE);
                            MyInboxLIST.setVisibility(View.GONE);
                        }


                        if(arrayList.size()>0)
                        {
                            txt_no_data.setVisibility(View.GONE);
                            MyInboxLIST.setVisibility(View.VISIBLE);
                            adapter=new Contract_History_Adapter(context,arrayList);
                            MyInboxLIST.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            txt_no_data.setVisibility(View.VISIBLE);
                            MyInboxLIST.setVisibility(View.GONE);
                        }

                        if (mSwipeRefreshLayout.isRefreshing() == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                    else
                    {
                        txt_no_data.setVisibility(View.VISIBLE);
                        MyInboxLIST.setVisibility(View.GONE);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    txt_no_data.setVisibility(View.VISIBLE);
                    MyInboxLIST.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);

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
