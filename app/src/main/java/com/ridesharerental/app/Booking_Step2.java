package com.ridesharerental.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ridesharerental.app.R.id.cast_button_type_forward_30_seconds;
import static com.ridesharerental.app.R.id.edit_street;
import static com.yalantis.ucrop.util.BitmapLoadUtils.exifToDegrees;

public class Booking_Step2 extends Activity {
    Common_Loader Loader;
    RelativeLayout Rel_Back, Rel_Book;
    TextView txt_car_name, txt_total_price, txt_host_name, txt_check_in, txt_check_out, txt_price_break_down, txt_license_country,txt_license_expire_date, txt_dob;
    RelativeLayout Rel_expire_date, Rel_DOB;
    EditText edit_license_Number;
    TextView edi_street;
    EditText edit_first_Name, edit_Last_Name, edit_phone_Number, edit_house_number, edit_city, edit_state, edit_zip_code;
    RelativeLayout Rel_document_upload;
    CheckBox checkBox_Agree;

    Typeface tf;
    ArrayList<String> pick_up_times = new ArrayList<String>();

    String pick_up_hour="", car_Name = "", car_hostname = "", checkIndate = "", checkoutdate = "", CarId = "",str_license_exp_date="",str_dob="",deductibleId="";
    SessionManager sessionManager;
    String user_id="";

    LinearLayout pricinglayout;
    List<Address> addresses=new ArrayList<>();


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_MAIN_ACTIVITY = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int SELECT_FILE = 200;
    private String filePath = null;
    private File image = null;
    MultipartBody.Part filePart = null;
    File destination;
    private static Uri fileUri;
    ImageView img_document;

    RelativeLayout rel_street;

    TextView txt_agree;

    //------------- chan------------------
    private final int PERMISSION_CAMERA_REQUEST_CODE = 230;
    private final int PERMISSION_GALLERY_REQUEST_CODE = 235;
    private static int CAMERA_REQUEST_FLAG = 1021;
    private static int GALLERY_REQUEST_FLAG = 891;
    private Uri mImageCaptureUri, myOutputURI;
    File myCapturedImage, myImageRoot;
    String imgpath = "", myDirectoryNameStr = "", myNameStr = "",imagePath = "";
    private Bitmap selectedBitmap;
    Dialog dialog;

    LinearLayout linear_empty;

    String str_total_amont="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking__step2);
        sessionManager = new SessionManager(Booking_Step2.this);
        Loader = new Common_Loader(Booking_Step2.this);


