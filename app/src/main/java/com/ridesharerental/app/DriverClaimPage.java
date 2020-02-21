package com.ridesharerental.app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by user129 on 8/7/2018.
 */
public class DriverClaimPage extends Fragment implements View.OnClickListener {


    private ActionBar actionBar;

    private EditText Ed_your_name, Ed_Address, Ed_email, Ed_Phone, Ed_Cell, Ed_driver_license, Ed_passengers_details, Ed_anyone_injured, Ed_police_report,
            Ed_police_dept, Ed_car_vin_number, Ed_tow, Ed_location_incident, Ed_description_of_incident, Ed_claimant_name, Ed_claimant_address,
            Ed_claimant_Phone;

    private RelativeLayout Rl_dob, Rl_uber_mode_yes, Rl_uber_mode_no, Rl_Lyft_mode_yes, Rl_Lyft_mode_no, Rl_car_drivable_yes, Rl_car_drivable_no, Rl_air_bag_deployed_yes,
            Rl_air_bag_deployed_no, Drv_condition_raining, Drv_condition_fog, Drv_condition_snow, Drv_condition_sunny, traffic_condition_heavy, traffic_condition_moderate, traffic_condition_no_traffic,
            Rl_claimant_dob, Rl_submit;

    private ImageView Iv_uber_mode_yes, Iv_uber_mode_no, Iv_Lyft_mode_yes, Iv_Lyft_mode_no, Iv_car_drivable_yes, Iv_car_drivable_no,
            Iv_air_bag_deployed_no, Iv_air_bag_deployed_yes, Iv_drv_condi_raining, Iv_drv_condi_fog, Iv_drv_condi_snow, Iv_drv_condi_sunny,
            Iv_traffic_condi_heavy, Iv_traffic_condi_moderate, Iv_traffic_condi_no_traffic;

    private TextView Tv_dob, Tv_claimant_dob;

    private LinearLayout Ll_agree_check;


    SessionManager sessionManager;
    String user_id = "";
    Common_Loader loader;
    String current_dob = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();

        View rootView = inflater.inflate(R.layout.driver_claims, container, false);

        init(rootView);

