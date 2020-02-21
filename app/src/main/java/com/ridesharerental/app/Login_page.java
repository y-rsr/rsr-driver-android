package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.pushnotification.GCMInitializer;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user113 on 11/28/2017.
 */

public class Login_page extends Activity {
    LinearLayout Linear_Back;
    TextView txt_Login, txt_for_got;
    EditText edit_User_Name, edit_Password;
    RelativeLayout Rl_driver, Rl_claimant;
    Common_Loader Loader;
    String str_email_match_key = "";
    String GCM_Id = "";
    String str_user_name = "", str_password = "";
    private Animation animShow, animHide;
    String str_get_user_email = "", str_get_user_name = "", str_get_user_id = "", str_get_user_iamge = "", str_get_active_reservattions = "",signature_image = "";
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Loader = new Common_Loader(Login_page.this);
        session = new SessionManager(Login_page.this);
        init();
        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_User_Name.getText().toString().trim().length() > 0) {
                    if (edit_User_Name.getText().toString().matches(str_email_match_key)) {

                        if (edit_Password.getText().toString().trim().length() > 0) {

                            str_user_name = edit_User_Name.getText().toString();
                            str_password = edit_Password.getText().toString();

                            post_Data();
                            /*Intent intent = new Intent(Login_page.this, Main_homepage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();*/
                        } else {
                            //snack_bar("Enter the password","");
                            snack_bar(getResources().getString(R.string.enter_the_password), "");
                        }

                    } else {
                        //  snack_bar("Enter a valid email id","");
                        snack_bar(getResources().getString(R.string.enter_valid_email), "");
                    }

                } else {
                    //snack_bar("Enter the email","");
                    snack_bar(getResources().getString(R.string.enter_the_email), "");
                }

            }
        });

        txt_for_got.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_page.this, Forgot_Password.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        Linear_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent log_intent = new Intent(Login_page.this, Slider_page.class);
                startActivity(log_intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                Login_page.this.finish();
            }
        });

        Rl_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Login_page.this, DriverClaimActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);


            }
        });

        Rl_claimant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(Login_page.this, DriverClaimentActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            Intent log_intent = new Intent(Login_page.this, Slider_page.class);
            startActivity(log_intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            Login_page.this.finish();
            ;
            return true;
        }
        return false;
    }

    public void init() {

        str_email_match_key = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        txt_Login = (TextView) findViewById(R.id.txt_sign_login);
        txt_for_got = (TextView) findViewById(R.id.txt_id_for_got);
        Linear_Back = (LinearLayout) findViewById(R.id.lin_back);
        Rl_driver = (RelativeLayout) findViewById(R.id.Rl_driver);
        Rl_claimant = (RelativeLayout) findViewById(R.id.Rl_claimant);

        edit_User_Name = (EditText) findViewById(R.id.edit_user_namess);
        edit_Password = (EditText) findViewById(R.id.edi_pass);

        animShow = AnimationUtils.loadAnimation(Login_page.this, R.anim.show_view);
        animHide = AnimationUtils.loadAnimation(Login_page.this, R.anim.hide_view);


        GCMInitializer initializer = new GCMInitializer(Login_page.this, new GCMInitializer.CallBack() {
            @Override
            public void onRegisterComplete(String registrationId) {
                GCM_Id = registrationId;
                System.out.println("-----GCM id-------->" + GCM_Id);
            }

            @Override
            public void onError(String errorMsg) {
            }
        });
        initializer.init();
    }


    //-----------------service---------------------

    public void post_Data() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", str_user_name);
        map.put("password", str_password);
        map.put("gcm_key", GCM_Id);

        System.out.println("-----Login Url-------->" + IConstant_WebService.baseurl + "app/driver/login");
        Set keys = map.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) map.get(key);

            System.out.println("" + key + ":" + value);
        }

        Call<ResponseBody> call = apiService.driver_Login(map);
        System.out.println("-----------Login url------>" + call.request().url().toString());

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
                        String str_status = response_obj.getString("status");
                        String str_message = response_obj.getString("msg");
                        if (str_status.equals("1")) {
                            // Alert_sucess("Success!!!",str_message);

                            JSONObject json_Common_Array = object.getJSONObject("commonArr");
                            // String str_get_user_email="",str_get_user_name="",str_get_user_id="",str_get_user_iamge;
                            str_get_user_email = json_Common_Array.getString("email");
                            //  str_get_user_name=json_Common_Array.getString("commonId");
                            str_get_user_id = json_Common_Array.getString("commonId");
                            str_get_user_iamge = json_Common_Array.getString("profile_pic");
                            str_get_active_reservattions = json_Common_Array.getString("active_reservation");
                            signature_image = json_Common_Array.getString("signature_image");
                            session.set_user_details(str_get_user_id, str_get_user_email, "", str_get_user_iamge, str_password,signature_image);
                            session.set_msg_count(json_Common_Array.getString("unread_message_count"));

                            Intent intent = new Intent(Login_page.this, Main_homepage.class);
                            intent.putExtra("calling_type", str_get_active_reservattions);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();

                        } else {
                            Alert(getResources().getString(R.string.action_opps), str_message);
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


    //-----------------------snack bar--------------
    private void snack_bar(String title, String message) {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        // TextView Tv_message = (TextView) view.findViewById(R.id.txt_message);

        Tv_title.setText(title);
        // Tv_message.setText(message);

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(Login_page.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }


    public void Alert_sucess(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(Login_page.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);


        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(Login_page.this, Main_homepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        });

        mDialog.show();
    }


    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(Login_page.this);
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