        sessionManager = new SessionManager(Booking_Step2.this);

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);





        //----------------------chan---------------------
        myImageRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), myDirectoryNameStr);
        if (!myImageRoot.exists()) {
            myImageRoot.mkdir();
        } else if (!myImageRoot.isDirectory()) {
            myImageRoot.delete();
            myImageRoot.mkdir();
        }



        myNameStr = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        myCapturedImage = new File(myImageRoot, myNameStr + ".jpg");

        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            car_Name = bundle.getString("Car_Name");
            car_hostname = bundle.getString("Car_HostName");
            checkIndate = bundle.getString("Car_CheckIn");
            checkIndate = bundle.getString("Car_CheckIn");
            checkoutdate = bundle.getString("Car_CheckOut");
            deductibleId = bundle.getString("deductibleId");
            pick_up_hour = bundle.getString("pick_up_hour");

            System.out.println("---------Ranjith Checkout date------->"+checkoutdate);
            System.out.println("---------kannan deductibleId------->"+deductibleId);


            CarId = bundle.getString("CarId");
        }

        init();

        //----------------------- set on click listener


        edi_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent new_intent = new Intent(Booking_Step2.this, Search_Google_Places.class);
                startActivityForResult(new_intent, 601);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        rel_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent new_intent = new Intent(Booking_Step2.this, Search_Google_Places.class);
                startActivityForResult(new_intent, 601);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        Rel_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Booking_Step2.this.finish();
            }
        });

        Rel_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_license_Number.getText().toString().length() > 0)
                {

                    if (txt_dob.getText().toString().trim().length() > 0)
                    {

                        if (txt_license_expire_date.getText().toString().trim().length() > 0)
                        {
                            if(edit_first_Name.getText().toString().trim().length()>0)
                            {

                                if(edit_Last_Name.getText().toString().trim().length()>0)
                                {
                                    if(edit_phone_Number.getText().toString().trim().length()>0)
                                    {

                                       // if(edit_house_number.getText().toString().trim().length()>0)
                                       // {
                                            if(edi_street.getText().toString().trim().length()>0)
                                            {


                                                        if(edit_zip_code.getText().toString().trim().length()>0)
                                                        {

                                                                if (checkBox_Agree.isChecked()) {
//                                                                Calendar currnetDateTime = Calendar.getInstance();
//
//                                                                SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
//                                                                formatDate.format(currnetDateTime.getTime());
//                                                                System.out.println("Time Display: " + formatDate.format(currnetDateTime.getTime()));
//                                                                if(checkPickuptime(formatDate.format(currnetDateTime.getTime())))
                                                                    //Savedata(formatDate.format(currnetDateTime.getTime()));
                                                                    Savedata();
//                                                                else
//                                                                    Alert(getResources().getString(R.string.action_opps), "Pickup time exceed.Only between 8:00 AM to  7:00 PM");
                                                                } else {
                                                                    //------------- agree  mandatory
                                                                    Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.checked_agree));
                                                                }
                                                        }
                                                        else
                                                        {
                                                            Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.zip_code));
                                                        }
                                            }
                                            else
                                            {
                                                Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.entet_address));
                                            }
                                       // }
                                      //  else
                                       // {
                                          //  Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_apartment_number));
                                       // }


                                    }
                                    else
                                    {
                                        Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_phone_number));
                                    }
                                }
                                else
                                {
                                    Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_last_name_));
                                }


                            }
                            else
                            {
                                Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_first_name_));
                            }


                        } else {
                            //------------- license dob  mandatory
                            Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_license_expire));
                        }

                    } else {
                        //------------- dob  mandatory
                        Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_dob));
                    }

                } else {
                    //------------- license number mandatory
                    Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.enter_license_number));
                }

            }
        });
        txt_price_break_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(Booking_Step2.this, PriceBreakdown.class);
                new_intent.putExtra("Car_Name", car_Name);
                new_intent.putExtra("Car_HostName", car_hostname);
                new_intent.putExtra("Car_CheckIn", checkIndate);
                new_intent.putExtra("Car_CheckOut", checkoutdate);
                new_intent.putExtra("CarId", CarId);
                new_intent.putExtra("deductibleId", deductibleId);
                startActivity(new_intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });



        Rel_document_upload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               // selectImage();
                dialog_popup();
            }
        });



        if (!isDeviceSupportCamera())
        {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }


    }

    private boolean checkPickuptime(String format) {
        System.out.println("Time Display: " + format);
//        StringTokenizer tk = new StringTokenizer(currnetDateTime);
//        String date = tk.nextToken();
//        String time = tk.nextToken();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
//        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
//        Date dt;
//        try {
//            dt = sdf.parse(time);
//            System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        LocalTime localTime = LocalTime.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
//        System.out.println(localTime.format(dateTimeFormatter));


        try {
            String string1 = "08:00:00";
            Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);

            String string2 = "19:00:00";
            Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            calendar2.add(Calendar.DATE, 1);

            String someRandomTime = format;
            Date d = new SimpleDateFormat("HH:mm:ss").parse(someRandomTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(d);
            calendar3.add(Calendar.DATE, 1);

            Date x = calendar3.getTime();
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                System.out.println("timing true");
                return true;
            }else {
                System.out.print("timing false");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean isDeviceSupportCamera()
    {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private void Savedata() {

        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", sessionManager.getUserDetails().get(sessionManager.KEY_USER_ID));


        HashMap<String, String> map = new HashMap<>();

        try {

            map.put("carId", CarId);
            map.put("firstname", edit_first_Name.getText().toString());
            map.put("lastname", edit_Last_Name.getText().toString());

            map.put("licence_exp_date", str_license_exp_date );
            //  map.put("address", edi_street.getText().toString()+","+edit_city.getText().toString()+","+edit_state.getText().toString());
            if(edi_street.getText().toString().length()>0)
            {
                map.put("address", edi_street.getText().toString());
            }
            if(edit_house_number.getText().toString().trim().length()>0)
            {
                map.put("apt_no", edit_house_number.getText().toString());
            }


            //map.put("city", edit_city.getText().toString());
          //  map.put("state", edit_state.getText().toString());

            map.put("birthday", str_dob);
            // map.put("licence_state",txt_license_country.getText().toString());
            map.put("licence_number", edit_license_Number.getText().toString());
            map.put("phone_no", edit_phone_Number.getText().toString());
            map.put("zip", edit_zip_code.getText().toString());

            map.put("date_from", checkIndate);
            map.put("date_to", checkoutdate);
            map.put("deductible", deductibleId);
            map.put("pickup_hour", pick_up_hour);
            System.out.println("pickup_hour---->"+pick_up_hour);


            System.out.println("*****************deductibleId*********************"+deductibleId);



            Set keys = map.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = (String) map.get(key);
                System.out.println(key + ":" + value);
            }
        }catch (Exception e){

        }



        Call<ResponseBody> call = apiService.proceed_Booking(header,map);
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
                    }else {
                        Loader.dismiss();
                        if (response != null && response.body() != null) {
                            String Str_response = response.body().string();
                            Log.e("----Country Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            JSONObject response_obj=object.getJSONObject("responseArr");
                            String str_status_code=response_obj.getString("status");
                            String str_msg=response_obj.getString("msg");

                            if (str_status_code.equalsIgnoreCase("1"))
                            {
                                JSONObject obj=object.getJSONObject("responseArr");
                                String booking_no=obj.getString("booking_no");
                                Intent new_intent = new Intent(Booking_Step2.this, Booking_Step3.class);
                                new_intent.putExtra("Booking_number",booking_no);
                                new_intent.putExtra("car_name",car_Name);
                                new_intent.putExtra("owner_name",car_hostname);
                                new_intent.putExtra("total_amount",str_total_amont);
                                new_intent.putExtra("msg","");
                                startActivity(new_intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                            }else
                                {
                                Alert(getResources().getString(R.string.action_opps),str_msg);

//                                    Intent new_intent = new Intent(Booking_Step2.this, Booking_Step3.class);
//                                    new_intent.putExtra("Booking_number",str_status_code);
//                                    new_intent.putExtra("msg",str_msg);
//                                    new_intent.putExtra("car_name",car_Name);
//                                    new_intent.putExtra("owner_name",car_hostname);
//                                    new_intent.putExtra("total_amount",str_total_amont);
//                                    startActivity(new_intent);
//                                    overridePendingTransition(R.anim.enter, R.anim.exit);
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
    }



    public void init() {


        linear_empty=(LinearLayout)findViewById(R.id.linear_empty);
        linear_empty.setVisibility(View.VISIBLE);
        txt_agree=(TextView)findViewById(R.id.txt_agree);
        rel_street=(RelativeLayout)findViewById(R.id.rel_street);

        img_document=(ImageView)findViewById(R.id.upload_img);
        Rel_Back = (RelativeLayout) findViewById(R.id.booking_step2_backLAY);
        Rel_Book = (RelativeLayout) findViewById(R.id.booking_step2leftLAY);
        pricinglayout = (LinearLayout) findViewById(R.id.pricinglayout);
        txt_total_price = (TextView) findViewById(R.id.txt_total_price);
        txt_car_name = (TextView) findViewById(R.id.txt_car_name);
        txt_host_name = (TextView) findViewById(R.id.txt_host_name);
        txt_check_in = (TextView) findViewById(R.id.txt_check_In);
        txt_check_out = (TextView) findViewById(R.id.txt_check_Out);
        txt_price_break_down = (TextView) findViewById(R.id.txt_price_break_down);
        txt_license_expire_date = (TextView) findViewById(R.id.txt_expire_date);
        txt_license_country = (TextView) findViewById(R.id.txt_license_country);
        txt_dob = (TextView) findViewById(R.id.my_editprofile_dob);

        Rel_expire_date = (RelativeLayout) findViewById(R.id.rel_expire_date);
        Rel_DOB = (RelativeLayout) findViewById(R.id.rel_dob);

        edit_first_Name = (EditText) findViewById(R.id.edit_first_Name);
        edit_Last_Name = (EditText) findViewById(R.id.edit_last_name);
        edit_phone_Number = (EditText) findViewById(R.id.edit_phone_number);
        edit_house_number = (EditText) findViewById(R.id.edit_house_number);
        edi_street = (TextView) findViewById(edit_street);
        edit_city = (EditText) findViewById(R.id.edit_city);
        edit_state = (EditText) findViewById(R.id.edit_state);
        edit_zip_code = (EditText) findViewById(R.id.txt_zip_code);


        Rel_document_upload = (RelativeLayout) findViewById(R.id.rel_document_upload);
        checkBox_Agree = (CheckBox) findViewById(R.id.check_agree);

        edit_license_Number = (EditText) findViewById(R.id.booking_license_number);


        //---------------------- on click listener
        Rel_expire_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepicker("license");
            }
        });
        Rel_DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepicker("dob");
            }
        });

        //--------------------------set text
        txt_car_name.setText(car_Name);
        txt_check_in.setText(checkIndate);

        txt_check_out.setText(checkoutdate);

        txt_host_name.setText(car_hostname);

        Load_Data();

    }


    private void datepicker(final String string)
    {

        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog;
        if (string.equalsIgnoreCase("license")) {
            dialog = new DatePickerDialog(Booking_Step2.this, datePickerListener_license, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());

        } else {
            dialog = new DatePickerDialog(Booking_Step2.this, datePickerListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener_license = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            String getStart_Date_Time = String.valueOf(selectedMonth + 1)  + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
            Log.d("Date------->", getStart_Date_Time);
            txt_license_expire_date.setText(getStart_Date_Time);
            str_license_exp_date = getStart_Date_Time;

        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            String getStart_Date_Time = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
            Log.d("Date------->", getStart_Date_Time);
            txt_dob.setText(getStart_Date_Time);
            str_dob = getStart_Date_Time;
        }
    };

    //-----------------service---------------------
    public void Load_Data() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", sessionManager.getUserDetails().get(sessionManager.KEY_USER_ID));

        HashMap<String, String> map = new HashMap<>();
        map.put("carId", CarId);
        map.put("date_from", checkIndate);
        map.put("date_to", checkoutdate);
        map.put("deductible", deductibleId);

        Set keys = map.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) map.get(key);
            System.out.println(""+key+":"+value);
        }

        Call<ResponseBody> call = apiService.pricing(header, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful())
                    {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else

                    {
                        if (response != null && response.body() != null)
                        {
                            linear_empty.setVisibility(View.GONE);
                            String Str_response = response.body().string();
                            Log.e("----Pricing Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);

                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1"))
                            {
                                linear_empty.setVisibility(View.GONE);
                                JSONObject data = object.getJSONObject("responseArr");

                                JSONObject obj_driver_details=data.getJSONObject("driverDetails");
                                String str_license=obj_driver_details.getString("licence_image");
                                String address=obj_driver_details.getString("address");
                                if(edi_street!=null && !edi_street.equals(""))
                                {
                                    edi_street.setText(address);
                                }
                                else

                                {
                                    edi_street.setText("");
                                }


                                if (str_license!=null && !str_license.equals(""))
                                {
                                    Picasso.with(Booking_Step2.this)
                                            .load(str_license)
                                            .placeholder(getResources().getDrawable(R.drawable.upload_new))
                                            .into(img_document);
                                }
                                JSONArray price = data.getJSONArray("pricingArr");
                                if(price.length()>0)
                                {
                                    for (int priceARR=0;priceARR <price.length();priceARR++)
                                    {
                                        JSONObject obj = price.getJSONObject(priceARR);
                                        if (obj.getString("key").equalsIgnoreCase("Total"))
                                    {
                                        if (!obj.getString("value").equalsIgnoreCase("") && !obj.getString("value").equalsIgnoreCase("0.0")&& !obj.getString("value").equalsIgnoreCase("0.00"))
                                        {
                                            if (!object.getJSONObject("commonArr").getString("currencySymbol").equalsIgnoreCase(""))
                                            {
                                                txt_total_price.setText(object.getJSONObject("commonArr").getString("currencySymbol")+" "+obj.getString("value"));
                                                str_total_amont=object.getJSONObject("commonArr").getString("currencySymbol")+" "+obj.getString("value");
                                                System.out.println("----------total amount------->"+str_total_amont);
                                                txt_agree.setText(getResources().getString(R.string.agree_to_pay)+" "+object.getJSONObject("commonArr").getString("currencySymbol")+" "+obj.getString("value")+" "+getResources().getString(R.string.terms_condition));
                                            }
                                        }else {
                                            pricinglayout.setVisibility(View.GONE);
                                        }
                                    }
                                    }
                                }

                                if (!data.getJSONObject("driverDetails").getString("licence_number").equalsIgnoreCase("")) {
                                    edit_license_Number.setText(data.getJSONObject("driverDetails").getString("licence_number"));
                                }
                                if (!data.getJSONObject("driverDetails").getString("licence_exp_date").equalsIgnoreCase("")) {
                                    str_license_exp_date  = (data.getJSONObject("driverDetails").getString("licence_exp_date"));
                                    txt_license_expire_date.setText(str_license_exp_date);
                                }

                                if (!data.getJSONObject("driverDetails").getString("birthday").equalsIgnoreCase("")) {
                                    str_dob =(data.getJSONObject("driverDetails").getString("birthday"));
                                    txt_dob.setText(str_dob);
                                }

                                if (!data.getJSONObject("driverDetails").getString("firstname").equalsIgnoreCase("")) {
                                    edit_first_Name.setText(data.getJSONObject("driverDetails").getString("firstname"));
                                }

                                if (!data.getJSONObject("driverDetails").getString("lastname").equalsIgnoreCase("")) {
                                    edit_Last_Name.setText(data.getJSONObject("driverDetails").getString("lastname"));
                                }

                                if (!data.getJSONObject("driverDetails").getString("phone_no").equalsIgnoreCase("")) {
                                    edit_phone_Number.setText(data.getJSONObject("driverDetails").getString("phone_no"));
                                }

                                if (!data.getJSONObject("driverDetails").getString("apt_no").equalsIgnoreCase("")) {
                                    edit_house_number.setText(data.getJSONObject("driverDetails").getString("apt_no"));
                                }
                                if (!data.getJSONObject("driverDetails").getString("city").equalsIgnoreCase("")) {
                                    edit_city.setText(data.getJSONObject("driverDetails").getString("city"));
                                }
                                if (!data.getJSONObject("driverDetails").getString("state").equalsIgnoreCase("")) {
                                    edit_state.setText(data.getJSONObject("driverDetails").getString("state"));
                                }
                                if (!data.getJSONObject("driverDetails").getString("zip").equalsIgnoreCase("")) {
                                    edit_zip_code.setText(data.getJSONObject("driverDetails").getString("zip"));
                                }

                            } else
                                {
                                    linear_empty.setVisibility(View.VISIBLE);
                            }
                            Loader.dismiss();
                        }
                        else
                        {
                            linear_empty.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e)
                {
                    linear_empty.setVisibility(View.VISIBLE);
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

    //---------------------Alert

    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(Booking_Step2.this);
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



    private  void selectImage()
    {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Booking_Step2.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo"))
                {
                    //----------------------------7.0 camera intent actions-----------------
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                } else if (items[item].equals("Choose from Library")) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_FILE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void dispatchTakePictureIntent() throws IOException
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                //destination=createImageFile();

                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                System.out.println("--------original--------->"+ myCapturedImage.getAbsolutePath());
                System.out.println("--------duplicate--------->"+photoURI.getPath());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_FLAG);

            }
        }
    }




    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {


        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 601)
            {
                String location_name = data.getStringExtra("location_name");
                edi_street.setText(location_name);
                String latitude = data.getStringExtra("latitude");
                String langitude = data.getStringExtra("langitude");

                *//*if(latitude!=null && !latitude.equalsIgnoreCase("0.0"))
                {
                    Double d_latitude = Double.parseDouble(latitude);
                    Double d_longitude = Double.parseDouble(langitude);
                    currecnt_location(d_latitude,d_longitude);
                }*//*
            }
        }
          if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                if (destination != null)
                {
                    filePath = destination.getAbsolutePath();
                    // mImageCaptureUri = Uri.fromFile(new File(filePath));
                    // Log.e("selectedImagePath------->", "" + filePath);

                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bmp = BitmapFactory.decodeFile(filePath, bmOptions);

                    System.out.println("-----true------------>"+path);
                    System.out.println("--false------------>"+filePath);
                    if(bmp!=null)
                    {
                        int image_width = bmp.getWidth();
                        int image_hight = bmp.getHeight();
                        System.out.println("----Ran-Width--------->"+image_width);
                        System.out.println("---ran--Height--------->"+image_hight);


                        if (image_width >= 1000 && image_hight >= 700)
                        {
                            img_document.setImageBitmap(bmp);
                            Alert_ask("Photo Upload", "Are you sure want to upload document?");
                        }
                        else
                        {
                            Alert("Alert","Upload Image size should be greater than 1000 * 700 Px");
                        }

                    }
                }
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
        else if (requestCode == SELECT_FILE)
        {
            if (resultCode == RESULT_OK)
            {

                Uri  urr = data.getData();
                if (null != urr)
                {
                    try
                    {
                        Bitmap  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), urr);
                        String[] proj = { MediaStore.Images.Media.DATA };
                        CursorLoader loader = new CursorLoader(Booking_Step2.this, urr, proj, null, null, null);
                        Cursor cursor1 = loader.loadInBackground();
                        int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor1.moveToFirst();
                        filePath= cursor1.getString(column_index);
                        System.out.println("--------Resultss----->"+filePath);
                        File f = new File(filePath);
                        destination=f;
                        cursor1.close();

                        int image_width = bitmap.getWidth();
                        int image_hight = bitmap.getHeight();
                        if (image_width >= 1000 && image_hight >= 700)
                        {
                            img_document.setImageBitmap(bitmap);
                            Alert_ask("Photo Upload", "Are you sure want to upload document?");
                        }
                        else
                        {
                            Alert("Alert","Upload Image size should be greater than 1000 * 700 Px");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //onSelectFromGalleryResult(data);
            }
        }

        else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // launching upload activity
                //launchUploadActivity(false);
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }*/


   /* private File createImageFile() throws IOException
    {


        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        File ranjiht  = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        destination=new File(Environment.getExternalStorageDirectory(), name + ".jpg");


        return ranjiht;
    }*/

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }





    public void upload_photo()
    {
        Loader.show();
        System.out.println("--------file path Image------>"+filePath);

        File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b= BitmapFactory.decodeFile(destination.getAbsolutePath());
        Bitmap out = Bitmap.createScaledBitmap(b, 600, 400, false);
        File file = new File(dir, "resize.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {}

        image = new File(file.getAbsolutePath());
        // add another part within the multipart request
        RequestBody requestName =RequestBody.create(MediaType.parse("multipart/form-data"), image.getName());

        //  RequestBody requestBoatId =RequestBody.create(MediaType.parse("multipart/form-data"), str_boat_id);
        // create RequestBody instance from file
        RequestBody requestFile =RequestBody.create(MediaType.parse("multipart/form-data"), image);
        // MultipartBody.Part is used to send also the actual file name


            filePart = MultipartBody.Part.createFormData("licence_image", image.getName(), requestFile);


        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiService.upload_document(header,filePart);



       /* RequestBody mFile = RequestBody.create(MediaType.parse("image*//**//*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photo", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), str_boat_id);*/



        /*String temp = Base64.encodeToString(byteArray, Base64.DEFAULT);
        HashMap<String, String> post = new HashMap<>();
        post.put("boat_id", str_boat_id);
        post.put("photo", temp);*/

        // Call<ResponseBody> call = apiService.add_boat(header, post);
//        Call<ResponseBody> call = apiService.upload(header, requestBodyFile,requestBodyJson);
        //Call<ResponseBody> call = apiService.uploadFile(fileToUpload,filename);

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
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_return_status = response_obj.getString("status");

                        if(str_return_status.equals("1"))
                        {
                            String str_message = response_obj.getString("msg");

                            Alert("Success!!!",str_message);


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
    }



    public void Alert_ask(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                upload_photo();

            }
        });

        mDialog.setNegativeButton(getResources().getString(R.string.Cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }




    public void currecnt_location(Double latitude, Double longitude)
    {
        Geocoder geocoder;
        addresses.clear();
        geocoder = new Geocoder(Booking_Step2.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if(addresses!=null && addresses.size()>0)
            {
               /* Address address1 = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address1.getMaxAddressLineIndex(); i++)
                {
                    sb.append(address1.getAddressLine(i)); //.append("\n");
                }
                sb.append(address1.getLocality()).append("\n");
                sb.append(address1.getPostalCode()).append("\n");
                sb.append(address1.getCountryName());
                String result = sb.toString();

                System.out.println("-----Return Result data------->"+result);*/

                System.out.println("-------all address--------->"+addresses.toString());


                String address = addresses.get(0).getAddressLine(0).replaceAll("\\s",""); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();// Here 1 represent max location result to returned, by documents it recommended 1 to 5

                System.out.println("-------Address------->" + address);


               /* boolean is_store=false;
                ArrayList<String> arrayList_street=new ArrayList<>();
                arrayList_street.clear();

                if(address!=null && !address.equals(""))
                {
                    String[] items = address.split(",");
                    if(items.length>0)
                    {
                        for (String item : items)
                        {
                            if(!item.equalsIgnoreCase(city))
                            {
                                is_store=true;
                                System.out.println("-----Get street address------->" + item);
                                arrayList_street.add(item);
                            }
                            else
                            {
                                System.out.println("-----Get street address---NO---->" + item);
                                if(is_store==false)
                                {
                                    arrayList_street.add(item);
                                }
                                break;
                            }
                        }
                        if(arrayList_street.size()>0)
                        {
                            String addr=arrayList_street.toString();
                            String final_address=(addr.substring(1, addr.length()-1));
                            if(final_address!=null && !final_address.equalsIgnoreCase(""))
                            {
                                edi_street.setText(final_address);
                            }
                            else
                            {
                                edi_street.setText("");
                            }
                        }

                       // System.out.println(loginToken.substring(1, loginToken.length()-1));
                    }
                }*/


                System.out.println("-------city------->" + city);
                System.out.println("-------state------>" + state);
                System.out.println("-------country----->" + country);
                System.out.println("-------postalCode--->" + postalCode);
                System.out.println("-------knownName------->" + knownName);

                if(address!=null && !address.equalsIgnoreCase(""))
                {
                    edi_street.setText(address);
                }
                else
                {
                    edi_street.setText("");
                }

                edit_city.setText(city);
                edit_state.setText(state);
                edit_zip_code.setText(postalCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void dialog_popup()
    {

        dialog = new Dialog(Booking_Step2.this);
        dialog.setContentView(R.layout.sample_test);
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable (true);
        dialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow ().setGravity (Gravity.BOTTOM);
        dialog.show ();
        LinearLayout lin_take_photo = (LinearLayout)dialog.findViewById(R.id.lin_tak_photo);
        LinearLayout lin_chose_photo = (LinearLayout)dialog.findViewById(R.id.lin_choose_photo);
        LinearLayout lin_cancel = (LinearLayout)dialog.findViewById(R.id.lin_cancel);

        lin_chose_photo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23)
                {
                    // Marshmallow+
                    if (!checkAccessFineLocationPermission() || !checkWriteExternalStoragePermission()) {
                        requestCameraPermission();
                    } else {
                        chooseImageFromGallery();
                    }
                } else
                {
                    chooseImageFromGallery();
                }

                dialog.dismiss();
            }});

        lin_take_photo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (!checkAccessFineLocationPermission() || !checkWriteExternalStoragePermission())
                {
                    requestGalleryPermission();
                } else {

                    if (Build.VERSION.SDK_INT >= 21) {
                        try {
                            dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        chooseImageFromCamera();
                    }
                }
                dialog.dismiss();
            }});

        lin_cancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }});

        dialog.show();
    }


    private boolean checkAccessFineLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWriteExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE,  android.Manifest.permission.CAMERA,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALLERY_REQUEST_CODE);
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.CAMERA,  android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        try {
                            dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        chooseImageFromCamera();
                    }
                } else {
                    requestCameraPermission();
                }
                break;

            case PERMISSION_GALLERY_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageFromGallery();
                } else {
                    requestGalleryPermission();
                }
        }
    }

