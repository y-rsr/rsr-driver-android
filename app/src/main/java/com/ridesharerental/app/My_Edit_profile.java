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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yalantis.ucrop.util.BitmapLoadUtils.exifToDegrees;

public class My_Edit_profile extends Activity implements DatePickerDialog.OnDateSetListener {
    ImageView img_back;
    CircleImageView profile_image;
    EditText edit_lname, edit_name, edit_email, edit_phone;
    TextView txt_dob, txt_expire_date;
    TextView edit_street;
    EditText edit_apartment_number, edit_city, edit_state, edit_zip_code, edit_license, edit_state_another;
    Common_Loader Loader;
    ConnectionDetector cd;
    SessionManager sessionManager;
    RelativeLayout rel_save;

    ImageView img_License;
    String str_profile_Image = "", str_license_image = "";
    String str_first_name = "", str_last_name = "", str_email = "", str_phone_number = "", str_date_of_birth = "", str_APt_no = "", str_street = "",
            str_address = "", str_city = "", str_state = "", str_zip_code = "", str_license_number = "", str_expiration_date = "", str_state_another = "";
    HashMap<String, String> details;


    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE_MAIN_ACTIVITY = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int SELECT_FILE = 200;
    private String filePath = null;
    private File image = null;
    MultipartBody.Part filePart = null;
    Call<ResponseBody> call;
    File destination;
    private static Uri fileUri;
    String user_id = "";

    RelativeLayout rel_documet;
    String sel_imge_type = "";
    LinearLayout linear_back;
    List<Address> addresses = new ArrayList<>();
    HashSet<String> hashSet_address = new HashSet<>();


