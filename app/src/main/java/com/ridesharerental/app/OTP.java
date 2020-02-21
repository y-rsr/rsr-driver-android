package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
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
 * Created by user65 on 1/25/2018.
 */

public class OTP extends Activity
{
    String str_mode="",str_verification_code="",str_f_name = "",str_l_name = "" , str_gcmKey= "",str_phone = "" , str_country = "",  str_referal = ",";
    EditText edit_otp;
    TextView btn_verify;
    LinearLayout lin_back;
    Common_Loader Loader;
    SessionManager session;
    TextView txt_resent;
    String str_get_user_id="",str_get_user_email="",str_get_user_iamge,str_password="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        session=new SessionManager(OTP.this);
        Loader=new Common_Loader(OTP.this);
        init();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(edit_otp.getText().toString().trim().length()>0)
                {
                    if(edit_otp.getText().toString().equalsIgnoreCase(str_verification_code))
                    {


                        save_data("normal");

                        //Sign_up_page.sign_up_page.finish();
                    }
                    else
                    {
                        snack_bar("Invalid OTP","");
                    }
                }
                else
                {
                    snack_bar("Enter your OTP","");
                }
            }
        });


        lin_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                overridePendingTransition(R.anim.enter, R.anim.exit);
                OTP.this.finish();
            }
        });

        txt_resent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_otp();
            }
        });
    }


    public void init()
    {
        lin_back=(LinearLayout)findViewById(R.id.lin_back);
        edit_otp=(EditText)findViewById(R.id.edit_otp);
        btn_verify=(TextView)findViewById(R.id.btn_verify);
        txt_resent=(TextView) findViewById(R.id.txt_resent);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {

            str_mode=bundle.getString("mode");
            str_verification_code=bundle.getString("verification_ocde");
            //edit_otp.setText(str_verification_code);

          //  str_get_user_id=bundle.getString("user_id");
            str_get_user_email=bundle.getString("user_email");
            str_get_user_iamge=bundle.getString("user_image");
            str_password=bundle.getString("password");
            str_f_name=bundle.getString("firstname");
            str_l_name=bundle.getString("lastname");
            str_gcmKey=bundle.getString("gcm_key");
            str_phone=bundle.getString("phone_no");
            str_country=bundle.getString("ph_country");
            str_referal=bundle.getString("referral_code");



        }
    }


    public void save_data(final String login_type)
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("firstname", str_f_name);
        map.put("lastname",str_l_name);
        map.put("email", str_get_user_email);
        map.put("gcm_key", str_gcmKey);

        if(str_phone != null  && !str_phone.equals("") && str_phone.length()>0)
        {
            map.put("phone_no", str_phone);
            map.put("ph_country", str_country);
        }

        if(str_password!=null && !str_password.equals(""))
        {
            map.put("password", str_password);
        }

        if(str_referal!=null && str_referal.length()>0)
        {
            map.put("referral_code", str_referal);
        }



        map.put("login_type",login_type);
        if(str_mode!=null  && !str_mode.equals("") && str_mode.equals("production"))
        {
            map.put("ph_verified","Yes");
        }
        else
        {
            map.put("ph_verified","No");
        }



        // map.put("facebookId", "android");
        // map.put("googleImage", "android");


        Set keys = map.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) map.get(key);
            System.out.println(""+key+":"+value);
        }

        Call<ResponseBody> call = apiService.user_register(map);

        System.out.println("-----------signup url------>"+call.request().url().toString());

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
                        Log.e("----Reg Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status=response_obj.getString("status");
                        String str_message=response_obj.getString("msg");
                        if(str_status.equals("1"))
                        {

                            JSONObject common_Obj=object.getJSONObject("commonArr");

                            String str_profile_image = common_Obj.getString("profile_pic");
                            session.set_profile(str_profile_image);

                            String str_msg_coutn=common_Obj.getString("unread_message_count");
                            session.set_msg_count(str_msg_coutn);

                            // Alert_sucess("Success!!!",str_message);
                            JSONObject json_Common_Array=object.getJSONObject("commonArr");
                            str_get_user_email=json_Common_Array.getString("email");
                            //  str_get_user_name=json_Common_Array.getString("commonId");
                            str_get_user_id=json_Common_Array.getString("commonId");
                            str_get_user_iamge=json_Common_Array.getString("profile_pic");
                            session.set_user_details(str_get_user_id,str_get_user_email,"",str_get_user_iamge,str_password,json_Common_Array.has("signature_image")? json_Common_Array.getString("signature_image"):"");
                            Intent intent = new Intent(OTP.this, Main_homepage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();


                        }
                        else
                        {
                            Alert("Info",str_message);
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
    public void Alert(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(OTP.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(OTP.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }

    public void send_otp()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> post = new HashMap<>();
        post.put("email", str_get_user_email);
        post.put("ph_country", str_country);
        post.put("phone_no",str_phone);
        Call<ResponseBody> call = apiService.send_otp(post);
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
                        Log.e("---- Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        String str_msg=response_obj.getString("msg");
                        if(str_status_code.equals("1"))
                        {
                            str_mode=response_obj.getString("mode");
                            str_verification_code=response_obj.getString("mobile_verification_code");
                            Toast.makeText(getApplicationContext(),str_msg,Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Alert("Failure",str_msg);
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
}
