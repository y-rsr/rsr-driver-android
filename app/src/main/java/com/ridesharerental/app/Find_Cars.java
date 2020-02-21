package com.ridesharerental.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.adapter.Discover_Adapter;
import com.ridesharerental.pojo.Find_Car_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.gps.GPSTracker;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ridesharerental.app.R.id.txt_location;

/**
 * Created by user65 on 11/29/2017.
 */

public class Find_Cars extends Fragment {
    private ActionBar actionBar;
    private ListView List_cars;
    public ArrayList<Find_Car_Bean> arrayList_City = new ArrayList<Find_Car_Bean>();
    public Discover_Adapter adapter;
    Context ctx;
    TextView txt_more_filter;

    Common_Loader Loader;
    ImageView img_location_list;
    public static int currentPosition = 0;

    RelativeLayout Rel_Location, Rel_Check_In, Rel_Check_Out;
    TextView Txt_Location, Txt_check_In, Txt_Check_Out;
    private ArrayList<Date> array_avail = new ArrayList<Date>();


    private CaldroidFragment dialogCaldroidFragment_start, dialogCaldroidFragment_end;
    Date date_start1;
    String p_select_start = "", p_select_end = "";
    final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    SwipeRefreshLayout mSwipeRefreshLayout;
    GPSTracker gpsTracker;
    double current_lat = 0.0, current_lang = 0.0;

