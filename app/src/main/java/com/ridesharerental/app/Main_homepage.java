package com.ridesharerental.app;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.countrycodepicker.CountryPicker;
import com.countrycodepicker.CountryPickerListener;
import com.devspark.appmsg.AppMsg;
import com.ridesharerental.adapter.Menu_Adapter;
import com.ridesharerental.facebook.Util;
import com.ridesharerental.fragments.HomeMessage;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.utils.UpdateLocationService;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.williamww.silkysignature.views.SignaturePad;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main_homepage extends AppCompatActivity implements Uread_count

{

    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;

    SessionManager sessionManager;
    Common_Loader Loader;
    HashMap<String, String> details;
    ListView list_item;
    ArrayList<String> arrayList_items = new ArrayList<>();
    DrawerLayout drawer;
    RelativeLayout navigationView;
    Menu_Adapter drawer_Adapter;

    ActionBar actionBar;
    ColorDrawable colorDrawable = new ColorDrawable();
    Toolbar toolbar;
    TextView tool_bar_title;

    //------------------fragment classes--------------
    Fragment fragment_dashboard = new DashBoardFragment();
    Fragment fragment_find_cars = new Find_Cars();
    Fragment fragment_reservations = new My_Reservation_New();
    Fragment fragment_transactions = new My_Transactions();
    Fragment fragment_my_profile = new My_Profile();
    Fragment fragment_signature = new AddSignature();
    Fragment fragment_inbox = new HomeMessage();
    //Fragment fragment_wish_list = new Wish_List();
    Fragment fragment_add_credit_card = new Add_credit_card();
    Fragment fragment_change_password = new Change_Password();
    Fragment fragment_ambasador_program = new Ambasador_New_Program();
    Fragment fragment_contract_history = new Contract_History();
    Fragment fragment_invite_friends = new Invite_Friends();
    Fragment fragment_contact_us = new Contact_Us();
    Fragment fragment_leave_review = new LeaveReview();
    Fragment fragment_driver_claim = new DriverClaimPage();

    ActionBarDrawerToggle toggle;
    static String Preference_name = "";
    String str_calling_Type = "";
    String str_get_address = "", str_get_car_make = "", str_get_model = "";
    String str_active_reservations = "";
    String str_un_read_count = "";


    String user_id = "", str_message_count = "";
    Common_Loader loader;

    public static int exit = 0;

    String notification_key = "";

    int is_signed = 0;
    AlertDialog alertDialog = null;
    MultipartBody.Part filePart = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_homepage);
        loader = new Common_Loader(Main_homepage.this);
        cd = new ConnectionDetector(Main_homepage.this);
        isInternetPresent = cd.isConnectingToInternet();

        Loader = new Common_Loader(Main_homepage.this);

        sessionManager = new SessionManager(Main_homepage.this);
        details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        str_message_count = details.get(sessionManager.KEY_MESSAGE_COUNT);

        //load_profile_data();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            str_calling_Type = bundle.getString("calling_type");
            str_get_address = bundle.getString("address");
            str_get_car_make = bundle.getString("car_make");
            str_get_model = bundle.getString("car_model");
            str_un_read_count = bundle.getString("unread_count");
            // notification_key=bundle.getString("key");
            // str_active_reservations=bundle.getString("active_reservation");

        }
        init();
        toolbar.setVisibility(View.VISIBLE);


        // HashMap<String, String> details = sessionManager.getUserDetails();
        // Preference_name = details.get(sessionManager.P);


        /*actionBar = getSupportActionBar();
        colorDrawable.setColor(getResources().getColor(R.color.orange_color));
        // colorDrawable.setColor(0xFFFF6666);
         actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("");*/


        //TextView tv = getToolBarTextView();
        toggle = new ActionBarDrawerToggle
                (
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (str_calling_Type != null && !str_calling_Type.equals("")) {
            if (str_calling_Type.equalsIgnoreCase("reservations")) {
                System.out.println("-------Active Reservations-----yes---->" + str_calling_Type);
                Main_items(2);
            } else if (str_calling_Type.equalsIgnoreCase("Yes")) {
                System.out.println("-------Active Reservations-----yes---->" + str_calling_Type);
                Main_items(2);
            } else if (str_calling_Type.equalsIgnoreCase("No")) {
                System.out.println("-------Active Reservations-----No---->" + str_calling_Type);
                Main_items(1);
            }
            if (str_calling_Type.equals("inbox")) {
                Main_items(4);
            } else if (str_calling_Type.equals("edit_profile")) {
                Main_items(5);
            } else if (str_calling_Type.equals("transaction")) {
                Main_items(3);
            } else if (str_calling_Type.equals("findcar")) {
                Main_items(1);
            } else if (str_calling_Type.equals("reservation")) {
                Main_items(2);
            } else if (str_calling_Type.equals("signature")) {
                Main_items(1);
            } else if (str_calling_Type.equals("to_signature")) {
                Main_items(6);
            }
        } else {
            Main_items(1);
        }

    }


    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tool_bar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        list_item = (ListView) findViewById(R.id.list_items);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (RelativeLayout) findViewById(R.id.nav_view);
        list_item.setOnItemClickListener(new DrawerItemClickListener());

        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        tool_bar_title.setText("");
        if(str_calling_Type.equalsIgnoreCase(""))
            load_profile_data();

        arrayList_items.clear();
        arrayList_items.add("Dashboard");
        arrayList_items.add("Find Cars");
        arrayList_items.add("My Reservations");
        arrayList_items.add("My Transactions");
        if (str_message_count != null && !str_message_count.equals("") && !str_message_count.equals("0")) {
            arrayList_items.add("Message" + "(" + str_message_count + ")");
        } else {
            arrayList_items.add("Message");
        }
        arrayList_items.add("My Profile");
        arrayList_items.add("Add/Update Signature");

        arrayList_items.add("Add Credit Card");
        arrayList_items.add("Change Password");
        arrayList_items.add("Ambassador Referral Program");
        arrayList_items.add("Contract History");
        arrayList_items.add("Contact Support");
        //arrayList_items.add("WishList");
        arrayList_items.add("Leave Review");
        arrayList_items.add("Report An Accident");
        arrayList_items.add("Logout");

        drawer_Adapter = new Menu_Adapter(Main_homepage.this, arrayList_items);
        list_item.setAdapter(drawer_Adapter);
        drawer_Adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void aMethod(String str_count) {
        System.out.println("------Interface Data------>" + str_count);

        if (str_count != null && !str_count.equals("") && !str_count.equals("0")) {
            arrayList_items.clear();
            arrayList_items.add("Dashboard");
            arrayList_items.add("Find Cars");
            arrayList_items.add("My Reservations");
            arrayList_items.add("My Transactions");
            if (str_count != null && !str_count.equals("") && !str_count.equals("0")) {
                arrayList_items.add("Message" + "(" + str_count + ")");
            } else {
                arrayList_items.add("Message");
            }
            arrayList_items.add("My Profile");
            arrayList_items.add("Add/Update Signature");
            arrayList_items.add("Add Credit Card");
            arrayList_items.add("Change Password");
            arrayList_items.add("Ambassador Referral Program");
            arrayList_items.add("Contract History");
            arrayList_items.add("Contact Support");
           // arrayList_items.add("WishList");
            arrayList_items.add("Leave Review");
            arrayList_items.add("Report An Accident");
            arrayList_items.add("Logout");
            if (drawer_Adapter != null) {
                drawer_Adapter = new Menu_Adapter(Main_homepage.this, arrayList_items);
                list_item.setAdapter(drawer_Adapter);
                drawer_Adapter.notifyDataSetChanged();
            }
            //tool_bar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
           // tool_bar_title.setText(arrayList_items.get(4).toString());


        } else {
            arrayList_items.clear();
            arrayList_items.add("Dashboard");
            arrayList_items.add("Find Cars");
            arrayList_items.add("My Reservations");
            arrayList_items.add("My Transactions");
            arrayList_items.add("Message");
            arrayList_items.add("My Profile");
            arrayList_items.add("Add/Update Signature");
            arrayList_items.add("Add Credit Card");
            arrayList_items.add("Change Password");
            arrayList_items.add("Ambassador Referral Program");
            arrayList_items.add("Contract History");
            arrayList_items.add("Contact Support");
            //arrayList_items.add("WishList");
            arrayList_items.add("Leave Review");
            arrayList_items.add("Report An Accident");
            arrayList_items.add("Logout");
            if (drawer_Adapter != null) {
                drawer_Adapter = new Menu_Adapter(Main_homepage.this, arrayList_items);
                list_item.setAdapter(drawer_Adapter);
                drawer_Adapter.notifyDataSetChanged();
            }
      //      tool_bar_title.setText(arrayList_items.get(4).toString());

        }


        //  str_message_count=str_count;


    }


    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Main_items(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fragment_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    private void Main_items(int positions) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (positions) {

            case 0:

                ft.replace(R.id.content_frame, fragment_dashboard);

                break;



            case 1:

                Bundle arguments = new Bundle();
                if (str_get_address != null && !str_get_address.equals("") && str_get_car_make != null
                        && !str_get_car_make.equals("") && str_get_model != null && !str_get_model.equals("")) {
                    arguments.putString("address", str_get_address);
                    arguments.putString("car_make", str_get_car_make);
                    arguments.putString("car_model", str_get_model);
                    fragment_find_cars.setArguments(arguments);
                }

                ft.replace(R.id.content_frame, fragment_find_cars);

                break;
            case 2:

                ft.replace(R.id.content_frame, fragment_reservations);

                break;

            case 3:

                ft.replace(R.id.content_frame, fragment_transactions);

                break;

            case 4:

                ft.replace(R.id.content_frame, fragment_inbox);
//                Intent i = new Intent(Main_homepage.this, MessageActivity.class);
//                startActivity(i);
//                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case 5:

                ft.replace(R.id.content_frame, fragment_my_profile);

                break;


            case 6:

                ft.replace(R.id.content_frame, fragment_signature);

                break;




            /*case 5:


                //-----------------wishlist
                ft.replace(R.id.content_frame, fragment_wish_list);

                break;*/

            case 7:

                ft.replace(R.id.content_frame, fragment_add_credit_card);

                break;


            case 8:

                ft.replace(R.id.content_frame, fragment_change_password);

                break;

            case 9:

                System.out.println("------------Invite friends------------");
                ft.replace(R.id.content_frame, fragment_ambasador_program);
                break;


            case 10:

                System.out.println("---------Contract History---------");
                ft.replace(R.id.content_frame, fragment_contract_history);

                break;


            case 11:

                System.out.println("---------Contact Us---------");
                ft.replace(R.id.content_frame, fragment_contact_us);
                break;

//
//            case 12:
//
//
//                ft.replace(R.id.content_frame, fragment_wish_list);
//                break;

            case 12:


                ft.replace(R.id.content_frame, fragment_leave_review);
                break;


            case 13:


//                ft.replace(R.id.content_frame, fragment_driver_claim);

                Intent in = new Intent(Main_homepage.this, DriverClaimActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);

                break;


            case 14:

                System.out.println("---------Logout---------");
                //alartmsg_logout();
                Alert(getResources().getString(R.string.app_name), getString(R.string.Are_you_logout));
                break;

        }
        ft.commit();
        list_item.setItemChecked(positions, true);
        toolbar.setTitle("");
        if (positions == 1) {
            tool_bar_title.setText("Find a Car");
        } else{
            if(positions!= 14)
                tool_bar_title.setText(arrayList_items.get(positions).toString());
        }

        //drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(navigationView);

    }


   /* private void setupDrawer()
    {
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // getSupportActionBar().setTitle("Navigation!");
                // actionBar.setIcon(R.drawable.ic_drawer);
                // invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()

                supportInvalidateOptionsMenu();
            }

            */

    /**
     * Called when a drawer has settled in a completely closed state.
     *//*
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // getSupportActionBar().setTitle(mActivityTitle);
                // actionBar.setIcon(R.drawable.ic_drawer);
                // invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
                supportInvalidateOptionsMenu();
            }
        };
        final Drawable upArrow = getResources().getDrawable(
                R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(
                getResources().getColor(R.color.white_color),
                PorterDuff.Mode.SRC_ATOP);
        actionBar.setHomeAsUpIndicator(upArrow);

        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

    }


    private void alartmsg_logout() {
        AlertDialog dialog = new AlertDialog.Builder(Main_homepage.this)
                .setMessage(getString(R.string.Are_you_logout))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        logoutFromFacebook();

                        sessionManager.session_logout();

                        Intent int_settingg = new Intent(
                                Main_homepage.this, Slider_page.class);
                        int_settingg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(int_settingg);
                        // Finish.Main_Finish();
                        Main_homepage.this.finish();
                        Main_homepage.this.overridePendingTransition(
                                R.anim.activity_in, R.anim.activity_out);
                        load_logout(Main_homepage.this);

                    }
                })
                .setNegativeButton(getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        }).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sofia Pro Regular.ttf");
        textView.setTypeface(face);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        nbutton.setTypeface(face);
        pbutton.setTypeface(face);
    }


    public void logoutFromFacebook() {
        //LoginManager.getInstance().logOut();
        Util.clearCookies(Main_homepage.this);
        //Facebook fb;
        // facebook.logout(Signin_Page_New.this);
        SharedPreferences.Editor editor = getSharedPreferences("CASPreferences", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            //alartExit();
            if(exit==0)
                Alert_exit(getResources().getString(R.string.app_name), getString(R.string.Are_you_Exit));
            return true;
        }
        return false;
    }


    private void alartExit() {
        AlertDialog dialog = new AlertDialog.Builder(Main_homepage.this)
                .setMessage(getString(R.string.Are_you_Exit))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sofia Pro Regular.ttf");
        textView.setTypeface(face);
        nbutton.setTypeface(face);
        pbutton.setTypeface(face);
    }


    public void load_profile_data() {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("commonId", user_id);
        Call<ResponseBody> call = apiService.show_driver_profile(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    loader.dismiss();
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
                            JSONObject common_Obj = object.getJSONObject("commonArr");
                            String str_profile_image = common_Obj.getString("profile_pic");
                            sessionManager.set_profile(str_profile_image);

                            String str_msg_coutn = common_Obj.getString("unread_message_count");
                            System.out.println("------Seesiion Count----------->" + str_msg_coutn);
                            str_message_count = str_msg_coutn;
                            sessionManager.set_msg_count(str_msg_coutn);
                            if(!common_Obj.getString("ph_verified").equalsIgnoreCase("Yes"))
                            {// here showing the alert if the user has not verification by phone.
                                AlertMobileVerify(getResources().getString(R.string.app_name), getString(R.string.verify_mobile));
                            }else  if(common_Obj.getString("signature_image").equalsIgnoreCase("") || common_Obj.getString("signature_image").length()==0 || !common_Obj.getString("signature_image").contains("http"))
                            {// here showing the alert if the user has not add their signature.
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
                loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }


    public void load_logout(Context ctx) {
        final Common_Loader loader_new = new Common_Loader(ctx);
        loader_new.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("device", "android");
        Call<ResponseBody> call = apiService.send_logout(map);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    loader_new.dismiss();
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader_new.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });

        loader_new.dismiss();
    }


    private void Alert(String title, String alert) {
        try {
            final PkDialog mDialog = new PkDialog(Main_homepage.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    logoutFromFacebook();

                    sessionManager.session_logout();

                    Intent int_settingg = new Intent(
                            Main_homepage.this, Slider_page.class);
                    int_settingg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(int_settingg);
                    // Finish.Main_Finish();
                    Main_homepage.this.finish();
                    Main_homepage.this.overridePendingTransition(
                            R.anim.activity_in, R.anim.activity_out);
                    load_logout(Main_homepage.this);

                }
            });
            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AlertSuccess(String title, String alert) {
        try {
            final PkDialog mDialog = new PkDialog(Main_homepage.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();

                }
            });
            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void AlertSignature(String title, String alert) {
        try {
            final PkDialog mDialog = new PkDialog(Main_homepage.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();

                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void AlertSignatureVerify(String title, String alert) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Main_homepage.this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_signature, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);




        final SignaturePad mSignaturePad = (SignaturePad) dialogView.findViewById(R.id.signature_pad);
        final TextView txt_signCancel = (TextView) dialogView.findViewById(R.id.txt_signCancel);
        final Button close_btn_signature_dialog = (Button) dialogView.findViewById(R.id.close_btn_signature_dialog);

        final TextView txt_signOK = (TextView) dialogView.findViewById(R.id.txt_signOK);

        alertDialog = dialogBuilder.create();
        close_btn_signature_dialog.setVisibility(View.VISIBLE);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
                txt_signOK.setEnabled(true);
                txt_signCancel.setEnabled(true);
                is_signed = 1;// here assign the value as 1 if the user signed on pad

            }

            @Override
            public void onClear() {
                txt_signOK.setEnabled(true);
                txt_signCancel.setEnabled(true);
                is_signed = 0;// here assign the value as 0 if the user signed on pad to alert the user
            }
        });
        txt_signCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignaturePad.clear();
                is_signed = 0;
            }
        });

        close_btn_signature_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertDialog.dismiss();
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
                        AlertSignature(getString(R.string.action_opps),getString(R.string.alert_signature));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        int height = (int)(getResources().getDisplayMetrics().heightPixels);
        int width = (int)(getResources().getDisplayMetrics().widthPixels);

        alertDialog.getWindow().setLayout(width, height);

        alertDialog.show();
//        try {
//            final PkDialog mDialog = new PkDialog(Main_homepage.this);
//            mDialog.setDialogTitle(title);
//            mDialog.setDialogMessage(alert);
//            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDialog.dismiss();
//                    Main_items(6);
//
//
//                }
//            });
//            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mDialog.dismiss();
//                }
//            });
//            mDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    private void AlertMobileVerify(String title, String alert) {
        try {
            final PkDialog mDialog = new PkDialog(Main_homepage.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    showMobileVerify();


                }
            });
            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        header.put("commonId", user_id);


        Call<ResponseBody> call = apiService.upload_driver_sign(header, filePart);
        System.out.println("-----------upload_driver_sign url------>" + call.request().url().toString());



        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    alertDialog.dismiss();
                    AlertSignature(getString(R.string.action_success),getString(R.string.alert_signature_success));
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
    private void showMobileVerify() {
        AlertDialog alertDialog_showVerify = null;
        final CountryPicker myPicker = CountryPicker.newInstance("Select Country");
        final AlertDialog.Builder builder = new AlertDialog.Builder(Main_homepage.this);

        // Get custom login form view.
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_mobile_verification, null);
        // Set above view in alert dialog.
        builder.setView(dialogView);

        // Register button click listener.
        final RelativeLayout rel_send = (RelativeLayout)dialogView.findViewById(R.id.rel_send);
        final RelativeLayout rel_close = (RelativeLayout)dialogView.findViewById(R.id.rel_close);
        final EditText edit_mobile_number = (EditText)dialogView.findViewById(R.id.edit_mobile_number);
        final TextView txt_country_code = (TextView)dialogView.findViewById(R.id.txt_country_code);
        myPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int aFlag) {
                myPicker.dismiss();
                txt_country_code.setText(dialCode);

            }
        });
        alertDialog_showVerify = builder.create();
        final AlertDialog finalAlertDialog = alertDialog_showVerify;

        rel_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Close Alert Dialog.
                    finalAlertDialog.cancel();
                   // textViewTmp.setText("Register success.");
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


        rel_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_mobile_number.getText().toString().isEmpty())
                {
                    snack_bar(getResources().getString(R.string.enter_phone),"");
                }else {
                    finalAlertDialog.cancel();
                    sendVerification(txt_country_code.getText().toString(),edit_mobile_number.getText().toString());

                }
            }
        });

        txt_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });
        // Login button click listener.


        // Reset button click listener.


        builder.setCancelable(true);

        alertDialog_showVerify.show();
    }

    private void sendVerification(String str_c_code,String str_ph_num) {



            Loader.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            HashMap<String, String> header = new HashMap<>();
            header.put("commonId", details.get(sessionManager.KEY_USER_ID));

            HashMap<String, String> post = new HashMap<>();
            post.put("ph_country", str_c_code);
            post.put("phone_no", str_ph_num);


            System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
            System.out.println("==ph_country===" + str_c_code);
            System.out.println("==phone_no===" + str_ph_num);
            Call<ResponseBody> call = apiService.send_verification_message(header,post);
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
                                Log.e("----send_verification-->", Str_response);
                                JSONObject object = new JSONObject(Str_response);
                                JSONObject response_obj = object.getJSONObject("responseArr");
                                JSONObject common_obj = object.getJSONObject("commonArr");
                                String status_code = response_obj.getString("status");
                                String message = response_obj.getString("msg");
                                Loader.dismiss();
                                if(status_code.equals("1")){

                                    //Alert(getResources().getString(R.string.action_success),message);
                                    showVerifyCode("Verification code has been sent.");

                                }else
                                {
                                    AlertMobileVerify(getResources().getString(R.string.action_opps),message);
                                }

                            }
                        }

                    } catch (Exception e) {
                        Loader.dismiss();
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

    private void showVerifyCode(String message) {
        AlertDialog alertDialog = null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(Main_homepage.this);

        // Get custom login form view.
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_enter_otp, null);
        // Set above view in alert dialog.
        builder.setView(dialogView);

        // Register button click listener.
        final Button btn_otp_cancel = (Button)dialogView.findViewById(R.id.btn_otp_cancel);
        final Button btn_otp_verify = (Button)dialogView.findViewById(R.id.btn_otp_verify);
        final Button btn_otp_resend = (Button)dialogView.findViewById(R.id.btn_otp_resend);
        final EditText edit_otp = (EditText)dialogView.findViewById(R.id.edit_otp);
        final TextView txt_message = (TextView) dialogView.findViewById(R.id.txt_message);
        txt_message.setText(message);
        alertDialog = builder.create();
        final AlertDialog finalAlertDialog = alertDialog;
        btn_otp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Close Alert Dialog.
                    finalAlertDialog.cancel();
                    // textViewTmp.setText("Register success.");
                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


        btn_otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_otp.getText().toString().isEmpty())
                {
                    snack_bar(getResources().getString(R.string.enter_phone),"");
                }else {
                  CodeVerification(edit_otp.getText().toString());
                    finalAlertDialog.dismiss();
                }
            }
        });

        btn_otp_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  reSendOTP();
                  finalAlertDialog.dismiss();

            }
        });

        // Login button click listener.


        // Reset button click listener.


        builder.setCancelable(true);

        alertDialog.show();
    }

    private void reSendOTP() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", details.get(sessionManager.KEY_USER_ID));
        System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
        Call<ResponseBody> call = apiService.resend_verification_message(header);
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
                            Log.e("----resend_verification_message-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            JSONObject response_obj = object.getJSONObject("responseArr");
                            JSONObject common_obj = object.getJSONObject("commonArr");
//                            String status_code = response_obj.getString("status");
                            String message = response_obj.getString("msg");
                            Loader.dismiss();
                            showVerifyCode("Verification code has been sent.");
                            //Alert(getResources().getString(R.string.action_success),message);

                        }
                    }

                } catch (Exception e) {
                    Loader.dismiss();
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

    private void CodeVerification(String s) {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", details.get(sessionManager.KEY_USER_ID));

        HashMap<String, String> post = new HashMap<>();
        post.put("mobile_verification_code", s);


        System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
        System.out.println("==mobile_verification_code===" + s);
        Call<ResponseBody> call = apiService.verify_mobile(header,post);
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
                            Log.e("----mobile_verification_code-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            JSONObject response_obj = object.getJSONObject("responseArr");
                            JSONObject common_obj = object.getJSONObject("commonArr");
                            String status_code = response_obj.getString("status");
                            String message = response_obj.getString("msg");
                            Loader.dismiss();
                            if(status_code.equals("1")){
                                AlertSuccess(getResources().getString(R.string.action_success),message);

                            }else
                            {
                                showVerifyCode(message);
                                //Alert(getResources().getString(R.string.action_opps),message);
                            }

                        }
                    }

                } catch (Exception e) {
                    Loader.dismiss();
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

    private void Alert_exit(String title, String alert) {
        try {
            final PkDialog mDialog = new PkDialog(Main_homepage.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    //if (android.os.Build.VERSION.SDK_INT >= 21) {
                        //finishAndRemoveTask();
                        finishAffinity();
//                    } else {
//                        finishAffinity();
//                    }

                }
            });
            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    // finish();
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("--------------service started----------");

        Intent alarmIntent = new Intent(Main_homepage.this, UpdateLocationService.class);
        alarmIntent.putExtra("Mode", "available");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Main_homepage.this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);


    }

    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(Main_homepage.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();
    }
}
