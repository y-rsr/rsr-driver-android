package com.ridesharerental.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ridesharerental.adapter.TransactionAdapter;
import com.ridesharerental.pojo.My_Transaction_Bean;
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
 * Created by user65 on 11/29/2017.
 */

public class My_Transactions extends Fragment {
    private ActionBar actionBar;
    private ExpandableHeightListView myTransactionList;
    private ArrayList<My_Transaction_Bean> myTransactionARR;
    Context context;
    Common_Loader Loader;
    TransactionAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    SessionManager sessionManager;
    String user_id = "";

    RelativeLayout Rel_Nodata;
    RelativeLayout Rel_find_car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.my_transations_layout, container, false);
        context = getActivity();
        Loader = new Common_Loader(context);


        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(rootView);
        Load_Data();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load_Data();
            }
        });

        Rel_find_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent val = new Intent(context, Main_homepage.class);
                val.putExtra("calling_type","findcar");
                context.startActivity(val);
            }
        });

        return rootView;
    }

    private void init(View rootView) {
        Rel_Nodata = (RelativeLayout) rootView.findViewById(R.id.rel_no_data);
        Rel_find_car=(RelativeLayout)rootView.findViewById(R.id.rel_next);

        myTransactionARR = new ArrayList<My_Transaction_Bean>();
        myTransactionARR.clear();
        myTransactionList = (ExpandableHeightListView) rootView.findViewById(R.id.my_transaction_layout_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
    }

    //-----------------service---------------------
    public void Load_Data() {
        Loader = new Common_Loader(context);
        Loader.show();
        Loader.secCancellable(false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.My_Transaction(header);
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
                        myTransactionARR.clear();
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String status_code = response_obj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray = response_obj.getJSONArray("transactions");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    My_Transaction_Bean bean = new My_Transaction_Bean();
                                    bean.setBooking_no(obj.getString("booking_no"));
                                    bean.setNo_of_days(obj.getString("no_of_days"));
                                    bean.setExtended(obj.getString("extended"));
                                    bean.setDateAdded(obj.getString("dateAdded"));
                                    bean.setRental_cost(obj.getString("rental_cost"));
                                    bean.setCarInfo(obj.getString("carInfo"));
                                    bean.setTotal_amount(obj.getString("total_amount"));
                                    bean.setExtendDetails(obj.getJSONArray("extendDetails"));
                                    myTransactionARR.add(bean);
                                }
                            }
                        }

                        if (mSwipeRefreshLayout.isRefreshing() == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (myTransactionARR.size() > 0) {
                            Rel_Nodata.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            adapter = new TransactionAdapter(getActivity(), myTransactionARR);
                            myTransactionList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Loader.dismiss();
                        } else {
                            Rel_Nodata.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                        }

                    } else {
                        if (mSwipeRefreshLayout.isRefreshing() == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        Rel_Nodata.setVisibility(View.VISIBLE);
                        mSwipeRefreshLayout.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Rel_Nodata.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    if (mSwipeRefreshLayout.isRefreshing() == true) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
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
}