    SessionManager sessionManager;
    String user_id = "";
    String str_search_Location = "";
    TextView txt_near_car_count;
    boolean default_location = false;
    String str_get_address = "Los Angeles,CA,United States", str_get_car_make = "", str_get_model = "";
    boolean isLoading = false;

    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.find_cars_layout, container, false);
        ctx = getActivity();

        sessionManager = new SessionManager(ctx);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        Loader = new Common_Loader(ctx);
        gpsTracker = new GPSTracker(ctx);
        cd = new ConnectionDetector(ctx);
        isInternetPresent = cd.isConnectingToInternet();

        Bundle arguments = getArguments();
        if (arguments != null) {
            str_get_address = arguments.getString("address");
            str_get_car_make = arguments.getString("car_make");
            str_get_model = arguments.getString("car_model");
            System.out.println("-----desired_Address------->" + str_get_address);
            System.out.println("-----desired_Car_Make------->" + str_get_car_make);
            System.out.println("-----desired_Car Model------->" + str_get_model);
        }



        if(gpsTracker.getLocation() != null && gpsTracker.getLocation()!= null){
        current_lat = gpsTracker.getLocation().getLatitude();
        current_lang = gpsTracker.getLocation().getLongitude();
        }else
        {
            current_lat = 13.0574021;
                    current_lang = 80.25325;
        }
        init(rootView);


        if (isInternetPresent) {
            if (!default_location) {
                str_search_Location = "";
                if (current_lat != 0.0) {
                    currecnt_location(current_lat, current_lang);
                }
            }
        } else {
            Alert(getResources().getString(R.string.action_no_internet_title), getResources().getString(R.string.action_no_internet_message));
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        List_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)

            {

//               Intent in=new Intent(getActivity(),Dummy.class);
//                startActivity(in);
//                 getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);




                if (arrayList_City.size() > 0) {


                    Intent val = new Intent(getActivity(), CarDetailPage.class);
                    val.putExtra("CarId", arrayList_City.get(position).getId().toString());
                    val.putExtra("wishlist", arrayList_City.get(position).getWishlist().toString());
                    val.putExtra("position", ""+position);
                    if (Txt_check_In.getText().toString().length() > 0) {
                        val.putExtra("check_in", Txt_check_In.getText().toString());
                    }
                    if (Txt_Check_Out.getText().toString().length() > 0) {
                        val.putExtra("check_out", Txt_Check_Out.getText().toString());
                    }
                    currentPosition = position;
                    startActivity(val);
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });


        // List_cars.setOnScrollListener(scrollListener);

        txt_more_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent val = new Intent(getActivity(), AdvanceFilter.class);
                val.putExtra("location", Txt_Location.getText().toString());
                startActivityForResult(val, 101);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        Rel_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(ctx, Search_Google_Places.class);
                startActivityForResult(new_intent, 100);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        Rel_Check_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Start_Date_Picker(savedInstanceState);
                setdate_start();

            }
        });

        Rel_Check_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //End_Date_Picker(savedInstanceState);
                setdate_end();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load_Data(0);
            }
        });

        img_location_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(ctx, Find_Car_Map_List.class);
                new_intent.putExtra("location", str_search_Location);
                startActivityForResult(new_intent, 100);
                getActivity().overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
            }
        });


        return rootView;
    }


    public void init(View rootvView) {


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootvView.findViewById(R.id.activity_main_swipe_refresh_layout);


        txt_near_car_count = (TextView) rootvView.findViewById(R.id.txt_near_cars);
        txt_more_filter = (TextView) rootvView.findViewById(R.id.txt_more_filter);

        Rel_Location = (RelativeLayout) rootvView.findViewById(R.id.rel_location);
        Rel_Check_In = (RelativeLayout) rootvView.findViewById(R.id.rel_check_In);
        Rel_Check_Out = (RelativeLayout) rootvView.findViewById(R.id.rel_check_Out);

        Txt_Location = (TextView) rootvView.findViewById(txt_location);
        Txt_check_In = (TextView) rootvView.findViewById(R.id.txt_check_In);
        Txt_Check_Out = (TextView) rootvView.findViewById(R.id.txt_check_Out);

        img_location_list = (ImageView) rootvView.findViewById(R.id.img_map_location);
        List_cars = (ListView) rootvView.findViewById(R.id.list_cars);






       /* LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_layout, List_cars, false);
        txt_near_car_count = (TextView) header.findViewById(R.id.txt_near_cars);
        txt_more_filter = (TextView) header.findViewById(R.id.txt_more_filter);

        Rel_Location = (RelativeLayout) header.findViewById(R.id.rel_location);
        Rel_Check_In = (RelativeLayout) header.findViewById(R.id.rel_check_In);
        Rel_Check_Out = (RelativeLayout) header.findViewById(R.id.rel_check_Out);

        Txt_Location = (TextView) header.findViewById(txt_location);
        Txt_check_In = (TextView) header.findViewById(R.id.txt_check_In);
        Txt_Check_Out = (TextView) header.findViewById(R.id.txt_check_Out);

        List_cars.addHeaderView(header, null, false); */
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                arrayList_City.clear();
                String location_name = data.getStringExtra("location_name");
                str_search_Location = location_name;
                String latitude = data.getStringExtra("latitude");
                String langitude = data.getStringExtra("langitude");
                default_location = false;
                Txt_Location.setText("" + location_name);
                if (str_search_Location != null && !str_search_Location.equals("")) {
                    Load_Data(0);
                }
                System.out.println("----str_search_Location  Name------->" + str_search_Location);
                System.out.println("---Latitude-------->" + latitude);
                System.out.println("---Langitude-------->" + langitude);
            } else if (requestCode == 101) {
                arrayList_City.clear();
                String location_name = data.getStringExtra("filter_data");
                System.out.println("-------filter data------->" + location_name.toString());

                try {
                    JSONObject object = new JSONObject(location_name);
                    JSONObject response_obj = object.getJSONObject("responseArr");
                    String str_status_code = response_obj.getString("status");
                    if (str_status_code.equals("1")) {
                        JSONArray jsonArray_cars_details = response_obj.getJSONArray("cars_list");
                        if (jsonArray_cars_details.length() > 0) {
                            for (int i = 0; i < jsonArray_cars_details.length(); i++) {
                                JSONObject obj = jsonArray_cars_details.getJSONObject(i);
                                Find_Car_Bean bean = new Find_Car_Bean();
                                bean.setId(obj.getString("id"));
                                bean.setCar_make(obj.getString("car_make"));
                                bean.setCar_model(obj.getString("car_model"));
                                bean.setYear(obj.getString("year"));
                                bean.setRent_daily(obj.getString("rent_daily"));
                                bean.setRent_weekly(obj.getString("rent_weekly"));
                                bean.setRent_monthly(obj.getString("rent_monthly"));
                                bean.setRating(obj.getString("rating"));
                                bean.setProfile_pic(obj.getString("profile_pic"));
                                bean.setVin_no(obj.getString("vin_no"));
                                bean.setV_no(obj.getString("v_no"));
                                bean.setWishlist(obj.getString("wishlist"));
                                bean.setTag(obj.getString("tag"));
//                                System.out.println("------Vehicle Number-json-------->"+obj.getString("v_no"));

                                JSONArray array_car_Image = obj.getJSONArray("car_images");
                                if (array_car_Image.length() > 0) {
                                    // for (int j = 0; i < array_car_Image.length(); j++)
                                    // {
                                    JSONObject obj_car_img = array_car_Image.getJSONObject(0);
                                    String car_Image = obj_car_img.getString("image");
                                    bean.setCar_image(obj_car_img.getString("image"));
                                    //  }
                                }
                                arrayList_City.add(bean);
                            }
                        }
                    }

                    if (arrayList_City.size() > 0) {
                        List_cars.setVisibility(View.VISIBLE);
                        txt_near_car_count.setText(String.valueOf(arrayList_City.size() + " " + ctx.getResources().getString(R.string.car_found)));
                        adapter = new Discover_Adapter(ctx, arrayList_City,"no");
                        List_cars.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Loader.dismiss();
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        txt_near_car_count.setText(ctx.getResources().getString(R.string.no_car_found));
                        List_cars.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    //-----------------service---------------------
    public void Load_Data(final int c_position) {
        if(c_position == 0)
            Loader.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("language", "en");
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        if (str_search_Location != null && !str_search_Location.equals("")) {
            post.put("location", str_search_Location);

            if (str_get_car_make != null && !str_get_car_make.equals("")) {
                post.put("car_make", str_get_car_make);
            }

            if (str_get_model != null && !str_get_model.equals("")) {
                post.put("car_model", str_get_model);
            }
        } else {
            if (str_get_address != null && !str_get_address.equals("")) {
                post.put("location", str_get_address);
            }

            if (str_get_car_make != null && !str_get_car_make.equals("")) {
                post.put("car_make", str_get_car_make);
            }

            if (str_get_model != null && !str_get_model.equals("")) {
                post.put("car_model", str_get_model);
            }


            System.out.println("---------Get current Location--->" + str_get_address);
        }
        Log.e("Post Params",post.toString());
        Set keys = post.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) post.get(key);

            System.out.println("" + key + ":" + value);
        }


        System.out.println("------post response-----"+post);


        // String url= IConstant_WebService.baseurl+"app/driver/find_a_car?commonId="+user_id+"&location="+str_get_address;
        // System.out.println("--------find car url------->"+url);
        Call<ResponseBody> call = apiService.find_cars(header, post);

        System.out.println("-----------find car url------>" + call.request().url().toString());
        //  Log.e(" Login url", call.request().url().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //Loader.dismiss();
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

                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1")) {


                            JSONArray jsonArray_cars_details = response_obj.getJSONArray("cars_list");
                            if (jsonArray_cars_details.length() > 0) {


                                arrayList_City.clear();

                                for (int i = 0; i < jsonArray_cars_details.length(); i++) {
                                    JSONObject obj = jsonArray_cars_details.getJSONObject(i);
                                    Find_Car_Bean bean = new Find_Car_Bean();
                                    bean.setId(obj.getString("id"));
                                    bean.setCar_make(obj.getString("car_make"));
                                    bean.setCar_model(obj.getString("car_model"));
                                    bean.setYear(obj.getString("year"));
                                    bean.setRent_daily(obj.getString("rent_daily"));
                                    bean.setRent_weekly(obj.getString("rent_weekly"));
                                    bean.setRent_monthly(obj.getString("rent_monthly"));
                                    bean.setRating(obj.getString("rating"));
                                    bean.setProfile_pic(obj.getString("profile_pic"));
                                    bean.setVin_no(obj.getString("vin_no"));
                                    bean.setV_no(obj.getString("v_no"));
                                    bean.setWishlist(obj.getString("wishlist"));
                                    bean.setTag(obj.getString("tag"));

//                                    System.out.println("------Vehicle Number-json-------->"+obj.getString("v_no"));

                                    JSONArray array_car_Image = obj.getJSONArray("car_images");
                                    if (array_car_Image.length() > 0) {
                                        // for (int j = 0; i < array_car_Image.length(); j++)
                                        // {
                                        JSONObject obj_car_img = array_car_Image.getJSONObject(0);
                                        String car_Image = obj_car_img.getString("image");
                                        bean.setCar_image(obj_car_img.getString("image"));
                                        //  }
                                    }
                                    arrayList_City.add(bean);
                                }

                            }
                        } else {
                            // Loader.dismiss();
                            txt_near_car_count.setText(ctx.getResources().getString(R.string.no_car_found));
                            List_cars.setVisibility(View.GONE);
                        }

                        /*if (default_location == false)
                        {
                            if (current_lat != 0.0)
                            {
                                currecnt_location(current_lat, current_lang);
                            }
                        }*/


//                        if (mSwipeRefreshLayout.isRefreshing())
//                        {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }

                    }


