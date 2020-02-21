package com.ridesharerental.app;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiClientPlaceSearch;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.TimePickerFragment;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user129 on 8/9/2018.
 */
public class DriverClaimActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText Ed_your_name, Ed_email, Ed_Phone, Ed_Cell, Ed_driver_license, Ed_passengers_details, Ed_anyone_injured, Ed_police_report,
            Ed_police_dept, Ed_car_vin_number, Ed_tow, Ed_location_incident, Ed_description_of_incident, Ed_claimant_name,
            Ed_claimant_Phone;

    private RelativeLayout Rl_back,Rl_dateAndTimeAccident,Rl_dob,Rl_TimeAccident, Rl_uber_mode_yes, Rl_uber_mode_no, Rl_Lyft_mode_yes, Rl_Lyft_mode_no, Rl_car_drivable_yes, Rl_car_drivable_no, Rl_air_bag_deployed_yes,
            Rl_air_bag_deployed_no, Drv_condition_raining, Drv_condition_fog, Drv_condition_snow, Drv_condition_sunny, traffic_condition_heavy, traffic_condition_moderate, traffic_condition_no_traffic,
            Rl_claimant_dob, Rl_submit;

    private ImageView Iv_uber_mode_yes, Iv_uber_mode_no, Iv_Lyft_mode_yes, Iv_Lyft_mode_no, Iv_car_drivable_yes, Iv_car_drivable_no,
            Iv_air_bag_deployed_no, Iv_air_bag_deployed_yes, Iv_drv_condi_raining, Iv_drv_condi_fog, Iv_drv_condi_snow, Iv_drv_condi_sunny,
            Iv_traffic_condi_heavy, Iv_traffic_condi_moderate, Iv_traffic_condi_no_traffic, Iv_agree_check_off, Iv_agree_check_on, Iv_claimant_agree_check_off, Iv_claimant_agree_check_on;

    private TextView Tv_dob, Tv_claimant_dob,Tv_TimeAccident,Tv_dateAndTimeAccident;

    private LinearLayout Ll_claimant_agree_check, Ll_agree_check;


    private AutoCompleteTextView Ed_Address, Ed_claimant_address,Ed_placeOfAccident;

    ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    SessionManager sessionManager;
    String user_id = "";
    Common_Loader loader;
    String current_dob = "", uber_mode = "Yes", lyft_mode = "No", drivable = "Yes", air_bag_deployed = "No", drive_condition = "", traffic_condition = "", claimant_agree_staus = "No";
    boolean agreestaus = false;
    boolean claimant_agree = false;
    ArrayList<String> itemList_location = new ArrayList<String>();
    ArrayList<String> itemList_placeId = new ArrayList<String>();
    private boolean isDataAvailable = false;
    private boolean isAddressAvailable = false;
    Call<ResponseBody> callplacesearch;
    ArrayAdapter<String> adapter;
    private String sLatitude = "", sLongitude = "", sSelected_location = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_claims);

        init();


        Ed_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ed_Address.dismissDropDown();
                sSelected_location = itemList_location.get(position);
                cd = new ConnectionDetector(DriverClaimActivity.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    LatLongRequest(itemList_placeId.get(position), "Claim Address");

                } else {
                    Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                }

            }
        });

        Ed_placeOfAccident.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ed_placeOfAccident.dismissDropDown();
                sSelected_location = itemList_location.get(position);
                cd = new ConnectionDetector(DriverClaimActivity.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    LatLongRequest(itemList_placeId.get(position), "AccidentPlace");

                } else {
                    Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                }

            }
        });


        Ed_Address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                cd = new ConnectionDetector(DriverClaimActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (Ed_Address.getText().length() > 0) {

                    if (callplacesearch != null) {

                        callplacesearch.cancel();

                    }


                    String sTypedPlace = "";

                    try {
                        sTypedPlace = URLEncoder.encode(Ed_Address.getText().toString().toLowerCase(), "utf-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }


                    CitySearchRequest(sTypedPlace, "Claim Address");


                }


            }
        });
        Ed_placeOfAccident.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                cd = new ConnectionDetector(DriverClaimActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (Ed_placeOfAccident.getText().length() > 0) {

                    if (callplacesearch != null) {

                        callplacesearch.cancel();

                    }


                    String sTypedPlace = "";

                    try {
                        sTypedPlace = URLEncoder.encode(Ed_placeOfAccident.getText().toString().toLowerCase(), "utf-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }


                    CitySearchRequest(sTypedPlace, "AccidentPlace");


                }


            }
        });


        Ed_claimant_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ed_claimant_address.dismissDropDown();
                sSelected_location = itemList_location.get(position);
                cd = new ConnectionDetector(DriverClaimActivity.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    LatLongRequest(itemList_placeId.get(position), "Claimant Address");

                } else {
                    Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                }


            }
        });


        Ed_claimant_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                cd = new ConnectionDetector(DriverClaimActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (Ed_claimant_address.getText().length() > 0) {

                    if (callplacesearch != null) {

                        callplacesearch.cancel();

                    }


                    String sTypedPlace = "";

                    try {
                        sTypedPlace = URLEncoder.encode(Ed_claimant_address.getText().toString().toLowerCase(), "utf-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }


                    CitySearchRequest(sTypedPlace, "Claimant Address");


                }


            }
        });


    }

    private void init() {
        cd = new ConnectionDetector(DriverClaimActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        loader = new Common_Loader(DriverClaimActivity.this);
        sessionManager = new SessionManager(DriverClaimActivity.this);

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);

        Ed_your_name = (EditText) findViewById(R.id.Ed_your_name);

        Ed_email = (EditText) findViewById(R.id.Ed_email);
        Ed_Phone = (EditText) findViewById(R.id.Ed_Phone);
        Ed_Cell = (EditText) findViewById(R.id.Ed_Cell);
        Ed_driver_license = (EditText) findViewById(R.id.Ed_driver_license);
        Ed_passengers_details = (EditText) findViewById(R.id.Ed_passengers_details);
        Ed_anyone_injured = (EditText) findViewById(R.id.Ed_anyone_injured);
        Ed_police_report = (EditText) findViewById(R.id.Ed_police_report);
        Ed_police_dept = (EditText) findViewById(R.id.Ed_police_dept);
        Ed_car_vin_number = (EditText) findViewById(R.id.Ed_car_vin_number);
        Ed_tow = (EditText) findViewById(R.id.Ed_tow);
        Ed_location_incident = (EditText) findViewById(R.id.Ed_location_incident);
        Ed_description_of_incident = (EditText) findViewById(R.id.Ed_description_of_incident);
        Ed_claimant_name = (EditText) findViewById(R.id.Ed_claimant_name);
        Ed_claimant_Phone = (EditText) findViewById(R.id.Ed_claimant_Phone);

        Rl_back = (RelativeLayout) findViewById(R.id.Rl_back);
        Rl_dob = (RelativeLayout) findViewById(R.id.Rl_dob);
        Rl_TimeAccident = (RelativeLayout) findViewById(R.id.Rl_TimeAccident);
        Rl_dateAndTimeAccident = (RelativeLayout) findViewById(R.id.Rl_dateAndTimeAccident);
        Rl_uber_mode_yes = (RelativeLayout) findViewById(R.id.Rl_uber_mode_yes);
        Rl_uber_mode_no = (RelativeLayout) findViewById(R.id.Rl_uber_mode_no);
        Rl_Lyft_mode_yes = (RelativeLayout) findViewById(R.id.Rl_Lyft_mode_yes);
        Rl_Lyft_mode_no = (RelativeLayout) findViewById(R.id.Rl_Lyft_mode_no);
        Rl_car_drivable_yes = (RelativeLayout) findViewById(R.id.Rl_car_drivable_yes);
        Rl_car_drivable_no = (RelativeLayout) findViewById(R.id.Rl_car_drivable_no);
        Rl_air_bag_deployed_yes = (RelativeLayout) findViewById(R.id.Rl_air_bag_deployed_yes);
        Rl_air_bag_deployed_no = (RelativeLayout) findViewById(R.id.Rl_air_bag_deployed_no);
        Drv_condition_raining = (RelativeLayout) findViewById(R.id.Drv_condition_raining);
        Drv_condition_fog = (RelativeLayout) findViewById(R.id.Drv_condition_fog);
        Drv_condition_snow = (RelativeLayout) findViewById(R.id.Drv_condition_snow);
        Drv_condition_sunny = (RelativeLayout) findViewById(R.id.Drv_condition_sunny);
        traffic_condition_heavy = (RelativeLayout) findViewById(R.id.traffic_condition_heavy);
        traffic_condition_moderate = (RelativeLayout) findViewById(R.id.traffic_condition_moderate);
        traffic_condition_no_traffic = (RelativeLayout) findViewById(R.id.traffic_condition_no_traffic);
        Rl_claimant_dob = (RelativeLayout) findViewById(R.id.Rl_claimant_dob);
        Rl_submit = (RelativeLayout) findViewById(R.id.Rl_submit);


        Iv_uber_mode_yes = (ImageView) findViewById(R.id.Iv_uber_mode_yes);
        Iv_uber_mode_no = (ImageView) findViewById(R.id.Iv_uber_mode_no);
        Iv_Lyft_mode_yes = (ImageView) findViewById(R.id.Iv_Lyft_mode_yes);
        Iv_Lyft_mode_no = (ImageView) findViewById(R.id.Iv_Lyft_mode_no);
        Iv_car_drivable_yes = (ImageView) findViewById(R.id.Iv_car_drivable_yes);
        Iv_car_drivable_no = (ImageView) findViewById(R.id.Iv_car_drivable_no);
        Iv_air_bag_deployed_no = (ImageView) findViewById(R.id.Iv_air_bag_deployed_no);
        Iv_air_bag_deployed_yes = (ImageView) findViewById(R.id.Iv_air_bag_deployed_yes);
        Iv_drv_condi_raining = (ImageView) findViewById(R.id.Iv_drv_condi_raining);
        Iv_drv_condi_fog = (ImageView) findViewById(R.id.Iv_drv_condi_fog);
        Iv_drv_condi_snow = (ImageView) findViewById(R.id.Iv_drv_condi_snow);
        Iv_drv_condi_sunny = (ImageView) findViewById(R.id.Iv_drv_condi_sunny);
        Iv_traffic_condi_heavy = (ImageView) findViewById(R.id.Iv_traffic_condi_heavy);
        Iv_traffic_condi_moderate = (ImageView) findViewById(R.id.Iv_traffic_condi_moderate);
        Iv_traffic_condi_no_traffic = (ImageView) findViewById(R.id.Iv_traffic_condi_no_traffic);
        Iv_claimant_agree_check_off = (ImageView) findViewById(R.id.Iv_claimant_agree_check_off);
        Iv_claimant_agree_check_on = (ImageView) findViewById(R.id.Iv_claimant_agree_check_on);
        Iv_agree_check_off = (ImageView) findViewById(R.id.Iv_agree_check_off);
        Iv_agree_check_on = (ImageView) findViewById(R.id.Iv_agree_check_on);


        Tv_dob = (TextView) findViewById(R.id.Tv_dob);
        Tv_dateAndTimeAccident = (TextView) findViewById(R.id.Tv_dateAndTimeAccident);
        Tv_TimeAccident = (TextView) findViewById(R.id.Tv_TimeAccident);
        Tv_claimant_dob = (TextView) findViewById(R.id.Tv_claimant_dob);

        Ll_agree_check = (LinearLayout) findViewById(R.id.Ll_agree_check);
        Ll_claimant_agree_check = (LinearLayout) findViewById(R.id.Ll_claimant_agree_check);


        Ed_Address = (AutoCompleteTextView) findViewById(R.id.Ed_Address);
        Ed_placeOfAccident = (AutoCompleteTextView) findViewById(R.id.Ed_placeOfAccident);
        Ed_claimant_address = (AutoCompleteTextView) findViewById(R.id.Ed_claimant_address);

        Ed_Address.setThreshold(1);
        Ed_placeOfAccident.setThreshold(1);
        Ed_claimant_address.setThreshold(1);
        Rl_back.setOnClickListener(this);
        Rl_dob.setOnClickListener(this);
        Rl_dateAndTimeAccident.setOnClickListener(this);
        Rl_TimeAccident.setOnClickListener(this);
        Rl_uber_mode_yes.setOnClickListener(this);
        Rl_uber_mode_no.setOnClickListener(this);
        Rl_Lyft_mode_yes.setOnClickListener(this);
        Rl_Lyft_mode_no.setOnClickListener(this);
        Rl_car_drivable_yes.setOnClickListener(this);
        Rl_car_drivable_no.setOnClickListener(this);
        Rl_air_bag_deployed_yes.setOnClickListener(this);
        Rl_air_bag_deployed_no.setOnClickListener(this);
        Drv_condition_raining.setOnClickListener(this);
        Drv_condition_fog.setOnClickListener(this);
        Drv_condition_snow.setOnClickListener(this);
        Drv_condition_sunny.setOnClickListener(this);
        traffic_condition_heavy.setOnClickListener(this);
        traffic_condition_moderate.setOnClickListener(this);
        traffic_condition_no_traffic.setOnClickListener(this);
        Rl_claimant_dob.setOnClickListener(this);
        Ll_claimant_agree_check.setOnClickListener(this);
        Ll_agree_check.setOnClickListener(this);
        Rl_submit.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.Rl_back:

                finish();

                break;

            case R.id.Rl_dob:

                current_dob = "dob";
                AccidentDatePicker();

                break;
            case R.id.Rl_dateAndTimeAccident:

                current_dob = "dateAndTime";
                AccidentDatePicker();

                break;
            case R.id.Rl_TimeAccident:



                break;

            case R.id.Rl_uber_mode_yes:


                uber_mode = "Yes";
                Iv_uber_mode_yes.setImageResource(R.drawable.radio_on);
                Iv_uber_mode_no.setImageResource(R.drawable.radio_off);


                break;
            case R.id.Rl_uber_mode_no:

                uber_mode = "No";
                Iv_uber_mode_yes.setImageResource(R.drawable.radio_off);
                Iv_uber_mode_no.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_Lyft_mode_yes:

                lyft_mode = "Yes";

                Iv_Lyft_mode_yes.setImageResource(R.drawable.radio_on);
                Iv_Lyft_mode_no.setImageResource(R.drawable.radio_off);


                break;
            case R.id.Rl_Lyft_mode_no:

                lyft_mode = "No";
                Iv_Lyft_mode_yes.setImageResource(R.drawable.radio_off);
                Iv_Lyft_mode_no.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_car_drivable_yes:
                drivable = "Yes";

                Iv_car_drivable_yes.setImageResource(R.drawable.radio_on);
                Iv_car_drivable_no.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Rl_car_drivable_no:

                drivable = "No";
                Iv_car_drivable_yes.setImageResource(R.drawable.radio_off);
                Iv_car_drivable_no.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_air_bag_deployed_yes:

                air_bag_deployed = "Yes";

                Iv_air_bag_deployed_no.setImageResource(R.drawable.radio_off);
                Iv_air_bag_deployed_yes.setImageResource(R.drawable.radio_on);


                break;
            case R.id.Rl_air_bag_deployed_no:

                air_bag_deployed = "No";

                Iv_air_bag_deployed_no.setImageResource(R.drawable.radio_on);
                Iv_air_bag_deployed_yes.setImageResource(R.drawable.radio_off);


                break;
            case R.id.Drv_condition_raining:

                drive_condition = "Raining";

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_on);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Drv_condition_fog:

                drive_condition = "Fog";

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_on);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Drv_condition_snow:

                drive_condition = "Snow";

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_on);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Drv_condition_sunny:

                drive_condition = "Sunny";

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_on);

                break;
            case R.id.traffic_condition_heavy:

                traffic_condition = "Heavy";

                Iv_traffic_condi_heavy.setImageResource(R.drawable.radio_on);
                Iv_traffic_condi_moderate.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_no_traffic.setImageResource(R.drawable.radio_off);


                break;
            case R.id.traffic_condition_moderate:

                traffic_condition = "Moderate";

                Iv_traffic_condi_heavy.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_moderate.setImageResource(R.drawable.radio_on);
                Iv_traffic_condi_no_traffic.setImageResource(R.drawable.radio_off);

                break;
            case R.id.traffic_condition_no_traffic:

                traffic_condition = "No Traffic";

                Iv_traffic_condi_heavy.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_moderate.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_no_traffic.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_claimant_dob:

                current_dob = "claimant_dob";
                AccidentDatePicker();

                break;

            case R.id.Ll_claimant_agree_check:


                if (Iv_claimant_agree_check_off.getVisibility() == View.VISIBLE) {

                    claimant_agree_staus = "Yes";
                    Iv_claimant_agree_check_off.setVisibility(View.GONE);
                    Iv_claimant_agree_check_on.setVisibility(View.VISIBLE);

                } else {

                    claimant_agree_staus = "No";
                    Iv_claimant_agree_check_off.setVisibility(View.VISIBLE);
                    Iv_claimant_agree_check_on.setVisibility(View.GONE);


                }


                break;


            case R.id.Ll_agree_check:

                if (Iv_agree_check_off.getVisibility() == View.VISIBLE) {

                    agreestaus = true;
                    Iv_agree_check_on.setVisibility(View.VISIBLE);
                    Iv_agree_check_off.setVisibility(View.GONE);

                } else {

                    agreestaus = false;
                    Iv_agree_check_on.setVisibility(View.GONE);
                    Iv_agree_check_off.setVisibility(View.VISIBLE);
                }


                break;

            case R.id.Rl_submit:

                if (Ed_your_name.getText().toString().trim().length() == 0) {

                    erroredit(Ed_your_name, "Name should not empty");

                } else if (!isValidEmail(Ed_email.getText().toString().replace(" ", ""))) {

                    Ed_email.setFocusable(true);
                    erroredit(Ed_email, "Email address is invalid");

                } else if (Ed_car_vin_number.getText().toString().trim().length() == 0) {

                    Ed_car_vin_number.setFocusable(true);
                    erroredit(Ed_car_vin_number, "Car VIN Number should not empty");

                } else if (!agreestaus) {

                    Toast.makeText(DriverClaimActivity.this, "Please Acknowledged", Toast.LENGTH_SHORT).show();

                } else {

                    cd = new ConnectionDetector(DriverClaimActivity.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (isInternetPresent) {

                        ApiServiceForSaveClaim();

                    } else {

                        Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                    }

                }


                break;

        }

    }


    private void ApiServiceForSaveClaim() {


        String time = Tv_TimeAccident.getText().toString().contains("hh:mm")?"":Tv_TimeAccident.getText().toString();
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        HashMap<String, String> post = new HashMap<>();
        post.put("driver_name", Ed_your_name.getText().toString());
        post.put("address", Ed_Address.getText().toString());
        post.put("place_of_accident", Ed_placeOfAccident.getText().toString());
        post.put("email", Ed_email.getText().toString());
        post.put("phone", Ed_Phone.getText().toString());
        post.put("cell", Ed_Cell.getText().toString());
        post.put("date_of_accident", Tv_dateAndTimeAccident.getText().toString().contains("mm/dd/yyyy") ? "" : Tv_dateAndTimeAccident.getText().toString()+" "+time);
        post.put("date_of_birth", Tv_dob.getText().toString().contains("mm/dd/yyyy") ? "" : Tv_dob.getText().toString());
        post.put("driver_licence", Ed_driver_license.getText().toString());
        post.put("uber_mode", uber_mode);
        post.put("lyft_mode", lyft_mode);
        post.put("passengers", Ed_passengers_details.getText().toString());
        post.put("any_injuries", Ed_anyone_injured.getText().toString());
        post.put("police_report", Ed_police_report.getText().toString());
        post.put("police_dept", Ed_police_dept.getText().toString());
        post.put("vin_no", Ed_car_vin_number.getText().toString());
        post.put("drivable", drivable);
        post.put("air_bag_deployed", air_bag_deployed);
        post.put("drive_condition", drive_condition);
        post.put("traffic_condition", traffic_condition);
        post.put("where_the_car_tow", Ed_tow.getText().toString());
        post.put("intersection_location", Ed_location_incident.getText().toString());
        post.put("description_of_incident", Ed_description_of_incident.getText().toString());
        post.put("pedestrian", claimant_agree_staus);
        post.put("pedestrian_name", Ed_claimant_name.getText().toString());
        post.put("pedestrian_address", Ed_claimant_address.getText().toString());
        post.put("pedestrian_dob", Tv_claimant_dob.getText().toString().contains("mm/dd/yyyy") ? "" : Tv_claimant_dob.getText().toString());
        post.put("pedestrian_phone", Ed_claimant_Phone.getText().toString());


        System.out.println("--------------kannan--------------" + post.toString());


        Call<ResponseBody> call = apiService.save_driver_clam(/*header,*/post);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String staus = "";

                try {

                    if (response.isSuccessful() && response.code() == 200) {


                        String res_objcet = response.body().string();


                        System.out.println("-------------save driver claim response-------------------------" + res_objcet);

                        JSONObject object = new JSONObject(res_objcet);

                        JSONObject responseArr = object.getJSONObject("responseArr");


                        staus = responseArr.getString("status");

                        if (staus.equalsIgnoreCase("1")) {


                            loader.dismiss();

                            AlertSnakbar("Driver Claim", "Updated Successfully");


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Write your code here
                                    finish();

                                }
                            }, 800);

                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


//                loader.dismiss();


            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader.dismiss();
            }
        });


    }


    //-------------------Search Place Request----------------
    private void CitySearchRequest(String data, final String addressforwho) {


        ApiInterface apiService = ApiClientPlaceSearch.getClient().create(ApiInterface.class);

        callplacesearch = apiService.google_place_serach("geocode||establishment", IConstant_WebService.Google_Map_Place_Search_Api, data);

        System.out.println("--------------Search city url-------------------" + callplacesearch.request().url().toString());


        callplacesearch.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String status = "";

                try {

                    if (response.isSuccessful()) {

                        String Str_response = response.body().string();

                        System.out.println("--------------Search city  reponse-------------------" + Str_response);

                        JSONObject object = new JSONObject(Str_response);
                        if (object.length() > 0) {

                            status = object.getString("status");
                            JSONArray place_array = object.getJSONArray("predictions");
                            if (status.equalsIgnoreCase("OK")) {
                                if (place_array.length() > 0) {
                                    itemList_location.clear();
                                    itemList_placeId.clear();
                                    for (int i = 0; i < place_array.length(); i++) {
                                        JSONObject place_object = place_array.getJSONObject(i);
                                        itemList_location.add(place_object.getString("description"));
                                        itemList_placeId.add(place_object.getString("place_id"));
                                    }
                                    isDataAvailable = true;
                                } else {
                                    itemList_location.clear();
                                    itemList_placeId.clear();
                                    isDataAvailable = false;
                                }
                            } else {
                                itemList_location.clear();
                                itemList_placeId.clear();
                                isDataAvailable = false;
                            }
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (isDataAvailable) {


                    if (addressforwho.equalsIgnoreCase("Claim Address")) {

                        adapter = new ArrayAdapter<String>
                                (DriverClaimActivity.this, R.layout.auto_complete_list_item, R.id.autoComplete_textView, itemList_location);
                        Ed_Address.setAdapter(adapter);


                    } else if (addressforwho.equalsIgnoreCase("Claimant Address")) {

                        adapter = new ArrayAdapter<String>
                                (DriverClaimActivity.this, R.layout.auto_complete_list_item, R.id.autoComplete_textView, itemList_location);
                        Ed_claimant_address.setAdapter(adapter);

                    }

                    else if (addressforwho.equalsIgnoreCase("AccidentPlace")) {

                        adapter = new ArrayAdapter<String>
                                (DriverClaimActivity.this, R.layout.auto_complete_list_item, R.id.autoComplete_textView, itemList_location);
                        Ed_placeOfAccident.setAdapter(adapter);

                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    //-------------------Get Latitude and Longitude from Address(Place ID) Request----------------
    private void LatLongRequest(String placeId, final String addressforwho) {


        ApiInterface apiService = ApiClientPlaceSearch.getClient().create(ApiInterface.class);


        callplacesearch = apiService.GetAddressFrom_LatLong_url(IConstant_WebService.Google_Map_Place_Search_Api, placeId);

        System.out.println("--------------LatLong url-------------------" + callplacesearch.request().url().toString());

        callplacesearch.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String status = "", sArea = "", sLocality = "", sCity = "", sState = "", sPostalCode = "", sRoute = "", sStreetName = "", sCountry = "";

                try {

                    if (response.isSuccessful()) {

                        String Str_response = response.body().string();

                        System.out.println("--------------LatLong  reponse-------------------" + Str_response);

                        JSONObject object = new JSONObject(Str_response);
                        if (object.length() > 0) {

                            status = object.getString("status");
                            JSONObject place_object = object.getJSONObject("result");
                            if (status.equalsIgnoreCase("OK")) {
                                if (place_object.length() > 0) {

                                    sArea = place_object.getString("vicinity");
                                    JSONArray addressArray = place_object.getJSONArray("address_components");
                                    if (addressArray.length() > 0) {
                                        for (int i = 0; i < addressArray.length(); i++) {
                                            JSONObject address_object = addressArray.getJSONObject(i);

                                            JSONArray typesArray = address_object.getJSONArray("types");
                                            if (typesArray.length() > 0) {
                                                for (int j = 0; j < typesArray.length(); j++) {


                                                    if (typesArray.get(j).toString().equalsIgnoreCase("locality")) {
                                                        sLocality = address_object.getString("long_name");
                                                    } else if (typesArray.get(j).toString().equalsIgnoreCase("administrative_area_level_2")) {
                                                        sCity = address_object.getString("long_name");
                                                    } else if (typesArray.get(j).toString().equalsIgnoreCase("administrative_area_level_1")) {
                                                        sState = address_object.getString("long_name");
                                                    } else if (typesArray.get(j).toString().equalsIgnoreCase("postal_code")) {
                                                        sPostalCode = address_object.getString("long_name");
                                                    } else if (typesArray.get(j).toString().equalsIgnoreCase("route")) {
                                                        sRoute = address_object.getString("long_name");
                                                    } else if (typesArray.get(j).toString().equalsIgnoreCase("sublocality_level_1")) {
                                                        sStreetName = address_object.getString("long_name");
                                                    } else if (typesArray.get(j).toString().equalsIgnoreCase("country")) {
                                                        sCountry = address_object.getString("long_name");
                                                    }


                                                }


                                                isAddressAvailable = true;
                                            } else {
                                                isAddressAvailable = false;
                                            }
                                        }
                                    } else {
                                        isAddressAvailable = false;
                                    }

                                    JSONObject geometry_object = place_object.getJSONObject("geometry");
                                    if (geometry_object.length() > 0) {
                                        JSONObject location_object = geometry_object.getJSONObject("location");
                                        if (location_object.length() > 0) {
                                            sLatitude = location_object.getString("lat");
                                            sLongitude = location_object.getString("lng");
                                            isDataAvailable = true;
                                        } else {
                                            isDataAvailable = false;
                                        }
                                    } else {
                                        isDataAvailable = false;
                                    }
                                } else {
                                    isDataAvailable = false;
                                }
                            } else {
                                isDataAvailable = false;
                            }
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (isDataAvailable) {


                    if (isAddressAvailable) {

                        if (addressforwho.equalsIgnoreCase("Claim Address")) {

                            Ed_Address.setText(sSelected_location);

                        } else if (addressforwho.equalsIgnoreCase("Claimant Address")) {

                            Ed_claimant_address.setText(sSelected_location);

                        } else if (addressforwho.equalsIgnoreCase("AccidentPlace")) {

                            Ed_placeOfAccident.setText(sSelected_location);

                        }


                    } else {

                        if (addressforwho.equalsIgnoreCase("Claim Address")) {

                            Ed_Address.setText(sSelected_location);
                            Ed_Address.dismissDropDown();

                        } else if (addressforwho.equalsIgnoreCase("Claimant Address")) {

                            Ed_claimant_address.setText(sSelected_location);
                            Ed_claimant_address.dismissDropDown();

                        }else if (addressforwho.equalsIgnoreCase("AccidentPlace")) {

                            Ed_placeOfAccident.setText(sSelected_location);
                            Ed_placeOfAccident.dismissDropDown();

                        }


                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    private void AccidentDatePicker() {

        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(this, myDateListener, mYear, mMonth, mDay);

        dpDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        dpDialog.show();

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth) {
            Log.e("onDateSet()", "arg0 = [" + arg0 + "], year = [" + year + "], monthOfYear = [" + monthOfYear + 1 + "], dayOfMonth = [" + dayOfMonth + "]");


            if (current_dob.equalsIgnoreCase("dob")) {

                Tv_dob.setText(String.format("%02d", monthOfYear + 1) + "/" + String.format("%02d", dayOfMonth) + "/" + String.valueOf(year));
//                Tv_accident_date.setTextColor(getResources().getColor(R.color.text_gray));

            } else if (current_dob.equalsIgnoreCase("claimant_dob")) {

                Tv_claimant_dob.setText(String.format("%02d", monthOfYear + 1) + "/" + String.format("%02d", dayOfMonth) + "/" + String.valueOf(year));
//                Tv_witness_dob.setTextColor(getResources().getColor(R.color.text_gray));

            } else if (current_dob.equalsIgnoreCase("dateAndTime")) {

                Tv_dateAndTimeAccident.setText(String.format("%02d", monthOfYear + 1) + "/" + String.format("%02d", dayOfMonth) + "/" + String.valueOf(year));
//                Tv_witness_dob.setTextColor(getResources().getColor(R.color.text_gray));
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }


        }
    };


    //-------------------------code to Check Email Validation-----------------------
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //--------------------Code to set error for EditText-----------------------
    private void erroredit(EditText editname, String msg) {
        Animation shake = AnimationUtils.loadAnimation(DriverClaimActivity.this, R.anim.shake);
        editname.startAnimation(shake);

        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.parseColor("#CC0000"));
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
        ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);
        editname.setError(ssbuilder);
    }


    private void AlertSnakbar(String title, String message) {

        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.snack_bar_new, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        // TextView Tv_message = (TextView) view.findViewById(R.id.txt_message);

        Tv_title.setText(title);
        // Tv_message.setText(message);

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.ligth_black);
        AppMsg snack = AppMsg.makeText(DriverClaimActivity.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(500);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();


    }


    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(DriverClaimActivity.this);
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


}