        return rootView;
    }


    private void init(View rootvView) {

        loader = new Common_Loader(getActivity());
        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);

        Ed_your_name = (EditText) rootvView.findViewById(R.id.Ed_your_name);
        Ed_Address = (EditText) rootvView.findViewById(R.id.Ed_Address);
        Ed_email = (EditText) rootvView.findViewById(R.id.Ed_email);
        Ed_Phone = (EditText) rootvView.findViewById(R.id.Ed_Phone);
        Ed_driver_license = (EditText) rootvView.findViewById(R.id.Ed_driver_license);
        Ed_passengers_details = (EditText) rootvView.findViewById(R.id.Ed_passengers_details);
        Ed_anyone_injured = (EditText) rootvView.findViewById(R.id.Ed_anyone_injured);
        Ed_police_report = (EditText) rootvView.findViewById(R.id.Ed_police_report);
        Ed_police_dept = (EditText) rootvView.findViewById(R.id.Ed_police_dept);
        Ed_car_vin_number = (EditText) rootvView.findViewById(R.id.Ed_car_vin_number);
        Ed_tow = (EditText) rootvView.findViewById(R.id.Ed_tow);
        Ed_location_incident = (EditText) rootvView.findViewById(R.id.Ed_location_incident);
        Ed_description_of_incident = (EditText) rootvView.findViewById(R.id.Ed_description_of_incident);
        Ed_claimant_name = (EditText) rootvView.findViewById(R.id.Ed_claimant_name);
        Ed_claimant_address = (EditText) rootvView.findViewById(R.id.Ed_claimant_address);


        Rl_dob = (RelativeLayout) rootvView.findViewById(R.id.Rl_dob);
        Rl_uber_mode_yes = (RelativeLayout) rootvView.findViewById(R.id.Rl_uber_mode_yes);
        Rl_uber_mode_no = (RelativeLayout) rootvView.findViewById(R.id.Rl_uber_mode_no);
        Rl_Lyft_mode_yes = (RelativeLayout) rootvView.findViewById(R.id.Rl_Lyft_mode_yes);
        Rl_Lyft_mode_no = (RelativeLayout) rootvView.findViewById(R.id.Rl_Lyft_mode_no);
        Rl_car_drivable_yes = (RelativeLayout) rootvView.findViewById(R.id.Rl_car_drivable_yes);
        Rl_car_drivable_no = (RelativeLayout) rootvView.findViewById(R.id.Rl_car_drivable_no);
        Rl_air_bag_deployed_yes = (RelativeLayout) rootvView.findViewById(R.id.Rl_air_bag_deployed_yes);
        Rl_air_bag_deployed_no = (RelativeLayout) rootvView.findViewById(R.id.Rl_air_bag_deployed_no);
        Drv_condition_raining = (RelativeLayout) rootvView.findViewById(R.id.Drv_condition_raining);
        Drv_condition_fog = (RelativeLayout) rootvView.findViewById(R.id.Drv_condition_fog);
        Drv_condition_snow = (RelativeLayout) rootvView.findViewById(R.id.Drv_condition_snow);
        Drv_condition_sunny = (RelativeLayout) rootvView.findViewById(R.id.Drv_condition_sunny);
        traffic_condition_heavy = (RelativeLayout) rootvView.findViewById(R.id.traffic_condition_heavy);
        traffic_condition_moderate = (RelativeLayout) rootvView.findViewById(R.id.traffic_condition_moderate);
        traffic_condition_no_traffic = (RelativeLayout) rootvView.findViewById(R.id.traffic_condition_no_traffic);
        Rl_claimant_dob = (RelativeLayout) rootvView.findViewById(R.id.Rl_claimant_dob);
        Rl_submit = (RelativeLayout) rootvView.findViewById(R.id.Rl_submit);


        Iv_uber_mode_yes = (ImageView) rootvView.findViewById(R.id.Iv_uber_mode_yes);
        Iv_uber_mode_no = (ImageView) rootvView.findViewById(R.id.Iv_uber_mode_no);
        Iv_Lyft_mode_yes = (ImageView) rootvView.findViewById(R.id.Iv_Lyft_mode_yes);
        Iv_Lyft_mode_no = (ImageView) rootvView.findViewById(R.id.Iv_Lyft_mode_no);
        Iv_car_drivable_yes = (ImageView) rootvView.findViewById(R.id.Iv_car_drivable_yes);
        Iv_car_drivable_no = (ImageView) rootvView.findViewById(R.id.Iv_car_drivable_no);
        Iv_air_bag_deployed_no = (ImageView) rootvView.findViewById(R.id.Iv_air_bag_deployed_no);
        Iv_air_bag_deployed_yes = (ImageView) rootvView.findViewById(R.id.Iv_air_bag_deployed_yes);
        Iv_drv_condi_raining = (ImageView) rootvView.findViewById(R.id.Iv_drv_condi_raining);
        Iv_drv_condi_fog = (ImageView) rootvView.findViewById(R.id.Iv_drv_condi_fog);
        Iv_drv_condi_snow = (ImageView) rootvView.findViewById(R.id.Iv_drv_condi_snow);
        Iv_drv_condi_sunny = (ImageView) rootvView.findViewById(R.id.Iv_drv_condi_sunny);
        Iv_traffic_condi_heavy = (ImageView) rootvView.findViewById(R.id.Iv_traffic_condi_heavy);
        Iv_traffic_condi_moderate = (ImageView) rootvView.findViewById(R.id.Iv_traffic_condi_moderate);
        Iv_traffic_condi_no_traffic = (ImageView) rootvView.findViewById(R.id.Iv_traffic_condi_no_traffic);


        Tv_dob = (TextView) rootvView.findViewById(R.id.Tv_dob);
        Tv_claimant_dob = (TextView) rootvView.findViewById(R.id.Tv_claimant_dob);

        Ll_agree_check = (LinearLayout) rootvView.findViewById(R.id.Ll_agree_check);


        Rl_dob.setOnClickListener(this);
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
        Rl_submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.Rl_dob:

                current_dob = "dob";
                AccidentDatePicker();

                break;

            case R.id.Rl_uber_mode_yes:

                Iv_uber_mode_yes.setImageResource(R.drawable.radio_on);
                Iv_uber_mode_no.setImageResource(R.drawable.radio_off);


                break;
            case R.id.Rl_uber_mode_no:

                Iv_uber_mode_yes.setImageResource(R.drawable.radio_off);
                Iv_uber_mode_no.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_Lyft_mode_yes:

                Iv_Lyft_mode_yes.setImageResource(R.drawable.radio_on);
                Iv_Lyft_mode_no.setImageResource(R.drawable.radio_off);


                break;
            case R.id.Rl_Lyft_mode_no:

                Iv_Lyft_mode_yes.setImageResource(R.drawable.radio_off);
                Iv_Lyft_mode_no.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_car_drivable_yes:

                Iv_car_drivable_yes.setImageResource(R.drawable.radio_on);
                Iv_car_drivable_no.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Rl_car_drivable_no:


                Iv_car_drivable_yes.setImageResource(R.drawable.radio_off);
                Iv_car_drivable_no.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_air_bag_deployed_yes:

                Iv_air_bag_deployed_no.setImageResource(R.drawable.radio_off);
                Iv_air_bag_deployed_yes.setImageResource(R.drawable.radio_on);


                break;
            case R.id.Rl_air_bag_deployed_no:

                Iv_air_bag_deployed_no.setImageResource(R.drawable.radio_on);
                Iv_air_bag_deployed_yes.setImageResource(R.drawable.radio_off);


                break;
            case R.id.Drv_condition_raining:


                Iv_drv_condi_raining.setImageResource(R.drawable.radio_on);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Drv_condition_fog:

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_on);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Drv_condition_snow:

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_on);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_off);

                break;
            case R.id.Drv_condition_sunny:

                Iv_drv_condi_raining.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_fog.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_snow.setImageResource(R.drawable.radio_off);
                Iv_drv_condi_sunny.setImageResource(R.drawable.radio_on);

                break;
            case R.id.traffic_condition_heavy:


                Iv_traffic_condi_heavy.setImageResource(R.drawable.radio_on);
                Iv_traffic_condi_moderate.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_no_traffic.setImageResource(R.drawable.radio_off);


                break;
            case R.id.traffic_condition_moderate:

                Iv_traffic_condi_heavy.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_moderate.setImageResource(R.drawable.radio_on);
                Iv_traffic_condi_no_traffic.setImageResource(R.drawable.radio_off);

                break;
            case R.id.traffic_condition_no_traffic:


                Iv_traffic_condi_heavy.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_moderate.setImageResource(R.drawable.radio_off);
                Iv_traffic_condi_no_traffic.setImageResource(R.drawable.radio_on);

                break;
            case R.id.Rl_claimant_dob:

                current_dob = "claimant_dob";
                AccidentDatePicker();

                break;
            case R.id.Rl_submit:

                break;

        }


    }



    private void AccidentDatePicker() {

        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), myDateListener, mYear, mMonth, mDay);

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

            }


        }
    };

    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(getActivity());
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