//                    if(mSwipeRefreshLayout.isRefreshing())
//                    {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
                    if(c_position == 0)
                        Loader.dismiss();

                    //
                    if (arrayList_City.size() > 0) {
                        List_cars.setVisibility(View.VISIBLE);
                        adapter = new Discover_Adapter(ctx, arrayList_City,"no");
                        List_cars.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        List_cars.setSelection(c_position);


                        if (arrayList_City.size() == 1) {

                            txt_near_car_count.setText(String.valueOf(arrayList_City.size() + " " + ctx.getResources().getString(R.string.single_car_found)));
                            isLoading = true;
                        } else {
                            txt_near_car_count.setText(String.valueOf(arrayList_City.size() + " " + ctx.getResources().getString(R.string.car_found)));
                            isLoading = true;
                        }

                    } else {
                        txt_near_car_count.setText(ctx.getResources().getString(R.string.no_car_found));
                        List_cars.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    e.printStackTrace();
                    if(c_position == 0)
                        Loader.dismiss();
                }

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                // isLoading=false;


//                new AsyncTaskRunner(isLoading,Loader).execute();

//                List_cars.setExpanded(true);

//                Loader.dismiss();
                /*System.out.println("-------wwwww------>"+arrayList_City.size());
                int ss=arrayList_City.size()-1;
                System.out.println("------kkkkk----->"+ss);
                if(isLoading==true)
                {
                 //   Loader.dismiss();
                    List_cars.setExpanded(true);
                }*/

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(c_position == 0)
                    Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }


    //--------------------calendar---------------------------
    private void Start_Date_Picker(final Bundle savedInstanceState) {
        final Bundle state = savedInstanceState;
        dialogCaldroidFragment_start = new CaldroidFragment();
        dialogCaldroidFragment_start.setCaldroidListener(listener);

        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
        if (state != null) {
            dialogCaldroidFragment_start.restoreDialogStatesFromKey(getActivity().getSupportFragmentManager(), state, "DIALOG_CALDROID_SAVED_STATE", dialogTag);
            Bundle args = dialogCaldroidFragment_start.getArguments();
            if (args == null) {
                args = new Bundle();
                dialogCaldroidFragment_start.setArguments(args);
            }
        } else {
            // Setup arguments
            Bundle bundle = new Bundle();
            // Setup dialogTitle
            dialogCaldroidFragment_start.setArguments(bundle);
        }
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        Date date_current = null, date_to = null;
        String str_current_date;

        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String formattedDate = df.format(c.getTime());
            date_current = df.parse(formattedDate);
            // str_current_date = df.format(date_current);
            // date_to = df.parse("2036-12-27");
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        if (array_avail.size() > 0) {
            dialogCaldroidFragment_start.setDisableDates(array_avail);
        }

        dialogCaldroidFragment_start.setMinDate(date_current);
        dialogCaldroidFragment_start.show(getActivity().getSupportFragmentManager(), dialogTag);

    }


    final CaldroidListener listener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            date_start1 = date;
            if (dialogCaldroidFragment_start.isVisible()) {
                p_select_start = formatter.format(date);

                SimpleDateFormat form_checkin = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date_checkin = null;
                try {

                    date_checkin = form_checkin.parse(formatter.format(date));
                    // SimpleDateFormat postFormater = new SimpleDateFormat("MMM dd yy");
                    SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    // String newDateStr = postFormater.format(date_checkin).toUpperCase();
                    String newDateStr = postFormater.format(date_checkin).toString();
                    System.out.println("Date  : " + newDateStr);
                    Txt_check_In.setText("" + newDateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialogCaldroidFragment_start.dismiss();
        }

        @Override
        public void onChangeMonth(int month, int year) {
            // String text = "month: " + month + " year: " + year; Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            // Toast.makeText(getApplicationContext(),"Long click "// +formatter.format(date),Toast.LENGTH_SHORT).show();
        }
    };


    private void End_Date_Picker(final Bundle savedInstanceState) {
        if (!p_select_start.equals("")) {
            final Bundle state = savedInstanceState;
            dialogCaldroidFragment_end = new CaldroidFragment();
            dialogCaldroidFragment_end.setCaldroidListener(listener_end);
            // If activity is recovered from rotation
            final String dialogTag = "CALDROID_DIALOG_FRAGMENT_END";
            if (state != null) {
                dialogCaldroidFragment_end.restoreDialogStatesFromKey(getActivity().getSupportFragmentManager(), state, "DIALOG_CALDROID_SAVED_STATE", dialogTag);
                Bundle args = dialogCaldroidFragment_end.getArguments();
                if (args == null) {
                    args = new Bundle();
                    dialogCaldroidFragment_end.setArguments(args);
                }
            } else {
                // Setup arguments
                Bundle bundle = new Bundle();
                // Setup dialogTitle
                dialogCaldroidFragment_end.setArguments(bundle);
            }

            try {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf_start = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date_start = sdf_start.parse(p_select_start);
                c.setTime(date_start);
                c.add(Calendar.DATE, 0);
                Date newDate = c.getTime();
                dialogCaldroidFragment_end.setMinDate(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (array_avail.size() > 0) {
                dialogCaldroidFragment_end.setDisableDates(array_avail);
            }
            dialogCaldroidFragment_end.show(getActivity().getSupportFragmentManager(), dialogTag);
            dialogCaldroidFragment_end.refreshView();

        }

    }


    final CaldroidListener listener_end = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            if (dialogCaldroidFragment_end.isVisible()) {
                p_select_end = formatter.format(date);
//				Tv_date_depart_listv.setText(formatter.format(date));
                SimpleDateFormat form_checkin = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date date_checkin = null;
                try {
                    date_checkin = form_checkin.parse(formatter.format(date));
                    SimpleDateFormat postFormater = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    String newDateStr = postFormater.format(date_checkin).toUpperCase();
                    System.out.println("Date  : " + newDateStr);
                    Txt_Check_Out.setText("" + newDateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dialogCaldroidFragment_end.dismiss();
        }

        @Override
        public void onChangeMonth(int month, int year) {
            // String text = "month: " + month + " year: " + year;Toast.makeText(getApplicationContext(), text,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLongClickDate(Date date, View view) {
            // Toast.makeText(getApplicationContext(),"Long click "+formatter.format(date),Toast.LENGTH_SHORT).show();
        }
    };


    public void currecnt_location(Double latitude, Double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ctx, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();// Here 1 represent max location result to returned, by documents it recommended 1 to 5

                System.out.println("-------Address------->" + address);
                System.out.println("-------city------->" + city);
                System.out.println("-------state------->" + state);
                System.out.println("-------country------->" + country);
                System.out.println("-------postalCode------->" + postalCode);
                System.out.println("-------knownName------->" + knownName);

                if (!default_location) {
                    str_get_address = city;
                    Txt_Location.setText(address);
                    // str_get_address="Los Angeles, CA, United States";
                    // str_get_address="";
                    str_get_address = "Los Angeles";
                    Txt_Location.setText("Los Angeles, CA, United States");
                    Load_Data(0);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-----------------new Calendar--------------------

    private void setdate_start() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(ctx, datePickerListener_start, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
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


    private DatePickerDialog.OnDateSetListener datePickerListener_start = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            String getStart_Date_Time = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
            Log.d("Date------->", getStart_Date_Time);
            Txt_check_In.setText(getStart_Date_Time);
            Txt_Check_Out.setHint(getResources().getString(R.string.date_change_format));

        }
    };


    private void setdate_end() {

        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(ctx, datePickerListener_end, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        long maxdate = 0;
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(Txt_check_In.getText().toString());
            maxdate = date1.getTime();
            selectionmodeday(maxdate, dialog);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dialog.setTitle("");
        dialog.show();
    }


    private DatePickerDialog.OnDateSetListener datePickerListener_end = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            //String getStart_Date_Time = String.valueOf(selectedDay) + "/" + String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedYear);
            String getStart_Date_Time = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
            Log.d("Date------->", getStart_Date_Time);

            Txt_Check_Out.setText(getStart_Date_Time);
        }
    };


    private void selectionmodeday(long date1, DatePickerDialog dialog) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, 2);
        long end = calendar.getTimeInMillis();
        dialog.getDatePicker().setMinDate(end);

    }


    public AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if ((firstVisibleItem + visibleItemCount) >= totalItemCount) {

                System.out.println("------Current firstVisibleItem---------->" + firstVisibleItem);
                System.out.println("------Current visibleItemCount---------->" + visibleItemCount);
                System.out.println("------Current totalItemCount---------->" + totalItemCount);
                //here start next 10 items request
            }

        }

    };


    /*private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if(lastInScreen == totalItemCount && !isLoading)
            {
                //loadMoreItems(totalItemCount - 1);
                int c_item=totalItemCount - 1;
                System.out.println("---------Current positions---------->"+c_item);
                //Load_Data();
                isLoading = true;
            }
        }
    };*/


    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(ctx);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }


