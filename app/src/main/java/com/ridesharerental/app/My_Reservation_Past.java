package com.ridesharerental.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ridesharerental.adapter.Past_Reservation_Recycler_Adapter;
import com.ridesharerental.adapter.Reservation_Adapter;
import com.ridesharerental.adapter.Reservation_Adapter_Past;
import com.ridesharerental.pojo.Past_Reservation_Bean;
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

public class My_Reservation_Past extends Fragment implements View.OnClickListener {
    private ActionBar actionBar;
    private RelativeLayout Mynot_activeLAy, MyactiveLAy, Mynot_pastLAy, MypastLAy, Myno_dataLAY;
    private Button Myno_dataBTN;
    private ArrayList<Past_Reservation_Bean> My_reservationARR;
    private ExpandableHeightListView My_listDATA, My_listDATA_Past;
    private Context Mycontext;
    Reservation_Adapter MyreservationAdapt;
    Reservation_Adapter_Past MyreservationAdapt_Past;
    Common_Loader Loader;
    Context context;
   SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView Recycle_Active;
    Past_Reservation_Recycler_Adapter adapter_recycler;



    SessionManager sessionManager;
    String user_id="";

    RelativeLayout Rel_data_contain,Rel_no_data;
    RelativeLayout Rel_find_car;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.past_reservation_layout, container, false);
        context = getActivity();
        Loader = new Common_Loader(context);

        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);
        init(rootView);
        Load_Data();



        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                Load_Data();
            }
        });


        Rel_find_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent val = new Intent(context, Main_homepage.class);
                val.putExtra("calling_type","findcar");
                startActivity(val);
            }
        });

        return rootView;
    }


    private void init(View rootView) {
 /*---------- mapping --------*/
        Mycontext = getActivity();
        My_reservationARR = new ArrayList<Past_Reservation_Bean>();

        My_reservationARR.clear();
          /*----------   ListView --------*/

        Rel_data_contain=(RelativeLayout)rootView.findViewById(R.id.rel_data_contain);
        Rel_no_data=(RelativeLayout)rootView.findViewById(R.id.rel_no_data);
        Rel_find_car=(RelativeLayout)rootView.findViewById(R.id.rel_next);

        Recycle_Active=(RecyclerView)rootView.findViewById(R.id.recycle_skiper_img);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        My_listDATA = (ExpandableHeightListView) rootView.findViewById(R.id.my_reservation_layout_listdata);
        My_listDATA_Past = (ExpandableHeightListView) rootView.findViewById(R.id.my_reservation_layout_listdata1);

        My_listDATA.setVisibility(View.VISIBLE);
        My_listDATA_Past.setVisibility(View.GONE);


         /*----------   Button --------*/
        Myno_dataBTN = (Button) rootView.findViewById(R.id.my_reservation_layout_nodataBTN);

        /*----------   relative layout  --------*/
        Mynot_activeLAy = (RelativeLayout) rootView.findViewById(R.id.my_reservation_layout_notactiveLAY);
        MyactiveLAy = (RelativeLayout) rootView.findViewById(R.id.my_reservation_layout_activeLAY);
        Mynot_pastLAy = (RelativeLayout) rootView.findViewById(R.id.my_reservation_layout_nopastLAY);
        MypastLAy = (RelativeLayout) rootView.findViewById(R.id.my_reservation_layout_pastLAY);
        Myno_dataLAY = (RelativeLayout) rootView.findViewById(R.id.my_reservation_layout_nodataLay);

          /*----------   Visibilty  --------*/

        Myno_dataLAY.setVisibility(View.GONE);
        My_listDATA.setVisibility(View.VISIBLE);


          /*----------   on Click listener --------*/
        Mynot_activeLAy.setOnClickListener(this);
        Mynot_pastLAy.setOnClickListener(this);
        Myno_dataBTN.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.my_reservation_layout_notactiveLAY:

                Mynot_activeLAy.setVisibility(View.GONE);
                MyactiveLAy.setVisibility(View.VISIBLE);
                Mynot_pastLAy.setVisibility(View.VISIBLE);
                MypastLAy.setVisibility(View.GONE);

                /*MyreservationAdapt = new Reservation_Adapter(Mycontext, My_reservationARR);
                My_listDATA.setAdapter(MyreservationAdapt);
                My_listDATA.setExpanded(true);
                MyreservationAdapt.notifyDataSetChanged();*/
                My_listDATA.setVisibility(View.VISIBLE);
                My_listDATA_Past.setVisibility(View.GONE);
                Loader.show();

                break;

            case R.id.my_reservation_layout_nopastLAY:

                Mynot_activeLAy.setVisibility(View.VISIBLE);
                MyactiveLAy.setVisibility(View.GONE);
                Mynot_pastLAy.setVisibility(View.GONE);
                MypastLAy.setVisibility(View.VISIBLE);

               /* MyreservationAdapt_Past = new Reservation_Adapter_Past(Mycontext, My_reservationARR);
                My_listDATA_Past.setAdapter(MyreservationAdapt_Past);
                My_listDATA_Past.setExpanded(true);
                MyreservationAdapt_Past.notifyDataSetChanged();*/
                My_listDATA.setVisibility(View.GONE);
                My_listDATA_Past.setVisibility(View.VISIBLE);
                Loader.show();
                break;

            case R.id.my_reservation_layout_nodataBTN:
                Intent val = new Intent(getActivity(), Main_homepage.class);
                startActivity(val);
                break;
        }
    }


    //-----------------service---------------------
    public void Load_Data()
    {
        Loader.show();
        Loader.secCancellable(false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId",user_id);
        Call<ResponseBody> call = apiService.Reservations_check(header);

        System.out.println("-----------my reservations url------>"+call.request().url().toString()+"?commonId="+user_id);

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
                        Loader.dismiss();
                        My_reservationARR.clear();
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            JSONArray  obj_active_reservations=response_obj.getJSONArray("past_reservations");
                            if(obj_active_reservations.length()>0)
                            {
                                System.out.println("------Past Reservations----->"+obj_active_reservations.toString());
                                for(int i=0;i<obj_active_reservations.length();i++)
                                {
                                    JSONObject obj=obj_active_reservations.getJSONObject(i);
                                    Past_Reservation_Bean bean=new Past_Reservation_Bean();
                                    bean.setCarId(obj.getString("carId"));
                                    bean.setPlat_no(obj.getString("plat_no"));
                                    bean.setCar_image(obj.getString("car_image"));
                                    bean.setCar_make(obj.getString("car_make"));
                                    bean.setCar_model(obj.getString("car_model"));
                                    bean.setYear(obj.getString("year"));
                                    bean.setProfile_pic(obj.getString("profile_pic"));
                                    bean.setDate_from(obj.getString("date_from"));
                                    bean.setDate_to(obj.getString("date_to"));
                                    bean.setTotal_amount(obj.getString("total_amount"));
                                    bean.setVin_no(obj.getString("vin_no"));
                                    bean.setOwnername(obj.getString("ownername"));
                                    bean.setStatus(obj.getString("status"));
                                    bean.setStreet(obj.getString("street"));
                                    bean.setV_no(obj.getString("v_no"));
                                    bean.setNotes(obj.getString("notes"));
                                    bean.setBooking_no(obj.getString("booking_no"));


                                    My_reservationARR.add(bean);
                                }
                            }
                        }

                        if(mSwipeRefreshLayout.isRefreshing()==true)
                        {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (My_reservationARR.size() > 0)
                        {
                            Rel_data_contain.setVisibility(View.VISIBLE);
                            Rel_no_data.setVisibility(View.GONE);
                            LinearLayoutManager horizontalLayoutManagaer
                                    = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            Recycle_Active.setHasFixedSize(false);
                            adapter_recycler = new Past_Reservation_Recycler_Adapter(context, My_reservationARR);
                            Recycle_Active.setAdapter(adapter_recycler);

                            /*SnapHelper snapHelper = new PagerSnapHelper();
                            try {
                                snapHelper.attachToRecyclerView(Recycle_Active);
                            } catch (Exception e) {

                            }*/
                            adapter_recycler.notifyDataSetChanged();
                            Recycle_Active.setLayoutManager(horizontalLayoutManagaer);
                        }
                        else
                        {
                            Rel_data_contain.setVisibility(View.GONE);
                            Rel_no_data.setVisibility(View.VISIBLE);
                        }

                    }
                    else
                    {
                        Rel_data_contain.setVisibility(View.GONE);
                        Rel_no_data.setVisibility(View.VISIBLE);
                        Loader.dismiss();
                    }
                } catch (Exception e)
                {
                    Rel_data_contain.setVisibility(View.GONE);
                    Rel_no_data.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                    Loader.dismiss();
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
    public void onDestroy() {
        Log.w("MyIntentService", "onDestroy callback called");
        super.onDestroy();
    }
}
