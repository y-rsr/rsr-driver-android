package com.ridesharerental.app;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
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
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiClientPlaceSearch;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
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
public class DriverClaimentActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText Ed_other_driver_name, Ed_other_email_address, Ed_other_driver_phone, Ed_other_driver_cell, Ed_owner_of_the_car,
            Ed_insurance_company, Ed_anyone_injured_in_the_car, Ed_car_seat, Ed_yr_mk_mode, Ed_license_dl, Ed_license_Plate, Ed_vin, Ed_time_of_incident,
            Ed_state, Ed_city;

    private RelativeLayout Rl_main_layout, Rl_dob, Rl_car_drivable_yes, Rl_car_drivable_no, Rl_air_bag_deployed_no, Rl_air_bag_deployed_yes, Rl_incident_date, Rl_submit, Rl_back;

    private ImageView Iv_car_drivable_yes, Iv_car_drivable_no, Iv_air_bag_deployed_no, Iv_air_bag_deployed_yes, Iv_agree_check_off, Iv_agree_check_on;

    private TextView Tv_dob, Tv_incident_date;

    private LinearLayout Ll_agree_check;

    private AutoCompleteTextView Ed_address;

    ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    SessionManager sessionManager;
    String user_id = "";
    Common_Loader loader;
    boolean agreestaus = false;

    String current_dob = "", drivable = "Yes", air_bag_deployed = "No";
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
        setContentView(R.layout.driver_claimant);

        init();

        Ed_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Ed_address.dismissDropDown();
                sSelected_location = itemList_location.get(position);
                cd = new ConnectionDetector(DriverClaimentActivity.this);
                isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {

                    LatLongRequest(itemList_placeId.get(position));

                } else {
                    Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                }


            }
        });


        Ed_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                cd = new ConnectionDetector(DriverClaimentActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                if(Ed_address.getText().length() > 0){

                    if (callplacesearch != null) {

                        callplacesearch.cancel();

                    }


                    String sTypedPlace = "";

                    try {
                        sTypedPlace = URLEncoder.encode(Ed_address.getText().toString().toLowerCase(), "utf-8");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }


                    CitySearchRequest(sTypedPlace);


                }


            }
        });


    }

    private void init() {

        cd = new ConnectionDetector(DriverClaimentActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        loader = new Common_Loader(DriverClaimentActivity.this);
        sessionManager = new SessionManager(DriverClaimentActivity.this);

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);


        Ed_other_driver_name = (EditText) findViewById(R.id.Ed_other_driver_name);
        Ed_other_email_address = (EditText) findViewById(R.id.Ed_other_email_address);
        Ed_other_driver_phone = (EditText) findViewById(R.id.Ed_other_driver_phone);
        Ed_other_driver_cell = (EditText) findViewById(R.id.Ed_other_driver_cell);
        Ed_owner_of_the_car = (EditText) findViewById(R.id.Ed_owner_of_the_car);
        Ed_insurance_company = (EditText) findViewById(R.id.Ed_insurance_company);
        Ed_anyone_injured_in_the_car = (EditText) findViewById(R.id.Ed_anyone_injured_in_the_car);
        Ed_car_seat = (EditText) findViewById(R.id.Ed_car_seat);
        Ed_yr_mk_mode = (EditText) findViewById(R.id.Ed_yr_mk_mode);
        Ed_license_dl = (EditText) findViewById(R.id.Ed_license_dl);
        Ed_license_Plate = (EditText) findViewById(R.id.Ed_license_Plate);
        Ed_vin = (EditText) findViewById(R.id.Ed_vin);
        Ed_time_of_incident = (EditText) findViewById(R.id.Ed_time_of_incident);
        Ed_state = (EditText) findViewById(R.id.Ed_state);
        Ed_city = (EditText) findViewById(R.id.Ed_city);


        Rl_main_layout = (RelativeLayout) findViewById(R.id.Rl_main_layout);
        Rl_dob = (RelativeLayout) findViewById(R.id.Rl_dob);
        Rl_car_drivable_yes = (RelativeLayout) findViewById(R.id.Rl_car_drivable_yes);
        Rl_car_drivable_no = (RelativeLayout) findViewById(R.id.Rl_car_drivable_no);
        Rl_air_bag_deployed_no = (RelativeLayout) findViewById(R.id.Rl_air_bag_deployed_no);
        Rl_air_bag_deployed_yes = (RelativeLayout) findViewById(R.id.Rl_air_bag_deployed_yes);
        Rl_incident_date = (RelativeLayout) findViewById(R.id.Rl_incident_date);
        Rl_submit = (RelativeLayout) findViewById(R.id.Rl_submit);
        Rl_back = (RelativeLayout) findViewById(R.id.Rl_back);


        Iv_car_drivable_yes = (ImageView) findViewById(R.id.Iv_car_drivable_yes);
        Iv_car_drivable_no = (ImageView) findViewById(R.id.Iv_car_drivable_no);
        Iv_air_bag_deployed_no = (ImageView) findViewById(R.id.Iv_air_bag_deployed_no);
        Iv_air_bag_deployed_yes = (ImageView) findViewById(R.id.Iv_air_bag_deployed_yes);
        Iv_agree_check_off = (ImageView) findViewById(R.id.Iv_agree_check_off);
        Iv_agree_check_on = (ImageView) findViewById(R.id.Iv_agree_check_on);


        Tv_dob = (TextView) findViewById(R.id.Tv_dob);
        Tv_incident_date = (TextView) findViewById(R.id.Tv_incident_date);

        Ed_address = (AutoCompleteTextView) findViewById(R.id.Ed_address);

        Ll_agree_check = (LinearLayout) findViewById(R.id.Ll_agree_check);


        Ed_address.setThreshold(1);

        Rl_back.setOnClickListener(this);
        Rl_dob.setOnClickListener(this);
        Rl_car_drivable_yes.setOnClickListener(this);
        Rl_car_drivable_no.setOnClickListener(this);
        Rl_air_bag_deployed_no.setOnClickListener(this);
        Rl_air_bag_deployed_yes.setOnClickListener(this);
        Rl_incident_date.setOnClickListener(this);
        Ll_agree_check.setOnClickListener(this);
        Rl_submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {


            case  R.id.Rl_back:

                finish();

                break;

            case R.id.Rl_dob:

                current_dob = "dob";
                AccidentDatePicker();

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

            case R.id.Rl_air_bag_deployed_no:

                air_bag_deployed = "No";

                Iv_air_bag_deployed_no.setImageResource(R.drawable.radio_on);
                Iv_air_bag_deployed_yes.setImageResource(R.drawable.radio_off);


                break;

            case R.id.Rl_air_bag_deployed_yes:


                air_bag_deployed = "Yes";

                Iv_air_bag_deployed_no.setImageResource(R.drawable.radio_off);
                Iv_air_bag_deployed_yes.setImageResource(R.drawable.radio_on);

                break;

            case R.id.Rl_incident_date:

                current_dob = "incident_date";
                AccidentDatePicker();

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

                if (Ed_other_driver_name.getText().toString().trim().length() == 0) {

                    erroredit(Ed_other_driver_name, "Name should not empty");

                } else if (!agreestaus) {

                    Toast.makeText(DriverClaimentActivity.this, "Please Acknowledged", Toast.LENGTH_SHORT).show();

                } else {
                    cd = new ConnectionDetector(DriverClaimentActivity.this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (isInternetPresent) {

                        ApiServiceForSaveClaimant();

                    } else {

                        Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                    }
                }


                break;


        }

    }


    private void ApiServiceForSaveClaimant() {


        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        HashMap<String, String> post = new HashMap<>();
        post.put("driver_name", Ed_other_driver_name.getText().toString());
        post.put("address", Ed_address.getText().toString());
        post.put("email", Ed_other_email_address.getText().toString());
        post.put("phone", Ed_other_driver_phone.getText().toString());
        post.put("cell", Ed_other_driver_cell.getText().toString());
        post.put("date_of_birth", Tv_dob.getText().toString().contains("mm/dd/yyyy") ? "" : Tv_dob.getText().toString());
        post.put("owner_of_the_car", Ed_owner_of_the_car.getText().toString());
        post.put("insurance_company", Ed_insurance_company.getText().toString());
        post.put("passengers", Ed_anyone_injured_in_the_car.getText().toString());
        post.put("drivable", drivable);
        post.put("air_bag_deployed", air_bag_deployed);
        post.put("car_seats", Ed_car_seat.getText().toString());
        post.put("make_model_year", Ed_yr_mk_mode.getText().toString());
        post.put("licence_dl", Ed_license_dl.getText().toString());
        post.put("licence_plate", Ed_license_Plate.getText().toString());
        post.put("vin", Ed_vin.getText().toString());
        post.put("date_of_incident", Tv_incident_date.getText().toString().contains("mm/dd/yyyy") ? "" : Tv_incident_date.getText().toString());
        post.put("time_of_incident", Ed_time_of_incident.getText().toString());
        post.put("state", Ed_state.getText().toString());
        post.put("city", Ed_city.getText().toString());


        System.out.println("--------------Claimant claim post params--------------" + post.toString());


        Call<ResponseBody> call = apiService.save_claimant_clam(/*header,*/post);


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

                            AlertSnakbar("Claimant Claim", "Updated Successfully");


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


            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader.dismiss();
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

            } else if (current_dob.equalsIgnoreCase("incident_date")) {

                Tv_incident_date.setText(String.format("%02d", monthOfYear + 1) + "/" + String.format("%02d", dayOfMonth) + "/" + String.valueOf(year));
//                Tv_witness_dob.setTextColor(getResources().getColor(R.color.text_gray));

            }


        }
    };


    //-------------------Search Place Request----------------
    private void CitySearchRequest(String data) {


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


                    adapter = new ArrayAdapter<String>
                            (DriverClaimentActivity.this, R.layout.auto_complete_list_item, R.id.autoComplete_textView, itemList_location);
                    Ed_address.setAdapter(adapter);

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    //-------------------Get Latitude and Longitude from Address(Place ID) Request----------------
    private void LatLongRequest(String placeId) {


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

                        Ed_address.setText(sSelected_location);


                    } else {


                        Ed_address.setText(sSelected_location);
                        Ed_address.dismissDropDown();


                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }


    //--------------------Code to set error for EditText-----------------------
    private void erroredit(EditText editname, String msg) {
        Animation shake = AnimationUtils.loadAnimation(DriverClaimentActivity.this, R.anim.shake);
        editname.startAnimation(shake);

        ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.parseColor("#CC0000"));
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(msg);
        ssbuilder.setSpan(fgcspan, 0, msg.length(), 0);
        editname.setError(ssbuilder);
    }

    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(DriverClaimentActivity.this);
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


    private void AlertSnakbar(String title, String message) {

        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.snack_bar_new, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        // TextView Tv_message = (TextView) view.findViewById(R.id.txt_message);

        Tv_title.setText(title);
        // Tv_message.setText(message);

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.ligth_black);
        AppMsg snack = AppMsg.makeText(DriverClaimentActivity.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(500);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();


    }


}
