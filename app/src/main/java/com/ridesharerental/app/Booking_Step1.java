package com.ridesharerental.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.ridesharerental.adapter.DeductionList_Adapter;
import com.ridesharerental.fragments.DirectInbox;
import com.ridesharerental.pojo.price_dedution_pojo;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.squareup.picasso.Picasso;
import com.williamww.silkysignature.views.SignaturePad;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Booking_Step1 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    RelativeLayout Rel_Back, Rel_Book;
    Common_Loader Loader;
    MultipartBody.Part filePart = null;
    TextView txt_car_name, txt_host_name;
    Spinner spn_pick_time;
    Typeface tf;
    ArrayList<String> pick_up_times = new ArrayList<String>();

    RadioButton Radio_by_day, radio_by_week, radio_by_month;
    RelativeLayout Rel_checkIn, rel_check_out;
    TextView txt_Check_In, txt_Check_Out, txt_PerDay, txt_PerWeek, txt_PerMonth;
    int is_signed = 0;
    AlertDialog alertDialog = null;

    private String car_Name = "", car_hostname = "", carper_day = "", carper_week = "", carper_month = "", CarId = "", Mininum_stay = "";
    String str_get_check_in = "", str_get_check_out = "";
    private ArrayList<price_dedution_pojo> deductionArrylist;
    private ExpandableHeightListView listview;
    private DeductionList_Adapter adapter;
    int current_deducetion_pos=0;

    SessionManager sessionManager;

    String str_userId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking__step1);

        sessionManager = new SessionManager(Booking_Step1.this);

        HashMap<String, String> details = sessionManager.getUserDetails();
        str_userId = details.get(sessionManager.KEY_USER_ID);


        Loader = new Common_Loader(Booking_Step1.this);

        deductionArrylist = new ArrayList<price_dedution_pojo>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            deductionArrylist = bundle.getParcelableArrayList("deductionarry");
            System.out.println("--------deductionArrylist size------" + deductionArrylist.size());
            car_Name = bundle.getString("Car_Name");
            car_hostname = bundle.getString("Car_HostName");
            carper_day = bundle.getString("Car_Day");
            carper_week = bundle.getString("Car_Week");
            carper_month = bundle.getString("Car_Month");
            CarId = bundle.getString("CarId");
            Mininum_stay = bundle.getString("minumu_stay");

            System.out.println("---------kannan minimum stay--------" + Mininum_stay);

            str_get_check_in = bundle.getString("check_in");
            str_get_check_out = bundle.getString("check_out");

            System.out.println("--------booking 1 check out--------->" + str_get_check_out);

            if (str_get_check_out != null && !str_get_check_out.equals("")) {
                //txt_Check_In.
            }
        }
        init();
        current_date();
        Rel_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Booking_Step1.this.finish();
            }
        });

        Rel_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_Check_In.getText().toString().length() > 0) {

                    if (txt_Check_Out.getText().toString().length() > 0) {
                        if(spn_pick_time.getSelectedItemPosition() != 0) {

                            Intent new_intent = new Intent(Booking_Step1.this, Booking_Step2.class);
                            new_intent.putExtra("Car_Name", car_Name);
                            new_intent.putExtra("Car_HostName", car_hostname);
                            new_intent.putExtra("Car_CheckIn", txt_Check_In.getText().toString());
                            new_intent.putExtra("Car_CheckOut", txt_Check_Out.getText().toString());
                            new_intent.putExtra("CarId", CarId);
                            new_intent.putExtra("pick_up_hour", spn_pick_time.getSelectedItem().toString());
                            new_intent.putExtra("deductibleId", deductionArrylist.get(current_deducetion_pos).getId());
                            startActivity(new_intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }else
                        {
                            Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.pickup_time));
                        }
                    } else {
                        Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.select_return_date));
                    }
                } else {
                    Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.select_pickup_date));
                }

            }
        });

        Rel_checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate_start();
            }
        });


        rel_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate_end();
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                current_deducetion_pos=position;


                for(int i=0; i<deductionArrylist.size();i++){


                    if(i==position){

                        deductionArrylist.get(i).setSelectflag("yes");


                    }else {

                        deductionArrylist.get(i).setSelectflag("no");

                    }


                }


             adapter.notifyDataSetChanged();



            }
        });


    }


    private void setdate_start() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(Booking_Step1.this, datePickerListener_start, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
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
            txt_Check_In.setText(getStart_Date_Time);
            str_get_check_in = getStart_Date_Time;
            txt_Check_Out.setHint(getResources().getString(R.string.date_hint));

        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener_end = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            String getStart_Date_Time = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
            Log.d("Date------->", getStart_Date_Time);

            txt_Check_Out.setText(getStart_Date_Time);
            str_get_check_out = getStart_Date_Time;
        }
    };

    private void selectionmodeday(long date1, DatePickerDialog dialog) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, Integer.parseInt(Mininum_stay));
        long end = calendar.getTimeInMillis();
        System.out.println("----Ranjith Date----->" + String.valueOf(end));
        dialog.getDatePicker().setMinDate(end);

    }

    private void selectionmodeweek(long date1, DatePickerDialog dialog) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, 7);
        long end = calendar.getTimeInMillis();
        System.out.println("----Ranjith Week----->" + String.valueOf(end));
        dialog.getDatePicker().setMinDate(end);

    }

    private void selectionmodemonth(long date1, DatePickerDialog dialog) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, 30);
        long end = calendar.getTimeInMillis();
        System.out.println("----Ranjith Month----->" + String.valueOf(end));
        dialog.getDatePicker().setMinDate(end);

    }


    private void setdate_end() {

        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(Booking_Step1.this, datePickerListener_end, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        long maxdate = 0;
        Date date = null;
        if (Radio_by_day.isChecked()) {

            try {

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date date1 = sdf.parse(txt_Check_In.getText().toString());
                maxdate = date1.getTime();
                selectionmodeday(maxdate, dialog);


            } catch (ParseException e) {
                e.printStackTrace();
            }


        } else if (radio_by_week.isChecked()) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date date1 = sdf.parse(txt_Check_In.getText().toString());

                maxdate = date1.getTime();
                //default_select_date(maxdate);
                selectionmodeweek(maxdate, dialog);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (radio_by_month.isChecked()) {
            try {

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                Date date1 = sdf.parse(txt_Check_In.getText().toString());
                maxdate = date1.getTime();
                selectionmodemonth(maxdate, dialog);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        dialog.setTitle("");
        dialog.show();
    }


    public void init() {
        load_profile_data();

        Rel_Back = (RelativeLayout) findViewById(R.id.booking_step1_backLAY);
        Rel_Book = (RelativeLayout) findViewById(R.id.booking_step2leftLAY);

        txt_car_name = (TextView) findViewById(R.id.txt_car_name);
        txt_host_name = (TextView) findViewById(R.id.txt_host_name);
        txt_PerDay = (TextView) findViewById(R.id.txt_perday);
        txt_PerWeek = (TextView) findViewById(R.id.txt_perweek);

        txt_PerMonth = (TextView) findViewById(R.id.txt_permonth);

        Radio_by_day = (RadioButton) findViewById(R.id.radio_by_day);
        radio_by_week = (RadioButton) findViewById(R.id.radio_by_week);
        radio_by_month = (RadioButton) findViewById(R.id.radio_by_month);
        Radio_by_day.setChecked(true);

        Rel_checkIn = (RelativeLayout) findViewById(R.id.rel_book_check_in);
        rel_check_out = (RelativeLayout) findViewById(R.id.rel_book_check_out);
        txt_Check_In = (TextView) findViewById(R.id.booking_start_date);
        txt_Check_Out = (TextView) findViewById(R.id.booking_end_date);
        listview = (ExpandableHeightListView) findViewById(R.id.dedution_list);
        spn_pick_time = (Spinner) findViewById(R.id.spn_pick_time);
        tf = Typeface.createFromAsset(getAssets(), "fonts/Sofia Pro Regular.ttf");
        setSpinner();

        if (str_get_check_in != null && !str_get_check_in.equals("") && !str_get_check_in.equals(null)) {
            txt_Check_In.setText(str_get_check_in);
        }

        if (str_get_check_out != null && !str_get_check_out.equals("")) {
            txt_Check_Out.setText(str_get_check_out);
        }


        if (deductionArrylist.size() > 0) {

            adapter = new DeductionList_Adapter(Booking_Step1.this, deductionArrylist);
            listview.setAdapter(adapter);
            listview.setExpanded(true);
            adapter.notifyDataSetChanged();

        }


        //-----------------checked listener this
        Radio_by_day.setOnCheckedChangeListener(this);
        radio_by_week.setOnCheckedChangeListener(this);
        radio_by_month.setOnCheckedChangeListener(this);


        //-------------------- bundle value set text

        txt_car_name.setText(car_Name);
        txt_host_name.setText(car_hostname);
        txt_PerDay.setText(getResources().getString(R.string.by_day) + " " + carper_day + " " + getResources().getString(R.string.per_day));
        txt_PerWeek.setText(getResources().getString(R.string.by_week) + " " + carper_week + " " + getResources().getString(R.string.per_week));
        txt_PerMonth.setText(getResources().getString(R.string.by_month) + " " + carper_month + " " + getResources().getString(R.string.per_month));

    }


    //-----------------service---------------------
    public void Load_Data1() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("language", "en");
        map.put("currency", "USD");
        Call<ResponseBody> call = apiService.post_amenties();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

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

                        Loader.dismiss();
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
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.radio_by_day:
                if (Radio_by_day.isChecked()) {

                    if (str_get_check_in != null && !str_get_check_in.equals("")) {
                        try {
                            long maxdate = 0;
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                            Date date1 = sdf.parse(txt_Check_In.getText().toString());
                            maxdate = date1.getTime();
                            default_select_date(maxdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    radio_by_week.setChecked(false);
                    radio_by_month.setChecked(false);
                }
                break;
            case R.id.radio_by_week:
                if (radio_by_week.isChecked()) {

                    if (str_get_check_in != null && !str_get_check_in.equals("")) {
                        try {
                            long maxdate = 0;
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                            Date date1 = sdf.parse(txt_Check_In.getText().toString());
                            maxdate = date1.getTime();
                            default_select_week(maxdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Radio_by_day.setChecked(false);
                    radio_by_month.setChecked(false);
                }
                break;
            case R.id.radio_by_month:
                if (radio_by_month.isChecked()) {

                    if (str_get_check_in != null && !str_get_check_in.equals("")) {
                        try {
                            long maxdate = 0;
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
                            Date date1 = sdf.parse(txt_Check_In.getText().toString());
                            maxdate = date1.getTime();
                            default_select_month(maxdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    radio_by_week.setChecked(false);
                    Radio_by_day.setChecked(false);
                }
                break;

        }

    }

    //--------------------------------------------------

    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(Booking_Step1.this);
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

    private void default_select_date(long date1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, Integer.parseInt(Mininum_stay));

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String formattedDate = df.format(calendar.getTime());
        System.out.println("-------Final Solutions------?" + formattedDate);
        txt_Check_Out.setText(formattedDate);
        int month = calendar.getTime().getMonth();
        int day = calendar.getTime().getDay();
        long year = calendar.getTime().getYear();
        System.out.println("--------Share day-------->" + month + "/" + day + "/" + year);
        System.out.println("--------Share day-------->" + calendar.getTimeInMillis());

    }

    private void default_select_week(long date1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, 7);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(calendar.getTime());
        System.out.println("-------Final Solutions------?" + formattedDate);
        txt_Check_Out.setText(formattedDate);
        int month = calendar.getTime().getMonth();
        int day = calendar.getTime().getDay();
        long year = calendar.getTime().getYear();
        System.out.println("--------Share day-------->" + month + "/" + day + "/" + year);
        System.out.println("--------Share day-------->" + calendar.getTimeInMillis());

    }

    private void default_select_month(long date1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date1);
        calendar.add(Calendar.DATE, 30);

        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(calendar.getTime());
        System.out.println("-------Final Solutions------?" + formattedDate);
        txt_Check_Out.setText(formattedDate);
        int month = calendar.getTime().getMonth();
        int day = calendar.getTime().getDay();
        long year = calendar.getTime().getYear();
        System.out.println("--------Share day-------->" + month + "/" + day + "/" + year);
        System.out.println("--------Share day-------->" + calendar.getTimeInMillis());

    }


    public void current_date() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String DateToStr = format.format(curDate);
        if (str_get_check_in != null && !str_get_check_in.equals("")) {

        } else {
            str_get_check_in = DateToStr;
            txt_Check_In.setText(str_get_check_in);
        }
        System.out.println(DateToStr);
        try {
            long maxdate = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            Date date1 = sdf.parse(DateToStr);
            maxdate = date1.getTime();
            // default_select_week(maxdate);
            default_select_date(maxdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSpinner() {

        pick_up_times.add("Pick-UP Time");
        pick_up_times.add("8:00 am");
        pick_up_times.add("9:00 am");
        pick_up_times.add("10:00 am");
        pick_up_times.add("11:00 am");
        pick_up_times.add("12:00 pm");
        pick_up_times.add("1:00 pm");
        pick_up_times.add("2:00 pm");
        pick_up_times.add("3:00 pm");
        pick_up_times.add("4:00 pm");
        pick_up_times.add("5:00 pm");
        pick_up_times.add("6:00 pm");
        pick_up_times.add("7:00 pm");

        ArrayAdapter pickupAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,pick_up_times);
        pickupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(Booking_Step1.this, pick_up_times);
        spn_pick_time.setAdapter(customSpinnerAdapter1);
    }
    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(Booking_Step1.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            txt.setTypeface(tf);
            txt.setTextColor(Color.parseColor("#464646"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(Booking_Step1.this);
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(0, 0, 0, 0);
            txt.setTextSize(16);
            txt.setTypeface(tf);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#464646"));
            return txt;
        }

    }

    public void load_profile_data() {
        //loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("commonId", str_userId);
        Call<ResponseBody> call = apiService.show_driver_profile(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                   // loader.dismiss();
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
                        Log.e("----Profile Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_status_code = response_obj.getString("status");

                        if (str_status_code.equals("1")) {
                            JSONObject common_Obj = object.getJSONObject("commonArr");

                            if(common_Obj.getString("signature_image").equalsIgnoreCase("") || common_Obj.getString("signature_image").length()==0 || !common_Obj.getString("signature_image").contains("http"))
                            {
                                AlertSignatureVerify(getResources().getString(R.string.app_name), getString(R.string.signature_alert));
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }

    private void AlertSignatureVerify(String title, String alert) {
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup



            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Booking_Step1.this);
// ...Irrelevant code for customizing the buttons and title
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_signature, null);
            dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);




        final SignaturePad mSignaturePad = (SignaturePad) dialogView.findViewById(R.id.signature_pad);
        final TextView txt_signCancel = (TextView) dialogView.findViewById(R.id.txt_signCancel);

        final TextView txt_signOK = (TextView) dialogView.findViewById(R.id.txt_signOK);

            alertDialog = dialogBuilder.create();

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                txt_signOK.setEnabled(true);
                txt_signCancel.setEnabled(true);
                is_signed = 1;

            }

            @Override
            public void onClear() {
                txt_signOK.setEnabled(true);
                txt_signCancel.setEnabled(true);
                is_signed = 0;
            }
        });
        txt_signCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                is_signed = 0;
            }
        });
        txt_signOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                try {
                    if(is_signed == 1)
                        uploadSignature(savebitmap(signatureBitmap));
                    else
                        Alert(getString(R.string.action_opps),getString(R.string.alert_signature));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        int height = (int)(getResources().getDisplayMetrics().heightPixels);
        int width = (int)(getResources().getDisplayMetrics().widthPixels);

        alertDialog.getWindow().setLayout(width, height);

            alertDialog.show();


    }
    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
    public File savebitmap(Bitmap bmp) throws IOException {
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
//        File f = new File(Environment.getExternalStorageDirectory()
//                + File.separator + "sign"+name+".jpg");
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator + "sign"+name+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        Log.e("sign path",f.getPath());
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }
    private void uploadSignature(File f) {

        Loader.show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        // MultipartBody.Part is used to send also the actual file name


        filePart = MultipartBody.Part.createFormData("signature_image", f.getName(), requestFile);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", str_userId);


        Call<ResponseBody> call = apiService.upload_driver_sign(header, filePart);
        System.out.println("-----------upload_driver_sign url------>" + call.request().url().toString());



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    alertDialog.dismiss();
                    Alert(getString(R.string.action_success),getString(R.string.alert_signature_success));
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


    }
}
