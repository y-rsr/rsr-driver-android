package com.ridesharerental.app;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.adapter.Inbox_Adapter;
import com.ridesharerental.pojo.Inbox_Bean;
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
 * Created by user65 on 12/11/2017.
 */

public class Inbox extends Fragment {
    private ActionBar actionBar;
    private ListView MyInboxLIST;
    private ArrayList<Inbox_Bean> MyInboxARR;

    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Common_Loader Loader;

    SessionManager sessionManager;
    String user_id = "";
    TextView txt_no_data;
    Uread_count unread_message_count;
    String str_msg_coutn="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
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

        MyInboxLIST.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (MyInboxARR.size() > 0) {
                    if(MyInboxARR.get(i).getBooking_no()!=null && !MyInboxARR.get(i).getBooking_no().equals(""))
                    {
                        Intent val = new Intent(getActivity(), Chat_detail.class);
                        val.putExtra("booking_no", MyInboxARR.get(i).getBooking_no());
                        val.putExtra("sender_name", MyInboxARR.get(i).getSender_name());
                        startActivity(val);
                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                    else
                    {
                        Toast.makeText(context,context.getResources().getString(R.string.invalid_booking),Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        return rootView;
    }


    private void init(View rootView) {

        MyInboxARR = new ArrayList<Inbox_Bean>();
        MyInboxLIST = (ListView) rootView.findViewById(R.id.fragment_inbox_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        txt_no_data=(TextView)rootView.findViewById(R.id.txt_no_data);
        txt_no_data.setVisibility(View.GONE);

    }


    //-----------------service---------------------
    public void Load_Data() {
        Loader.show();
        MyInboxARR.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.list_inbox(header);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
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
                        JSONObject response_obj = object.getJSONObject("responseArr");




                        String status_code = response_obj.getString("status");
                        if (status_code.equals("1"))
                        {
                            JSONObject common_Obj=object.getJSONObject("commonArr");

                            String str_profile_image = common_Obj.getString("profile_pic");
                            sessionManager.set_profile(str_profile_image);

                             str_msg_coutn=common_Obj.getString("unread_message_count");
                            if(unread_message_count!=null)
                            {
                                if(str_msg_coutn!=null && !str_msg_coutn.equals("") && !str_msg_coutn.equals(null) && !str_msg_coutn.equals("0"))
                                {
                                    unread_message_count.aMethod(str_msg_coutn);
                                }
                                else
                                {
                                    unread_message_count.aMethod("");
                                }

                                System.out.println("-------put message count interface-------->");
                            }


                            sessionManager.set_msg_count(str_msg_coutn);

                            System.out.println("--------session count---------->"+str_msg_coutn);


                            JSONArray array_message = response_obj.getJSONArray("messages");
                            if (array_message.length() > 0)
                            {
                                for (int i = 0; i < array_message.length(); i++) {
                                    JSONObject obj = array_message.getJSONObject(i);
                                    Inbox_Bean bean = new Inbox_Bean();
                                    bean.setBooking_no(obj.getString("booking_no"));
                                    bean.setSender_pic(obj.getString("sender_pic"));
                                    bean.setSender_name(obj.getString("sender_name"));
                                    bean.setCar_make(obj.getString("car_make"));
                                    bean.setCar_model(obj.getString("car_model"));
                                    bean.setYear(obj.getString("year"));
                                    bean.setDateAdded(obj.getString("dateAdded"));
                                    bean.setRead_status(obj.getString("read_status"));
                                    MyInboxARR.add(bean);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }

                        if (MyInboxARR.size() > 0)
                        {
                            txt_no_data.setVisibility(View.GONE);
                            MyInboxLIST.setVisibility(View.VISIBLE);
                            MyInboxLIST.setAdapter(new Inbox_Adapter(getActivity(), MyInboxARR));
                            Loader.dismiss();
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
                } catch (Exception e) {
                    e.printStackTrace();
                    txt_no_data.setVisibility(View.VISIBLE);
                    MyInboxLIST.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            unread_message_count = (Uread_count) activity;
            if(str_msg_coutn!=null && !str_msg_coutn.equals("") && !str_msg_coutn.equals(null) && !str_msg_coutn.equals("0"))
            {
                unread_message_count.aMethod(str_msg_coutn);
                System.out.println("------interface-unread count------>"+str_msg_coutn);
            }
            else
            {
                unread_message_count.aMethod("");
            }
            System.out.println("------Interface object created--------->");
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