/*
    private void dispatchTakePictureIntent() throws IOException
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null)
            {
                //destination=createImageFile();

                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                System.out.println("--------original--------->"+ myCapturedImage.getAbsolutePath());
                System.out.println("--------duplicate--------->"+photoURI.getPath());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_FLAG);

            }
        }
    }
    */

    private File createImageFile() throws IOException
    {
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        File ranjiht  = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        return ranjiht;
    }




    private void chooseImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(myCapturedImage));
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, CAMERA_REQUEST_FLAG);
    }

    private void chooseImageFromGallery() {
        Intent aIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(aIntent, GALLERY_REQUEST_FLAG);
    }

    private String getRealPathFromURI(Uri mImageCaptureUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(mImageCaptureUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return mImageCaptureUri.getPath();
        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 601)
            {
                String location_name = data.getStringExtra("location_name");
                edi_street.setText(location_name);
                String latitude = data.getStringExtra("latitude");
                String langitude = data.getStringExtra("langitude");

                //*if(latitude!=null && !latitude.equalsIgnoreCase("0.0"))
                {
                    Double d_latitude = Double.parseDouble(latitude);
                    Double d_longitude = Double.parseDouble(langitude);
                    currecnt_location(d_latitude,d_longitude);
                }//*
            }
        }

        if (resultCode == RESULT_OK)
        {
            if (requestCode == CAMERA_REQUEST_FLAG || requestCode == UCrop.REQUEST_CROP)
            {
                try {
                    if (requestCode == CAMERA_REQUEST_FLAG)
                    {


                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;

                        imagePath = destination.getAbsolutePath();
                        mImageCaptureUri = Uri.fromFile(new File(imagePath));
                        Log.e("selectedImagePath>", "" + imagePath);

                        String path = getRealPathFromURI(mImageCaptureUri);
                        File curFile = new File(path);
                        try {
                            ExifInterface exif = new ExifInterface(curFile.getPath());
                            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            int rotationInDegrees = exifToDegrees(rotation);

                            Matrix matrix = new Matrix();
                            if (rotation != 0f) {
                                matrix.preRotate(rotationInDegrees);
                            }
                            //    thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
                        } catch (IOException ex) {
                            Log.e("TAG", "Failed to get Exif data", ex);
                        }

                        Uri picUri = Uri.fromFile(curFile);

                        UCrop.Options Uoptions = new UCrop.Options();
                        Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                        Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));


                        UCrop.of(picUri, picUri)
                                .withAspectRatio(4, 4)
                                .withMaxResultSize(8000, 8000)
                                .withOptions(Uoptions)
                                .start(Booking_Step2.this);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (requestCode == GALLERY_REQUEST_FLAG)
            {

                Uri selectedImage = data.getData();
                if (selectedImage.toString().startsWith("content://com.sec.android.gallery3d.provider"))
                {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    final String picturePath = c.getString(columnIndex);
                    c.close();
                    File curFile = new File(picturePath);

                    Uri picUri = Uri.fromFile(curFile);

                    UCrop.Options Uoptions = new UCrop.Options();
                    Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

                    UCrop.of(picUri, picUri)
                            .withAspectRatio(4, 4)
                            .withOptions(Uoptions)
                            .withMaxResultSize(8000, 8000)
                            .start(Booking_Step2.this);


                } else {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();

                    int columnIndex = c.getColumnIndex(filePath[0]);
                    final String picturePath = c.getString(columnIndex);
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    Bitmap thumbnail = bitmap; //getResizedBitmap(bitmap, 600);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    File curFile = new File(picturePath);

                    try {
                        ExifInterface exif = new ExifInterface(curFile.getPath());
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = exifToDegrees(rotation);

                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotationInDegrees);
                        }
                        if (thumbnail != null) {
                            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
                        }
                    } catch (IOException ex) {
                        Log.e("TAG", "Failed to get Exif data", ex);
                    }
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                    c.close();
                    //---new-----------new image--DIR
                    if (!myImageRoot.exists()) {
                        myImageRoot.mkdir();
                    } else if (!myImageRoot.isDirectory()) {
                        myImageRoot.delete();
                        myImageRoot.mkdir();
                    }

                    final File image = new File(myImageRoot, System.currentTimeMillis() + ".jpg");
                    myOutputURI = Uri.fromFile(image);

                    System.out.println("----image---" + image.getName());

                    UCrop.Options Uoptions = new UCrop.Options();
                    Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                    Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

                    UCrop.of(selectedImage, myOutputURI)
                            .withAspectRatio(4, 4)
                            .withOptions(Uoptions)
                            .withMaxResultSize(8000, 8000)
                            .start(Booking_Step2.this);

                }
            }
            if (requestCode == UCrop.REQUEST_CROP) {

                final Uri resultUri = UCrop.getOutput(data);
                String imgpath = resultUri.getPath();
                System.out.println("=======chan_image_path==========>"+imgpath);
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);


                byte[] byteArray = byteArrayOutputStream.toByteArray();

                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                img_document.setImageBitmap(Bitmap.createScaledBitmap(bmp, img_document.getWidth(),
                        img_document.getHeight(), false));
                destination = new File(getPath(resultUri));
                Alert_ask("Photo Upload", "Are you sure want to upload document?");



               /* Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                profile_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, profile_image.getWidth(),
                        profile_image.getHeight(), false));

                destination = new File(getPath(resultUri));
                Alert_ask("Photo Upload", "Are you sure want to upload photo?");*/

                // Alert_upload(getResources().getString(R.string.upload_photo),imgpath);

            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                System.out.println("========cropError===========" + cropError);
            }

        }


    }
}