    //----------------------chan--------------
    private final int PERMISSION_CAMERA_REQUEST_CODE = 230;
    private final int PERMISSION_GALLERY_REQUEST_CODE = 235;
    private static int CAMERA_REQUEST_FLAG = 1021;
    private static int GALLERY_REQUEST_FLAG = 891;
    private Uri mImageCaptureUri, myOutputURI;
    File myCapturedImage, myImageRoot;
    String imgpath = "", myDirectoryNameStr = "", myNameStr = "", imagePath = "";
    private Bitmap selectedBitmap;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my__edit_profile);
        Loader = new Common_Loader(My_Edit_profile.this);
        cd = new ConnectionDetector(My_Edit_profile.this);
        sessionManager = new SessionManager(My_Edit_profile.this);
        details = sessionManager.getUserDetails();

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);


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
        init();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*onBackPressed();
                My_Edit_profile.this.finish();*/

                Intent my_Edit = new Intent(My_Edit_profile.this, Main_homepage.class);
                my_Edit.putExtra("calling_type", "edit_profile");
                startActivity(my_Edit);
                My_Edit_profile.this.finish();
            }
        });

        txt_expire_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpireDatePicker();
            }
        });
        txt_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthDatePicker();
            }
        });
        rel_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Saveprofiledata();
            }
        });

        edit_street.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(My_Edit_profile.this, Search_Google_Places.class);
                startActivityForResult(new_intent, 501);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });


        linear_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* onBackPressed();
                My_Edit_profile.this.finish();*/

                Intent my_Edit = new Intent(My_Edit_profile.this, Main_homepage.class);
                my_Edit.putExtra("calling_type", "edit_profile");
                startActivity(my_Edit);
                My_Edit_profile.this.finish();

               /* Intent returnIntent = new Intent();
                returnIntent.putExtra("first_name",edit_name.getText().toString());
                returnIntent.putExtra("last_name",edit_lname.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                My_Edit_profile.this.finish();*/
            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sel_imge_type = "Profile";
                //selectImage();
                dialog_popup();
            }
        });


        rel_documet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sel_imge_type = "document";
                // selectImage();
                dialog_popup();
            }
        });

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    //------------------------- method to save all date functions
    private void Saveprofiledata() {

        hashSet_address.clear();
        if (edit_name.getText().toString().trim().length() > 0) {
            if (edit_lname.getText().toString().trim().length() > 0) {
                if (edit_phone.getText().toString().trim().length() > 0) {
                    if (txt_dob.getText().toString().trim().length() > 0) {

                        if (edit_street.getText().toString().trim().length() > 0) {


                            if (edit_zip_code.getText().toString().trim().length() > 0) {

                                if (edit_license.getText().toString().trim().length() > 0) {
                                    if (txt_expire_date.getText().toString().trim().length() > 0) {

                                        if (edit_state_another.getText().toString().trim().length() > 0) {
                                            if (cd.isConnectingToInternet()) {

                                                           /* hashSet_address.add(edit_street.getText().toString());
                                                            hashSet_address.add(edit_city.getText().toString());
                                                            hashSet_address.add(edit_state.getText().toString());*/

                                                          /*  hashSet_address.add(edit_state.getText().toString());
                                                            hashSet_address.add(edit_city.getText().toString());
                                                            hashSet_address.add(edit_street.getText().toString());

                                                            System.out.println("-------Hashset Values------->"+hashSet_address.toString());*/

                                                // str_address=edit_street.getText().toString()+","+edit_city.getText().toString()+edit_state.getText().toString();
                                                str_address = edit_street.getText().toString();

                                                hashSet_address.add(str_address);
                                                if (hashSet_address.size() > 0) {
                                                    System.out.println("-------Hashset Values------->" + hashSet_address.toString());
                                                    String loginToken = hashSet_address.toString();
                                                    str_address = (loginToken.substring(1, loginToken.length() - 1));
                                                }
                                                Saveprofile();
                                            } else {
                                                Alert(getResources().getString(R.string.action_no_internet_title), getResources().getString(R.string.action_no_internet_message));
                                            }
                                        } else {
                                            //---------- license expire date empty
                                            snack_bar(getResources().getString(R.string.state_hint), "");
                                        }
                                    } else {
                                        //---------- license expire date empty
                                        snack_bar(getResources().getString(R.string.exp_hint), "");
                                    }
                                } else {
                                    //---------- license empty
                                    snack_bar(getResources().getString(R.string.license_hint), "");
                                }


                            } else {
                                //---------- zipcode empty
                                snack_bar(getResources().getString(R.string.zipcode_hint), "");
                            }


                        } else {
                            //---------- street empty
                            snack_bar(getResources().getString(R.string.street_hint), "");
                        }
                    } else {
                        //---------- dob empty
                        snack_bar(getResources().getString(R.string.don_hint), "");
                    }
                } else {
                    //---------- phone number empty
                    snack_bar(getResources().getString(R.string.enter_phone), "");
                }
            } else {
                //---------- last name empty
                snack_bar(getResources().getString(R.string.enter_second_name), "");
            }
        } else {
            //---------- first name empty
            snack_bar(getResources().getString(R.string.enter_first_name), "");
        }
    }

    private boolean isDeviceSupportCamera() {
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


    //----------------------------------- save  profile service

    private void Saveprofile() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", details.get(sessionManager.KEY_USER_ID));

        HashMap<String, String> map = new HashMap<>();

        try {
            map.put("firstname", edit_name.getText().toString().trim());
            map.put("lastname", edit_lname.getText().toString().trim());
            map.put("gender", "");
            map.put("phone_no", edit_phone.getText().toString().trim());
            map.put("birthday", str_date_of_birth);

            if (edit_apartment_number.getText().toString().trim().length() > 0) {
                map.put("apt_no", edit_apartment_number.getText().toString());
            }

            map.put("address", str_address);
            // map.put("city", edit_city.getText().toString());
            //  map.put("state", edit_state.getText().toString());

            map.put("zip", edit_zip_code.getText().toString().trim());
            map.put("licence_number", edit_license.getText().toString());
            map.put("licence_exp_date", str_expiration_date);

            map.put("licence_state", edit_state_another.getText().toString());


            Set keys = map.keySet();

            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                String value = (String) map.get(key);
                System.out.println("" + key + ":" + value);
            }
        } catch (Exception e) {

        }


        Call<ResponseBody> call = apiService.post_saveProfile(header, map);
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
                    } else {
                        if (response != null && response.body() != null) {
                            String Str_response = response.body().string();
                            Log.e("----Country Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);


                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1")) {
                                Alert(getResources().getString(R.string.action_success), object.getJSONObject("responseArr").getString("msg"));
                            } else {
                                Alert(getResources().getString(R.string.action_opps), object.getJSONObject("responseArr").getString("msg"));
                            }
                            Loader.dismiss();
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


    //------------------------------ date picker

    private void birthDatePicker() {
        Calendar c = Calendar.getInstance();
        //DatePickerDialog dialog = new DatePickerDialog(this,datePickerListener_start,now.get(Calendar.YEAR) - 15, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener_start, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        //dialog.getDatePicker().setMaxDate(new Date().getTime());
        /*long maxdate = 0;
        Date date = null;
        Calendar mac = Calendar.getInstance();
        mac.add(Calendar.DATE, 7);
        date = mac.getTime();
        maxdate = date.getTime();
        dialog.setTitle("");
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.getDatePicker().setMaxDate(maxdate);*/

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener_start = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            txt_dob.setText(String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear));
            str_date_of_birth = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
        }
    };


    private void ExpireDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener_start1, now.get(Calendar.YEAR) - 15, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        // dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener_start1 = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            txt_expire_date.setText(String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear));
            str_expiration_date = String.valueOf(selectedMonth + 1) + "/" + String.valueOf(selectedDay) + "/" + String.valueOf(selectedYear);
        }

    };

    //------------------- date conversion for set text
    private String Datecoversion(String s) {
        String value = "";
        SimpleDateFormat form_checkin = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        try {
            Date date_check = form_checkin.parse(s);
            SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            value = postFormater.format(date_check);
            System.out.println("==value===" + value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    public void init() {
        linear_back = (LinearLayout) findViewById(R.id.linear_back);

        img_License = (ImageView) findViewById(R.id.img_document);
        rel_save = (RelativeLayout) findViewById(R.id.rel_save);
        img_back = (ImageView) findViewById(R.id.img_back_arrow);
        profile_image = (CircleImageView) findViewById(R.id.my_profile_layout_imageview);

        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_lname = (EditText) findViewById(R.id.edit_last_name);

        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        txt_dob = (TextView) findViewById(R.id.my_editprofile_dob);
        txt_expire_date = (TextView) findViewById(R.id.my_editprofile_expdate);
        edit_apartment_number = (EditText) findViewById(R.id.edit_apartment_number);
        edit_street = (TextView) findViewById(R.id.edit_street);
        edit_city = (EditText) findViewById(R.id.edit_city);
        edit_state = (EditText) findViewById(R.id.edit_state);
        edit_zip_code = (EditText) findViewById(R.id.edit_zip_code);
        edit_license = (EditText) findViewById(R.id.edot_license_number);
        edit_state_another = (EditText) findViewById(R.id.edit_state_1);

        rel_documet = (RelativeLayout) findViewById(R.id.rel_docuemnt_upload);

        if (cd.isConnectingToInternet()) {
            Load_Data();
        } else {
            Alert(getResources().getString(R.string.action_no_internet_title), getResources().getString(R.string.action_no_internet_message));
        }


    }


    //----------------------------------- view profile service

    public void Load_Data() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", details.get(sessionManager.KEY_USER_ID));
        System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
        Call<ResponseBody> call = apiService.show_driver_profile(header);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        Loader.dismiss();
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {


                        if (response != null && response.body() != null) {
                            String Str_response = response.body().string();
                            Log.e("----Country Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            JSONObject response_obj = object.getJSONObject("responseArr");
                            JSONObject common_obj = object.getJSONObject("commonArr");
                            String status_code = response_obj.getString("status");
                            if (status_code != null && !status_code.equals("") && status_code.equals("1")) {
                                JSONObject driver_detail_obj = response_obj.getJSONObject("driverDetails");


                                str_first_name = driver_detail_obj.getString("firstname");
                                str_last_name = driver_detail_obj.getString("lastname");
                                str_profile_Image = driver_detail_obj.getString("profile_pic");
                                str_phone_number = driver_detail_obj.getString("phone_no");
                                str_APt_no = driver_detail_obj.getString("apt_no");

                                str_license_image = driver_detail_obj.getString("licence_image");

                                edit_street.setText(driver_detail_obj.getString("address"));
                                str_date_of_birth = Datecoversion(driver_detail_obj.getString("birthday"));
                                str_city = driver_detail_obj.getString("city");
                                str_zip_code = driver_detail_obj.getString("zip");
                                str_license_number = driver_detail_obj.getString("licence_number");
                                str_expiration_date = Datecoversion(driver_detail_obj.getString("licence_exp_date"));
                                str_state = driver_detail_obj.getString("state");

                                str_state_another = driver_detail_obj.getString("licence_state");

                                str_email = common_obj.getString("email");
                            }

                            //--------------------------------------------  function
                            edit_name.setText(str_first_name);
                            edit_lname.setText(str_last_name);
                            edit_email.setText(str_email);
                            edit_phone.setText(str_phone_number);
                            txt_dob.setText(str_date_of_birth);

                            edit_apartment_number.setText(str_APt_no);
                            edit_city.setText(str_city);
                            edit_state.setText(str_state);
                            edit_zip_code.setText(str_zip_code);
                            edit_license.setText(str_license_number);
                            txt_expire_date.setText(str_expiration_date);

                            edit_state_another.setText(str_state_another);

                            if (str_profile_Image != null && !str_profile_Image.equals("")) {
                                Picasso.with(My_Edit_profile.this)
                                        .load(str_profile_Image)
                                        .placeholder(getResources().getDrawable(R.drawable.icn_profile))
                                        .into(profile_image);
                            }
                            if (str_license_image != null && !str_license_image.equals("")) {
                                Picasso.with(My_Edit_profile.this)
                                        .load(str_license_image)
                                        .placeholder(getResources().getDrawable(R.drawable.upload_new))
                                        .into(img_License);
                            }
                            Loader.dismiss();
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

    //------------------------snack bar---------------
    private void snack_bar(String title, String message) {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.LENGTH_SHORT);
        // snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }


    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(this);
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


    public void Alert_ask(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                upload_photo();

            }
        });

        mDialog.setNegativeButton(getResources().getString(R.string.Cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(My_Edit_profile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    //----------------------------7.0 camera intent actions-----------------
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
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


    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }





    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
        if (requestCode == 501)
        {
            String location_name = data.getStringExtra("location_name");
            edit_street.setText(location_name);
            String latitude = data.getStringExtra("latitude");
            String langitude = data.getStringExtra("langitude");

            if(latitude!=null && !latitude.equalsIgnoreCase("0.0"))
            {
                Double d_latitude = Double.parseDouble(latitude);
                Double d_longitude = Double.parseDouble(langitude);
                currecnt_location(d_latitude,d_longitude);
            }
        }
        }

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE || requestCode == UCrop.REQUEST_CROP)
        {






            if (resultCode == RESULT_OK)
            {
                if (destination != null)
                {
                    filePath = destination.getAbsolutePath();
                    // mImageCaptureUri = Uri.fromFile(new File(filePath));
                    // Log.e("selectedImagePath------->", "" + filePath);



                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;

                   Uri mImageCaptureUri = Uri.fromFile(new File(filePath));
                    String path = getRealPathFromURI(mImageCaptureUri);
                    Log.e("selectedImagePath>", "" + path);
                    File curFile = new File(path);
                    try {
                        ExifInterface exif = new ExifInterface(curFile.getPath());
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int rotationInDegrees = exifToDegrees(rotation);

                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(rotationInDegrees);
                        }
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
                            .withMaxResultSize(5000, 5000)
                            .withOptions(Uoptions)
                            .start(com.ridesharerental.app.My_Edit_profile.this);

                    *//*
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
                            if(sel_imge_type.equals("Profile"))
                            {
                                profile_image.setImageBitmap(bmp);
                                Alert_ask("Photo Upload", "Are you sure want to upload photo?");
                            }
                            else
                            {
                                img_License.setImageBitmap(bmp);
                                Alert_ask("Photo Upload", "Are you sure want to upload document?");
                            }

                        }
                        else
                        {
                            Alert("Alert","Upload Image size should be greater than 1000 * 700 Px");
                        }

                    }*//*
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
                        CursorLoader loader = new CursorLoader(My_Edit_profile.this, urr, proj, null, null, null);
                        Cursor cursor1 = loader.loadInBackground();
                        int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor1.moveToFirst();
                        filePath= cursor1.getString(column_index);
                        System.out.println("--------Resultss----->"+filePath);
                        File f = new File(filePath);
                        destination=f;
                        cursor1.close();

                        Uri picUri = Uri.fromFile(f);
                        UCrop.Options Uoptions = new UCrop.Options();
                        Uoptions.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        Uoptions.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                        Uoptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

                        UCrop.of(picUri, picUri)
                                .withAspectRatio(4, 4)
                                .withMaxResultSize(8000, 8000)
                                .withOptions(Uoptions)
                                .start(com.ridesharerental.app.My_Edit_profile.this);

                        *//*int image_width = bitmap.getWidth();
                        int image_hight = bitmap.getHeight();
                        if (image_width >= 1000 && image_hight >= 700)
                        {

                            if(sel_imge_type.equals("Profile"))
                            {
                                profile_image.setImageBitmap(bitmap);
                                Alert_ask("Photo Upload", "Are you sure want to upload photo?");
                            }
                            else
                            {
                                img_License.setImageBitmap(bitmap);
                                Alert_ask("Photo Upload", "Are you sure want to upload document?");
                            }
                        }
                        else
                        {
                            Alert("Alert","Upload Image size should be greater than 1000 * 700 Px");
                        }*//*
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //onSelectFromGalleryResult(data);
            }
        }

        if (requestCode == UCrop.REQUEST_CROP)
        {

            final Uri resultUri = UCrop.getOutput(data);
            System.out.println("---cropped img-URI-------------srt-" + resultUri);

            try {
                mySelectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                System.out.println("---cropped img---bmp-----------srt-" + mySelectedBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
              mySelectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

            destination = new File(getPath(resultUri));
            Alert_ask("Photo Upload", "Are you sure want to upload photo?");
           // Alert_Upload(getResources().getString(R.string.upload_photo));


        } else if (resultCode == UCrop.RESULT_ERROR)
        {
            final Throwable cropError = UCrop.getError(data);
            System.out.println("======== cropError===========" + cropError);
           // Toast.makeText(TrustAndVerificationActivity.this, getResources().getString(R.string.product_crop_failed_error), Toast.LENGTH_SHORT).show();
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


    public void upload_photo() {
        Loader.show();
        System.out.println("--------file path Image------>" + filePath);

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Bitmap b = BitmapFactory.decodeFile(destination.getAbsolutePath());
        Bitmap out = Bitmap.createScaledBitmap(b, 600, 400, false);
        File file = new File(dir, System.currentTimeMillis()+"_resize.jpg");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            out.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            b.recycle();
            out.recycle();
        } catch (Exception e) {
        }

        image = new File(file.getAbsolutePath());
        // add another part within the multipart request
        RequestBody requestName = RequestBody.create(MediaType.parse("multipart/form-data"), image.getName());

        //  RequestBody requestBoatId =RequestBody.create(MediaType.parse("multipart/form-data"), str_boat_id);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        // MultipartBody.Part is used to send also the actual file name

        if (sel_imge_type.equals("Profile")) {
            filePart = MultipartBody.Part.createFormData("profile_image", image.getName(), requestFile);
        } else {
            filePart = MultipartBody.Part.createFormData("licence_image", image.getName(), requestFile);
        }

        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        if (sel_imge_type.equals("Profile")) {

            call = apiService.uploadImage(header, filePart);
        } else {
            call = apiService.uploadprofile_document(header, filePart);
        }




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
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_return_status = response_obj.getString("status");

                        if (str_return_status.equals("1")) {
                            String str_message = response_obj.getString("msg");
                            Alert("Success!!!", str_message);
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


    public void currecnt_location(Double latitude, Double longitude) {
        Geocoder geocoder;
        addresses.clear();
        geocoder = new Geocoder(My_Edit_profile.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

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


            /*boolean is_store=false;
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
                            edit_street.setText(final_address);
                        }
                        else
                        {
                            edit_street.setText("");
                        }
                    }

                    // System.out.println(loginToken.substring(1, loginToken.length()-1));
                }
            }*/


            if (address != null && !address.equalsIgnoreCase("")) {
                edit_street.setText(address);
            } else {
                edit_street.setText("");
            }

            edit_city.setText(city);
            edit_state.setText(state);
            edit_zip_code.setText(postalCode);


        } catch (IOException e) {
            e.printStackTrace();
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


    private void dialog_popup() {

        dialog = new Dialog(My_Edit_profile.this);
        dialog.setContentView(R.layout.sample_test);
        // dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        LinearLayout lin_take_photo = (LinearLayout) dialog.findViewById(R.id.lin_tak_photo);
        LinearLayout lin_chose_photo = (LinearLayout) dialog.findViewById(R.id.lin_choose_photo);
        LinearLayout lin_cancel = (LinearLayout) dialog.findViewById(R.id.lin_cancel);

        lin_chose_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    if (!checkAccessFineLocationPermission() || !checkWriteExternalStoragePermission()) {
                        requestCameraPermission();
                    } else {
                        chooseImageFromGallery();
                    }
                } else {
                    chooseImageFromGallery();
                }

                dialog.dismiss();
            }
        });

        lin_take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!checkAccessFineLocationPermission() || !checkWriteExternalStoragePermission()) {
                    requestGalleryPermission();
                } else {

                    if (Build.VERSION.SDK_INT >= 21) {
                        try {
                            dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        chooseImageFromCamera();
                    }
                }
                dialog.dismiss();
            }
        });

        lin_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALLERY_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CAMERA_REQUEST_CODE);

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
                    } else {
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


    private void dispatchTakePictureIntent() throws IOException {
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
            if (photoFile != null) {
                //destination=createImageFile();

                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                System.out.println("--------original--------->" + myCapturedImage.getAbsolutePath());
                System.out.println("--------duplicate--------->" + photoURI.getPath());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_FLAG);

            }
        }
    }

    private File createImageFile() throws IOException {
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        File ranjiht = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 501) {
                String location_name = data.getStringExtra("location_name");
                edit_street.setText(location_name);
                String latitude = data.getStringExtra("latitude");
                String langitude = data.getStringExtra("langitude");

                if (latitude != null && !latitude.equalsIgnoreCase("0.0")) {
                    Double d_latitude = Double.parseDouble(latitude);
                    Double d_longitude = Double.parseDouble(langitude);
                    currecnt_location(d_latitude, d_longitude);
                }
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_FLAG || requestCode == UCrop.REQUEST_CROP) {
                try {
                    if (requestCode == CAMERA_REQUEST_FLAG) {
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
                                .start(My_Edit_profile.this);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == GALLERY_REQUEST_FLAG) {

                Uri selectedImage = data.getData();
                if (selectedImage.toString().startsWith("content://com.sec.android.gallery3d.provider")) {
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
                            .start(My_Edit_profile.this);


                }
                else if (selectedImage.toString().startsWith("content://com.google.android.apps.photos.content")){
                    try {
                        InputStream is = getContentResolver().openInputStream(selectedImage);
                        if (is != null) {
                            Bitmap pictureBitmap = BitmapFactory.decodeStream(is);
                            String realPath = getRealPath(My_Edit_profile.this, getImageUri(My_Edit_profile.this, pictureBitmap));
                            Uri selectedImageUri = Uri.fromFile(new File(realPath));

                            Bitmap bitmap = BitmapFactory.decodeFile(realPath);
                            Bitmap thumbnail = bitmap; //getResizedBitmap(bitmap, 600);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            File curFile = new File(realPath);

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

                            thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

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

                            UCrop.of(selectedImageUri, myOutputURI)
                                    .withAspectRatio(4, 4)
                                    .withOptions(Uoptions)
                                    .withMaxResultSize(8000, 8000)
                                    .start(My_Edit_profile.this);
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }catch (IOException ex) {
                    Log.e("TAG", "Failed to get Exif data", ex);
                }

                }
                else {
                    try {
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();

                    int columnIndex = c.getColumnIndex(filePath[0]);
                    final String picturePath = c.getString(columnIndex);
                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    Bitmap thumbnail = bitmap; //getResizedBitmap(bitmap, 600);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    File curFile = new File(picturePath);


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
                            .start(My_Edit_profile.this);
                    } catch (IOException ex) {
                        Log.e("TAG", "Failed to get Exif data", ex);
                    }catch (Exception ex) {
                        Log.e("TAG", "Failed to get Exif data", ex);
                    }
                }
            }
            if (requestCode == UCrop.REQUEST_CROP) {

                final Uri resultUri = UCrop.getOutput(data);
                String imgpath = resultUri.getPath();
                System.out.println("=======image_path==========>" + imgpath);
                try {
                    selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);


                byte[] byteArray = byteArrayOutputStream.toByteArray();


                if (sel_imge_type.equals("Profile")) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    profile_image.setImageBitmap(Bitmap.createScaledBitmap(bmp, profile_image.getWidth(),
                            profile_image.getHeight(), false));
                    destination = new File(getPath(resultUri));
                    Alert_ask("Photo Upload", "Are you sure want to upload photo?");
                } else {
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    img_License.setImageBitmap(Bitmap.createScaledBitmap(bmp, img_License.getWidth(),
                            img_License.getHeight(), false));
                    destination = new File(getPath(resultUri));
                    Alert_ask("Photo Upload", "Are you sure want to upload document?");
                }


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
    public String getRealPath(Activity activity,Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.MediaColumns.DATA};
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return "";
    }
    public Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "img", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            Intent my_Edit = new Intent(My_Edit_profile.this, Main_homepage.class);
            my_Edit.putExtra("calling_type", "edit_profile");
            startActivity(my_Edit);
            My_Edit_profile.this.finish();
            return true;
        }
        return false;
    }


}