//    private class AsyncTaskRunner extends AsyncTask<Boolean, Boolean, Boolean>
//    {
//
//        private String resp;
//        ProgressDialog progressDialog;
//        ArrayList<Find_Car_Bean> arrayList_;
//        boolean status_=false;
//        Common_Loader loader_;
//
//        public  AsyncTaskRunner(boolean status,Common_Loader loader)
//        {
//           // this.arrayList_=arrayList_City;
//            this.status_=status;
//            this.loader_=loader;
//
//        }
//
//
//        @Override
//        protected void onPreExecute()
//        {
//           // progressDialog = ProgressDialog.show(ctx, "ProgressDialog","Wait for "+ " seconds");
//            loader_.show();
//            List_cars.setExpanded(true);
//        }
//
//        @Override
//        protected Boolean doInBackground(Boolean... params)
//        {
//            return status_;
//        }
//
//
//        @Override
//        protected void onPostExecute(Boolean result)
//        {
//            // execution of result of Long time consuming operation
//          //  progressDialog.dismiss();
//            System.out.println("------Return status------------>"+result);
//            if(result==true)
//            {
//                loader_.dismiss();
//            }
//            else
//            {
//                loader_.dismiss();
//            }
//
//        }

//    }
    int isPaused = -1;

@Override
public void onPause() {
    super.onPause();
    isPaused = currentPosition;

}
    @Override
    public void onResume() {
        super.onResume();
        if(isPaused>=0) {
            Load_Data(currentPosition);

        }
    }
}
