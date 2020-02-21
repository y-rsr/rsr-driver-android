package com.ridesharerental.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ridesharerental.adapter.Ambasador_Rank_Adapter;
import com.ridesharerental.pojo.Rank_Pojo;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;

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
 * Created by user65 on 2/20/2018.
 */

public class Rank extends Fragment
{
    private ActionBar actionBar;
    Context context;

    RecyclerView recyclerView;
    ArrayList<Rank_Pojo> arrayList_Transaction = new ArrayList<Rank_Pojo>();


    Common_Loader Loader;
    SessionManager sessionManager;
    String user_id = "";


    TextView txt_no_data;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState)

    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.ambasador_transaction, container, false);
        context = getActivity();
        Loader=new Common_Loader(context);
        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(rootView);
        load_Rank();



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                load_Rank();
            }
        });

        return rootView;
    }


    public void init(View rootview) {

        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycle_skiper_img);
        txt_no_data=(TextView)rootview.findViewById(R.id.txt_no_data);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootview.findViewById(R.id.activity_main_swipe_refresh_layout);
    }


    public void load_Rank()
    {
        Loader.show();
        arrayList_Transaction.clear();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.ambasador_program(header);
        System.out.println("--------Ambasador Program---->" + call.request().url().toString() + "?commonId=" + user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response:Bounu " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1"))
                        {
                            JSONArray array_transaction=response_obj.getJSONArray("rankchanges");
                            if(array_transaction.length()>0)
                            {
                                for(int i=0;i<array_transaction.length();i++)
                                {
                                    JSONObject object_new=array_transaction.getJSONObject(i);
                                    Rank_Pojo bean=new Rank_Pojo();
                                    bean.setId(object_new.getString("id"));
                                    bean.setRank(object_new.getString("rank"));
                                    bean.setDateAdded(object_new.getString("dateAdded"));
                                    bean.setType(object_new.getString("type"));
                                    arrayList_Transaction.add(bean);


                                }
                            }




                            if(arrayList_Transaction.size()>0)
                            {

                                recyclerView.setVisibility(View.VISIBLE);
                                txt_no_data.setVisibility(View.GONE);
                                LinearLayoutManager
                                        horizontalLayoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                recyclerView.setHasFixedSize(true);
                                Ambasador_Rank_Adapter adapter_recycler = new Ambasador_Rank_Adapter(context, arrayList_Transaction);
                                recyclerView.setAdapter(adapter_recycler);

                           /* SnapHelper snapHelper = new PagerSnapHelper();
                            try {
                                snapHelper.attachToRecyclerView(Recycle_Active);
                            } catch (Exception e) {

                            }*/
                                adapter_recycler.notifyDataSetChanged();
                                recyclerView.setLayoutManager(horizontalLayoutManagaer);
                            }
                            else
                            {
                                txt_no_data.setText("No Rank");
                                txt_no_data.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }


                            if (mSwipeRefreshLayout.isRefreshing() == true)
                            {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }



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


        Loader.dismiss();

    }
}
