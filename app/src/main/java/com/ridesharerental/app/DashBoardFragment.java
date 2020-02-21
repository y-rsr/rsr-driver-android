package com.ridesharerental.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.countrycodepicker.CountryPicker;
import com.countrycodepicker.CountryPickerListener;
import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class DashBoardFragment extends Fragment implements View.OnClickListener {
    TextView my_profile_layout_editProfile,my_profile_layout_name,txt_email_verrify_desc,txt_phone_verified
            ,txt_email_verified,txt_email_send,txt_phone_send,txt_country_code,view_profile,manage_list;
    Common_Loader Loader;
    SessionManager sessionManager;
    HashMap<String, String> details;
    String user_id = "";
    CircleImageView img_profile;
    Dialog dialog;
    int exit = 0;
    CountryPicker myPicker;
    Button btn_otp_resend,btn_cancel,btn_submit;
    Button btn_otp_cancel,btn_otp_verify;
    LinearLayout ll_enter_otp,ll_enter_mobile;
    EditText edit_mobile_number,edit_otp;


    public DashBoardFragment() {
        // Required empty public constructor
    }


//    public static DashBoardFragment newInstance(String param1, String param2) {
//        DashBoardFragment fragment = new DashBoardFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        init(view);
        return view;
    }

    private void init(View view)
    {
        my_profile_layout_name = (TextView) view.findViewById(R.id.my_profile_layout_name);
        txt_email_verrify_desc = (TextView) view.findViewById(R.id.txt_email_verrify_desc);
        txt_email_verified = (TextView) view.findViewById(R.id.txt_email_verified);
        txt_phone_verified = (TextView) view.findViewById(R.id.txt_phone_verified);
        txt_email_send = (TextView) view.findViewById(R.id.txt_email_send);
        txt_phone_send = (TextView) view.findViewById(R.id.txt_phone_send);
        txt_country_code = (TextView) view.findViewById(R.id.txt_country_code);
        view_profile = (TextView) view.findViewById(R.id.view_profile);
        manage_list = (TextView) view.findViewById(R.id.manage_list);
        txt_country_code = (TextView) view.findViewById(R.id.txt_country_code);

        my_profile_layout_editProfile = (TextView) view.findViewById(R.id.my_profile_layout_editProfile);
        img_profile = (CircleImageView) view.findViewById(R.id.img_profile);

        ll_enter_mobile = (LinearLayout) view.findViewById(R.id.ll_enter_mobile);
        ll_enter_otp = (LinearLayout) view.findViewById(R.id.ll_enter_otp);
        edit_mobile_number = (EditText) view.findViewById(R.id.edit_mobile_number);
        edit_otp = (EditText) view.findViewById(R.id.edit_otp);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_otp_cancel = (Button) view.findViewById(R.id.btn_otp_cancel);
        btn_otp_verify = (Button) view.findViewById(R.id.btn_otp_verify);
        btn_otp_resend = (Button) view.findViewById(R.id.btn_otp_resend);

        my_profile_layout_editProfile.setOnClickListener(this);
        txt_email_send.setOnClickListener(this);
        txt_phone_send.setOnClickListener(this);

        txt_country_code.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_otp_cancel.setOnClickListener(this);
        btn_otp_verify.setOnClickListener(this);
        btn_otp_resend.setOnClickListener(this);
        view_profile.setOnClickListener(this);
        manage_list.setOnClickListener(this);

        ll_enter_mobile.setVisibility(View.GONE);
        ll_enter_otp.setVisibility(View.GONE);


        sessionManager = new SessionManager(getActivity());
        Loader = new Common_Loader(getActivity());
        details = sessionManager.getUserDetails();
        myPicker = CountryPicker.newInstance("Select Country");

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);

        myPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int aFlag) {
                myPicker.dismiss();
                txt_country_code.setText(dialCode);

            }
        });
        view.setFocusableInTouchMode(true);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_BACK) {

                    if(exit==0)
                        showBackPressedDialog();
                    else{
                        ll_enter_mobile.setVisibility(View.GONE);
                        ll_enter_otp.setVisibility(View.GONE);
                        exit = 0;}

                    System.out.println("------back pressed-----------------");

                    return true;

                }


                return true;
            }
        });
        Load_Data();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.my_profile_layout_editProfile:

                startActivity(new Intent(getActivity(),My_Edit_profile.class));
                break;

            case R.id.view_profile:

                Intent my_Edit = new Intent(getActivity(), Main_homepage.class);
                my_Edit.putExtra("calling_type", "edit_profile");
                startActivity(my_Edit);
                break;

            case R.id.txt_email_send:
                resend_email_verification();
                break;

            case R.id.txt_phone_send:
                ll_enter_mobile.setVisibility(View.VISIBLE);
                ll_enter_otp.setVisibility(View.GONE);
                Main_homepage.exit = 1;
                exit = 1 ;
                break;

            case R.id.txt_country_code:
                myPicker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
                break;

            case R.id.btn_otp_verify:
                verify_mobile();
                break;

            case R.id.btn_otp_cancel:
                ll_enter_mobile.setVisibility(View.GONE);
                ll_enter_otp.setVisibility(View.GONE);
                exit = 0 ;
                break;

            case R.id.btn_cancel:
                ll_enter_mobile.setVisibility(View.GONE);
                ll_enter_otp.setVisibility(View.GONE);
                Main_homepage.exit = 0;
                exit = 0 ;
                break;

            case R.id.btn_submit:
                send_verification_message();
                break;

            case R.id.btn_otp_resend:
                resend_verification_message();
                break;
        }


    }

    private void resend_verification_message() {
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
                            Alert(getActivity().getResources().getString(R.string.action_success),message);

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

    private void verify_mobile() {
        if(edit_otp.getText().toString().isEmpty())
        {
            snack_bar("Please enter OTP","");
        }else
        {
            //edit_otp.setText("");

            Loader.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            HashMap<String, String> header = new HashMap<>();
            header.put("commonId", details.get(sessionManager.KEY_USER_ID));

            HashMap<String, String> post = new HashMap<>();
            post.put("mobile_verification_code", edit_otp.getText().toString());


            System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
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
                                    edit_otp.setText("");
                                    Load_Data();
                                    Alert(getActivity().getResources().getString(R.string.action_success),message);
                                    ll_enter_mobile.setVisibility(View.GONE);
                                    ll_enter_otp.setVisibility(View.GONE);
                                    exit = 0 ;
                                }else
                                {
                                    Alert(getActivity().getResources().getString(R.string.action_opps),message);
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
    }

    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(getActivity(), msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.BOTTOM);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        //snack.setAnimation(R.anim.slide_up_anim, R.anim.slide_down_anim);
        snack.show();
    }
    private void send_verification_message() {

        if(edit_mobile_number.getText().toString().isEmpty())
        {
            snack_bar(getResources().getString(R.string.enter_phone),"");
        }else
        {


            Loader.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            HashMap<String, String> header = new HashMap<>();
            header.put("commonId", details.get(sessionManager.KEY_USER_ID));

            HashMap<String, String> post = new HashMap<>();
            post.put("ph_country", txt_country_code.getText().toString());
            post.put("phone_no", edit_mobile_number.getText().toString());


            System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
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
                                Log.e("----resend_verification-->", Str_response);
                                JSONObject object = new JSONObject(Str_response);
                                JSONObject response_obj = object.getJSONObject("responseArr");
                                JSONObject common_obj = object.getJSONObject("commonArr");
                                String status_code = response_obj.getString("status");
                                String message = response_obj.getString("msg");
                                Loader.dismiss();
                                if(status_code.equals("1")){
                                    edit_mobile_number.setText("");
                                    Alert(getActivity().getResources().getString(R.string.action_success),getString(R.string.success_otp_send));
                                    ll_enter_mobile.setVisibility(View.GONE);
                                    ll_enter_otp.setVisibility(View.VISIBLE);
                                }else
                                {
                                    Alert(getActivity().getResources().getString(R.string.action_opps),message);
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

    }

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
                            Log.e("----Profile Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            JSONObject response_obj = object.getJSONObject("responseArr");
                            JSONObject common_obj = object.getJSONObject("commonArr");
                            String status_code = response_obj.getString("status");
                            if (status_code != null && !status_code.equals("") && status_code.equals("1")) {
                                JSONObject driver_detail_obj = response_obj.getJSONObject("driverDetails");

                                my_profile_layout_name.setText(driver_detail_obj.getString("firstname") + " " + driver_detail_obj.getString("lastname"));
                                Picasso.with(getActivity()).load(driver_detail_obj.getString("profile_pic")).into(img_profile);
                                txt_email_verrify_desc.setText("Please verify your email address by clicking the link in the message we just sent to " + common_obj.getString("email") + " can't find our message.");

                                txt_phone_verified.setText(common_obj.getString("ph_verified").equals("Yes")?"Phone Number-Verified":"Phone Verification Pending");
                                txt_email_verified.setText(common_obj.getString("email_verified").equals("Yes")?"Email Address-Verified":"Email Verification Pending");


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
    public void resend_email_verification() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", details.get(sessionManager.KEY_USER_ID));
        System.out.println("==KEY_USER_ID===" + details.get(sessionManager.KEY_USER_ID));
        Call<ResponseBody> call = apiService.resend_email_verification(header);
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
                            Log.e("----resend_email-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            JSONObject response_obj = object.getJSONObject("responseArr");
                            JSONObject common_obj = object.getJSONObject("commonArr");
//                            String status_code = response_obj.getString("status");
                            String message = response_obj.getString("msg");
                            Loader.dismiss();
                            Alert(getActivity().getResources().getString(R.string.action_success),message);

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

    private void Alert(String title,String message)
    {
        final PkDialog mDialog = new PkDialog(getActivity());
        mDialog.setDialogTitle(title);
        mDialog.setDialogMessage(message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();


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
    private void showBackPressedDialog() {
        System.gc();
        final PkDialog mDialog = new PkDialog(getActivity());
        mDialog.setDialogTitle("Alert");
        mDialog.setDialogMessage(getResources().getString(R.string.dialog_app_exiting));

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                getActivity().finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

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

}
