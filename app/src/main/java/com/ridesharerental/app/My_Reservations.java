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

import com.ridesharerental.adapter.Active_Reservation_Recycler_Adapter;
import com.ridesharerental.adapter.Reservation_Adapter;
import com.ridesharerental.adapter.Reservation_Adapter_Past;
import com.ridesharerental.pojo.Active_Reservation_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 11/29/2017.
 */

public class My_Reservations extends Fragment implements View.OnClickListener {
    private ActionBar actionBar;
    private RelativeLayout Mynot_activeLAy, MyactiveLAy, Mynot_pastLAy, MypastLAy, Myno_dataLAY;
    private Button Myno_dataBTN;
    private ArrayList<Active_Reservation_Bean> My_reservationARR;
    private ExpandableHeightListView My_listDATA, My_listDATA_Past;
    private Context Mycontext;
    Reservation_Adapter MyreservationAdapt;
    Reservation_Adapter_Past MyreservationAdapt_Past;
    Common_Loader Loader;
    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerView Recycle_Active;
    Active_Reservation_Recycler_Adapter adapter_recycler;

    SessionManager sessionManager;
    String user_id = "";

    public static boolean timerstop=true;

    RelativeLayout Rel_data_contain, Rel_no_data;
    RelativeLayout Rel_find_car;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.my_reservations_layout, container, false);
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
                val.putExtra("calling_type", "findcar");
                startActivity(val);
            }
        });

        return rootView;
    }


    private void init(View rootView) {
 /*---------- mapping --------*/
        Mycontext = getActivity();
        My_reservationARR = new ArrayList<Active_Reservation_Bean>();

        My_reservationARR.clear();
          /*----------   ListView --------*/


        Rel_data_contain = (RelativeLayout) rootView.findViewById(R.id.rel_data_contain);
        Rel_no_data = (RelativeLayout) rootView.findViewById(R.id.rel_no_data);
        Rel_find_car = (RelativeLayout) rootView.findViewById(R.id.rel_next);

        Recycle_Active = (RecyclerView) rootView.findViewById(R.id.recycle_skiper_img);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        My_listDATA = (ExpandableHeightListView) rootView.findViewById(R.id.my_reservation_layout_listdata);
        My_listDATA_Past = (ExpandableHeightListView) rootView.findViewById(R.id.my_reservation_layout_listdata1);

        My_listDATA.setVisibility(View.GONE);
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
        switch (view.getId()) {
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
    public void Load_Data() {
        Loader.show();
        Loader.secCancellable(false);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.Reservations_check(header);
        System.out.println("-----Reservatio url----->"+call.request().url().toString());
        System.out.println("------User Id------>"+user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //String s = "[{\"id\":\"Registration Certificate\",\"name\":\"http:\\/\\/rentals.zoplay.com\\/ridesharerental.com\\/images\\/car_documents\\/1513853361-1356x800.jpg\"},{\"id\":\"Insurance Document\",\"name\":\"http:\\/\\/rentals.zoplay.com\\/ridesharerental.com\\/images\\/car_documents\\/1524237718-boat2.jpeg\"}]";
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
                        Log.e("----Reservation-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        Loader.dismiss();
                        My_reservationARR.clear();
                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1")) {
                            JSONArray obj_active_reservations = response_obj.getJSONArray("active_reservations");

                            System.out.println("----Active Reservation-------->"+obj_active_reservations);
                            if (obj_active_reservations.length() > 0)
                            {
                                for (int i = 0; i < obj_active_reservations.length(); i++)
                                {
                                    JSONObject obj = obj_active_reservations.getJSONObject(i);
                                    Active_Reservation_Bean bean = new Active_Reservation_Bean();
                                    bean.setCarId(obj.getString("carId"));
                                    bean.setPlat_no(obj.getString("plat_no"));
                                    bean.setCar_make(obj.getString("car_make"));
                                    bean.setCar_model(obj.getString("car_model"));
                                    bean.setYear(obj.getString("year"));
                                    bean.setStatus(obj.getString("status"));
                                    bean.setCar_image(obj.getString("car_image"));
                                    bean.setProfile_pic(obj.getString("profile_pic"));
                                    bean.setDate_from(obj.getString("date_from"));
                                    bean.setDate_to(obj.getString("date_to"));
                                    bean.setTotal_amount(obj.getString("total_amount"));
                                    bean.setTimer_date(obj.getString("timer_date"));
                                    bean.setVin_no(obj.getString("vin_no"));
                                    bean.setProgress(obj.getString("progress"));
                                    bean.setOwnername(obj.getString("ownername"));
                                    bean.setStreet(obj.getString("street"));
                                    bean.setNo_of_days(obj.getString("no_of_days"));
                                    bean.setBooking_no(obj.getString("booking_no"));
                                    bean.setAllow_extend(obj.getString("allow_extend"));
                                    bean.setExtended(obj.getString("extended"));
                                    bean.setV_no(obj.getString("v_no"));
                                    bean.setNotes(obj.getString("notes"));
                                    bean.setDocuments(obj.getJSONArray("documents"));
                                    if(!obj.isNull("bookingDocs")  )
                                        bean.setBooking_documents(obj.getJSONArray("bookingDocs"));
//                                    else
//                                        bean.setBooking_documents(new JSONArray(s));
                                    bean.setExtended_date(obj.getString("extended_date"));

                                    bean.setStr_timer_counting("");


                                    /*Thread myThread = null;
                                    Runnable myRunnableThread = new CountDownRunner(obj.getString("timer_date"),bean);

                                    myThread = new Thread(myRunnableThread);
                                    myThread.start();*/

                                    My_reservationARR.add(bean);

                                    timerstop=true;


                                }
                            }else {

                                timerstop=false;

                            }

                        }


                       /* JSONArray obj_listing_arr=object.getJSONArray("responseArr");

                        if(obj_listing_arr.length()>0)
                        {
                            for(int i=0;i<obj_listing_arr.length();i++)
                            {
                                JSONObject obj=obj_listing_arr.getJSONObject(i);
                                Reservation_pojo pojo = new Reservation_pojo();
                                pojo.setCar_name("Toyato-corella 2008");
                                pojo.setEnd_date("14/10/17");
                                pojo.setStart_date("14/10/18");
                                pojo.setUsername("James");
                                pojo.setPrice(obj.getString("key"));
                                pojo.setStatus("Active");
                                My_reservationARR.add(pojo);
                            }
                        }*/

                        if (mSwipeRefreshLayout.isRefreshing() == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        if (My_reservationARR.size() > 0)
                        {

                            Rel_data_contain.setVisibility(View.VISIBLE);
                            Rel_no_data.setVisibility(View.GONE);
                            LinearLayoutManager horizontalLayoutManagaer
                                    = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            Recycle_Active.setHasFixedSize(true);
                            adapter_recycler = new Active_Reservation_Recycler_Adapter(context, My_reservationARR, "",getActivity());
                            Recycle_Active.setAdapter(adapter_recycler);

                            /*SnapHelper snapHelper = new PagerSnapHelper();
                            try {
                                snapHelper.attachToRecyclerView(Recycle_Active);
                            } catch (Exception e) {

                            }*/
                            adapter_recycler.notifyDataSetChanged();
                            Recycle_Active.setLayoutManager(horizontalLayoutManagaer);
                        } else {
                            Rel_data_contain.setVisibility(View.GONE);
                            Rel_no_data.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Rel_data_contain.setVisibility(View.GONE);
                        Rel_no_data.setVisibility(View.VISIBLE);
                        Loader.dismiss();
                        if (mSwipeRefreshLayout.isRefreshing() == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                } catch (Exception e) {
                    Rel_data_contain.setVisibility(View.GONE);
                    Rel_no_data.setVisibility(View.VISIBLE);
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
    public void onDestroy() {


       if(My_reservationARR.size() > 0){

           adapter_recycler.stoptimer();

        }

        System.out.println("--------kannan destory--------");

        Log.w("MyIntentService", "onDestroy callback called");
        super.onDestroy();
    }


    public void doWork(final String str_to_date,final Active_Reservation_Bean bean)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            public void run() {
                try {
                    Date current_date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String DateToStr = format.format(current_date);
                    System.out.println("--------Display current date and time---------->" + DateToStr);
                    Date reach_date = format.parse(str_to_date);
                    printDifference(current_date, reach_date,bean);

                } catch (Exception e) {
                }
            }
        });
    }


    class CountDownRunner implements Runnable
    {
        String str_curren_date = "";
        Active_Reservation_Bean bean_data;

        public CountDownRunner(String to_date,Active_Reservation_Bean bean) {
            this.str_curren_date = to_date;
            this.bean_data=bean;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork(str_curren_date,bean_data);
                    //Thread.sleep(1000); // Pause of 1 Second
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }


    public void printDifference(Date startDate, Date endDate,Active_Reservation_Bean bean)
    {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String timer = elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + " s";
        System.out.println("------Timer------->" + timer);
        bean.setStr_timer_counting(timer);
        if(elapsedDays == 0)
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.add(Calendar.HOUR, 23);
            different = endDate.getTime() - startDate.getTime();
             elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

             elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

             elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

             elapsedSeconds = different / secondsInMilli;

             timer = elapsedDays + "d " + elapsedHours + "h " + elapsedMinutes + "m " + elapsedSeconds + " s";
            bean.setStr_timer_counting(timer);

        }

        My_reservationARR.add(bean);


    }
}
