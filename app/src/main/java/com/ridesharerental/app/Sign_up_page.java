package com.ridesharerental.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.countrycodepicker.CountryPicker;
import com.countrycodepicker.CountryPickerListener;
import com.devspark.appmsg.AppMsg;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ridesharerental.pushnotification.GCMInitializer;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.ActivitySwitcher;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.RippleView;
import com.ridesharerental.widgets.SimpleSpanBuilder;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user113 on 11/28/2017.
 */

public class Sign_up_page  extends AppCompatActivity implements   GoogleApiClient.OnConnectionFailedListener
{
    LinearLayout Linear_Back;
    TextView txt_terms_conditions;
    TextView txt_sign_up;
    RelativeLayout Rel_sign_Up;
    //public static Sign_up_page sign_up_page;

    EditText edit_First_Name,Edit_Last_Name,Edit_text_email,edit_mobile_number,edit_password,edit_confrim_pass;
    TextView txt_country_code;
    Common_Loader Loader;
    RelativeLayout Rel_container;
    String str_email_match_key="";
    String GCM_Id="";
    String str_first_name="",str_last_name="",str_email="",str_password="";
    String str_mode="",verification_oode="";
    SessionManager session;
    String str_get_user_email="",message="",str_get_user_id="",str_get_user_iamge;

    private CountryPicker myPicker;


    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    ImageView img_google_signUp;
    String gmail_personPhotoUrl1="",str_get_first_name="";

    RippleView Ripple_google_plus;

    EditText editText_referal_code;

    TextView txt_terms,txt_privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Loader=new Common_Loader(Sign_up_page.this);
        session=new SessionManager(Sign_up_page.this);

        gso = ((MyApplication) getApplication()).getGoogleSignInOptions();
        mGoogleApiClient = ((MyApplication) getApplication()).getGoogleApiClient(Sign_up_page.this, this);

        myPicker = CountryPicker.newInstance("Select Country");

        init();

        txt_country_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                myPicker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });


        myPicker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int aFlag) {
                myPicker.dismiss();
                txt_country_code.setText(dialCode);
                message = code;
            }
        });
//        txt_sign_up.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if(edit_First_Name.getText().toString().trim().length()>0)
//                {
//                    if(Edit_Last_Name.getText().toString().trim().length()>0)
//                    {
//                        if(Edit_text_email.getText().toString().trim().length()>0)
//                        {
//                            if(Edit_text_email.getText().toString().matches(str_email_match_key))
//                            {
//                                if(edit_password.getText().toString().trim().length()>0)
//                                {
//                                        if(edit_confrim_pass.getText().toString().trim().length()>0)
//                                        {
//                                            if(edit_password.getText().toString().toString().trim().length()>5)
//                                            {
//                                                if(edit_password.getText().toString().equals(edit_confrim_pass.getText().toString()))
//                                                {
//
//                                                    if (edit_mobile_number.getText().toString().length()>0)
//                                                    {
//                                                        str_first_name=edit_First_Name.getText().toString();
//                                                        str_last_name=Edit_Last_Name.getText().toString();
//                                                        str_email=Edit_text_email.getText().toString();
//                                                        str_password=edit_password.getText().toString();
//                                                        str_password=edit_password.getText().toString();
//                                                        //save_data("normal");
//                                                        send_otp();
//                                                    }
//                                                    else
//                                                    {
//                                                       // snack_bar("Enter phone number","");
//                                                        snack_bar(getResources().getString(R.string.enter_phone),"");;
//                                                    }
//
//                                                }
//                                                else
//                                                {
//                                                   // snack_bar("Password and confirm password\n should be same","");
//                                                    snack_bar(getResources().getString(R.string.same_password),"");;
//                                                }
//                                            }
//                                            else
//                                            {
//                                               // snack_bar("Minimum password length is 6","");
//                                                snack_bar(getResources().getString(R.string.enter_maximum_password),"");;
//                                            }
//
//                                        }
//                                        else
//                                        {
//                                           // snack_bar("Enter Confirm Password","");
//                                            snack_bar(getResources().getString(R.string.enter_confirm_password),"");;
//                                        }
//                                    }
//                                else
//                                {
//                                  //  snack_bar("Enter Password","");
//                                    snack_bar(getResources().getString(R.string.enter_password),"");;
//                                }
//
//                            }
//                            else
//                            {
//                               // snack_bar("Enter valid email","");
//                                snack_bar(getResources().getString(R.string.enter_your_valid_email_address),"");
//                            }
//
//
//                        }
//                        else
//                        {
//                            //Edit_text_email.setError("Enter your Email");
//                           // snack_bar("Enter your E-mail address","");
//                            snack_bar(getResources().getString(R.string.enter_your_email_address),"");
//                        }
//                    }
//                    else
//                    {
//                        snack_bar(getResources().getString(R.string.enter_your_last_name),"");
//                    }
//
//
//                }
//                else
//                {
//                    snack_bar(getResources().getString(R.string.enter_your_first_name),"");
//                }
//            }
//        });

        Linear_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent log_intent=new Intent(Sign_up_page.this,Slider_page.class);
                startActivity(log_intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                Sign_up_page.this.finish();
            }
        });




        Rel_sign_Up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(edit_First_Name.getText().toString().trim().length()>0)
                {
                    if(Edit_Last_Name.getText().toString().trim().length()>0)
                    {
                        if(Edit_text_email.getText().toString().trim().length()>0)
                        {
                            if(Edit_text_email.getText().toString().matches(str_email_match_key))
                            {
                                if(edit_password.getText().toString().trim().length()>0)
                                {
                                    if(edit_confrim_pass.getText().toString().trim().length()>0)
                                    {
                                        if(edit_password.getText().toString().toString().trim().length()>5)
                                        {
                                            if(edit_password.getText().toString().equals(edit_confrim_pass.getText().toString()))
                                            {

                                                if (edit_mobile_number.getText().toString().length()>0)
                                                {
                                                    str_first_name=edit_First_Name.getText().toString();
                                                    str_last_name=Edit_Last_Name.getText().toString();
                                                    str_email=Edit_text_email.getText().toString();
                                                    str_password=edit_password.getText().toString();
                                                    str_password=edit_password.getText().toString();
                                                    //save_data("normal");
                                                    send_otp();
                                                }
                                                else
                                                {
                                                    // snack_bar("Enter phone number","");
                                                    snack_bar(getResources().getString(R.string.enter_phone),"");;
                                                }
                                            }
                                            else
                                            {
                                                // snack_bar("Password and confirm password\n should be same","");
                                                snack_bar(getResources().getString(R.string.same_password),"");;
                                            }
                                        }
                                        else
                                        {
                                            // snack_bar("Minimum password length is 6","");
                                            snack_bar(getResources().getString(R.string.enter_maximum_password),"");;
                                        }
                                    }
                                    else
                                    {
                                        // snack_bar("Enter Confirm Password","");
                                        snack_bar(getResources().getString(R.string.enter_confirm_password),"");;
                                    }
                                }
                                else
                                {
                                    //  snack_bar("Enter Password","");
                                    snack_bar(getResources().getString(R.string.enter_password),"");;
                                }

                            }
                            else
                            {
                                // snack_bar("Enter valid email","");
                                snack_bar(getResources().getString(R.string.enter_your_valid_email_address),"");
                            }

                        }
                        else
                        {
                            //Edit_text_email.setError("Enter your Email");
                            // snack_bar("Enter your E-mail address","");
                            snack_bar(getResources().getString(R.string.enter_your_email_address),"");
                        }
                    }
                    else
                    {
                        snack_bar(getResources().getString(R.string.enter_your_last_name),"");
                    }

                }
                else
                {
                    snack_bar(getResources().getString(R.string.enter_your_first_name),"");
                }
            }
        });




       /* Rel_sign_Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(edit_First_Name.getText().toString().trim().length()>0)
                {

                    if(Edit_Last_Name.getText().toString().trim().length()>0)
                    {

                        if(Edit_text_email.getText().toString().trim().length()>0)
                        {
                            if(Edit_text_email.getText().toString().matches(str_email_match_key))
                            {
                                if(edit_password.getText().toString().trim().length()>0)
                                {
                                    if(edit_confrim_pass.getText().toString().trim().length()>0)
                                    {
                                        if(edit_password.getText().toString().toString().trim().length()>5)
                                        {

                                            if(edit_password.getText().toString().equals(edit_confrim_pass.getText().toString()))
                                            {
                                                str_first_name=edit_First_Name.getText().toString();
                                                str_last_name=Edit_Last_Name.getText().toString();
                                                str_email=Edit_text_email.getText().toString();
                                                str_password=edit_password.getText().toString();
                                                //save_data("normal");
                                                send_otp();

                                                // Intent intent = new Intent(Sign_up_page.this, Main_homepage.class);
                                                //  startActivity(intent);
                                                //  overridePendingTransition(R.anim.enter, R.anim.exit);
                                                //  finish();
                                            }
                                            else
                                            {
                                             //   snack_bar("Password and confirm password\n should be same","");
                                                snack_bar(getResources().getString(R.string.same_password),"");;
                                            }
                                        }
                                        else
                                        {
                                           // snack_bar("Minimum password length is 6","");
                                            snack_bar(getResources().getString(R.string.enter_maximum_password),"");;
                                        }
                                    }
                                    else
                                    {
                                       // snack_bar("Enter Confirm Password","");
                                        snack_bar(getResources().getString(R.string.enter_confirm_password),"");;
                                    }

                                }
                                else
                                {
                                  //  snack_bar("Enter Password","");
                                    snack_bar(getResources().getString(R.string.enter_password),"");;
                                }

                            }
                            else
                            {
                               // snack_bar("Enter valid email","");
                                snack_bar(getResources().getString(R.string.enter_your_valid_email_address),"");
                            }


                        }
                        else
                        {
                          //  snack_bar("Enter your E-mail address","");
                            snack_bar(getResources().getString(R.string.enter_your_email_address),"");
                        }
                    }
                    else
                    {
                       // snack_bar("Enter your Last Name","");
                        snack_bar(getResources().getString(R.string.enter_your_last_name),"");
                    }


                }
                else
                {
                   // edit_First_Name.setError("Enter your First Name");
                 //   snack_bar("Enter your First Name","");
                    snack_bar(getResources().getString(R.string.enter_your_first_name),"");
                }
            }
        });*/

        img_google_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signOutFromGplus();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        Ripple_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

               // mobile_verification();
                signOutFromGplus();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String url = IConstant_WebService.my_terms_condition;
                System.out.println("------Terms condition-------->"+IConstant_WebService.my_terms_condition);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        txt_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String url = IConstant_WebService.my_privacy_policy;
                System.out.println("------Privacy policy-------->"+IConstant_WebService.my_privacy_policy);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


    }

    public void init()
    {
        str_email_match_key = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        editText_referal_code=(EditText)findViewById(R.id.edit_referal_code);
        img_google_signUp=(ImageView)findViewById(R.id.img_google);
        edit_password=(EditText)findViewById(R.id.edit_password);
        edit_confrim_pass=(EditText)findViewById(R.id.edit_confrim_pass);

        Rel_container=(RelativeLayout)findViewById(R.id.rel_container);

        edit_First_Name=(EditText)findViewById(R.id.edit_first_Name);
        Edit_Last_Name=(EditText)findViewById(R.id.editText_last_name);
        Edit_text_email=(EditText)findViewById(R.id.edit_email);
        edit_mobile_number=(EditText)findViewById(R.id.edit_mobile_number);
        txt_country_code=(TextView)findViewById(R.id.txt_country_code);

        txt_terms_conditions=(TextView)findViewById(R.id.txt_terms_and_conditions);
        txt_sign_up=(TextView)findViewById(R.id.txt_sign_up);
        Linear_Back=(LinearLayout)findViewById(R.id.lin_back);

        Rel_sign_Up=(RelativeLayout)findViewById(R.id.rel_signup);

        Ripple_google_plus=(RippleView)findViewById(R.id.main_login_google_plus);


        txt_terms=(TextView)findViewById(R.id.txt_terms);
        txt_privacy_policy=(TextView)findViewById(R.id.txt_privacy_policy);


        GCMInitializer initializer = new GCMInitializer(Sign_up_page.this, new GCMInitializer.CallBack() {
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

       /* final SpannableString text = new SpannableString("By signing,you are agree to our terms and \n privacy policy");
        text.setSpan(new RelativeSizeSpan(1.5f), text.length() - " our terms and \n privacy policy".length(), text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new ForegroundColorSpan(Color.RED), 3, text.length() - 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_terms_conditions.setText(text);*/



        SimpleSpanBuilder ssb = new SimpleSpanBuilder();
        ssb.appendWithSpace("By signing,you are agree to our");
        ssb.append("terms",new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),new RelativeSizeSpan(1.1f));
        ssb.append(" and ",new ForegroundColorSpan(getResources().getColor(R.color.ligth_black)),new RelativeSizeSpan(1.1f));
        ssb.append(" privacy policy",new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),new RelativeSizeSpan(1.1f));
        txt_terms_conditions.setText(ssb.build());


        //txt_terms_conditions.setText("By signing,you are agree to our terms and \n privacy policy");
    }


    private void animatedStartActivity()
    {
        final Intent intent = new Intent(Sign_up_page.this,
                Main_homepage.class);
        // disable default animation for new intent
        ActivitySwitcher.animationOut(findViewById(R.id.container),
                getWindowManager(),
                new ActivitySwitcher.AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished()
                    {
                        startActivity(intent);
                    }
                });
    }



    //-----------------service---------------------

    public void save_data(final String login_type)
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("firstname", edit_First_Name.getText().toString());
        map.put("lastname",Edit_Last_Name.getText().toString());
        map.put("email", Edit_text_email.getText().toString());
        map.put("gcm_key", GCM_Id);

        if(edit_mobile_number.getText().toString().trim().length()>0)
        {
            map.put("phone_no", edit_mobile_number.getText().toString());
            map.put("ph_country", txt_country_code.getText().toString());
        }

        if(str_password!=null && !str_password.equals(""))
        {
            map.put("password", edit_password.getText().toString());
        }

        if(editText_referal_code.getText().toString().trim().length()>0)
        {
            map.put("referral_code", editText_referal_code.getText().toString());
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
                        Log.e("----Country Response-->", Str_response);
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
                            //session.set_user_details(str_get_user_id,str_get_user_email,"",str_get_user_iamge,str_password);

                            //verification_oode

                            Intent intent = new Intent(Sign_up_page.this, OTP.class);
                            intent.putExtra("mode",str_mode);
                            intent.putExtra("verification_ocde",verification_oode);

                            intent.putExtra("user_id",str_get_user_id);
                            intent.putExtra("user_email",str_get_user_email);
                            intent.putExtra("user_image",str_get_user_iamge);
                            intent.putExtra("user_password",str_password);

                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            //finish();

                           /* if(login_type!=null && !login_type.equals("") && login_type.equalsIgnoreCase("normal"))
                            {

                            }
                            else
                            {
                                Intent intent = new Intent(Sign_up_page.this, Main_homepage.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            }*/



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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0))
        {
            Intent log_intent=new Intent(Sign_up_page.this,Slider_page.class);
            startActivity(log_intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            Sign_up_page.this.finish();
            return true;
        }
        return false;
    }


   public void snack_bar1()
   {
      /* Snackbar snackbar = Snackbar.make(Rel_container, "", Snackbar.LENGTH_SHORT);
       Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
       TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
       textView.setVisibility(View.INVISIBLE);
       View view = getLayoutInflater().inflate(R.layout.custom_snack_bar, null);

       final ViewGroup.LayoutParams params = snackbar.getView().getLayoutParams();
       if (params instanceof CoordinatorLayout.LayoutParams) {
           ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
       } else {
           ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
       }
       snackbar.getView().setLayoutParams(params);
       snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
       //view.setBackgroundColor(Color.TRANSPARENT);
       layout.addView(view, 0);

       TextView txt_alert=(TextView)view.findViewById(R.id.txt_alert);
       txt_alert.setText("Enter First Name");
       snackbar.show();*/

   }




    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(Sign_up_page.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }



    public void Alert_sucess(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(Sign_up_page.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                Intent intent = new Intent(Sign_up_page.this, Main_homepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        });

        mDialog.show();
    }


    public void Alert(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(Sign_up_page.this);
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



    private void signOutFromGplus() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);

                    }
                });
    }


    private void updateUI(boolean isSignedIn)
    {
        if (isSignedIn) {
            // btnSignIn.setVisibility(View.GONE);
			/*
			 * btnSignOut.setVisibility(View.VISIBLE);
			 * btnRevokeAccess.setVisibility(View.VISIBLE);
			 * llProfileLayout.setVisibility(View.VISIBLE);
			 */
        } else {
            // btnSignIn.setVisibility(View.VISIBLE);
			/*
			 * btnSignOut.setVisibility(View.GONE);
			 * btnRevokeAccess.setVisibility(View.GONE);
			 * llProfileLayout.setVisibility(View.GONE);
			 */
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result)
    {
        requestSyncForAccounts();
        getUsernameLong(Sign_up_page.this,result);
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        /*if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount userAccount = result.getSignInAccount();
            System.out.println("------User Account------>"+userAccount);
            if(userAccount.getPhotoUrl()!=null && !userAccount.getPhotoUrl().equals(""))
            {
                gmail_personPhotoUrl1 = userAccount.getPhotoUrl().toString();
                System.out.println("------gmail profile image url-------->"+gmail_personPhotoUrl1);
            }
            String userId = userAccount.getId();
            String displayedUsername="";
            if(userAccount.getDisplayName()!=null && !userAccount.getDisplayName().equals(""))
            {
                displayedUsername = userAccount.getDisplayName();
            }
            else
            {
                displayedUsername = userAccount.getGivenName();
            }
            str_email = userAccount.getEmail();

            if(str_email!=null && !str_email.equals(""))
            {

                if(displayedUsername!=null && !displayedUsername.equals(""))
                {
                    try {
                        String[] parts = displayedUsername.split(" ");
                        str_get_first_name = parts[0];
                        str_get_last_name = parts[1];
                    }catch (Exception e)
                    {
                    }
                }
                System.out.println("-----User Id---->"+userId);
                System.out.println("----User Name----->"+displayedUsername);
                System.out.println("-----User Email---->"+str_email);
                facebook_login("google");
                // String userProfilePhoto = userAccount.getPhotoUrl().toString();
            }

        }*/
    }


    private void requestSyncForAccounts() {
        SyncAdapterType[] syncAdapters = ContentResolver.getSyncAdapterTypes();
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        Account[] accounts = AccountManager.get(Sign_up_page.this).getAccounts();
        for (Account account : accounts) {
            for (int j = 0; j < syncAdapters.length; j++) {
                SyncAdapterType sa = syncAdapters[j];
                if (ContentResolver.getSyncAutomatically(account, sa.authority)) {
                    ContentResolver.requestSync(account, sa.authority, extras);
                }
            }
        }
    }


    public  String getUsernameLong(Context context, GoogleSignInResult result)
    {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();
        for (Account account : accounts) {
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
            Log.i("DGEN ACCOUNT","CALENDAR LIST ACCOUNT/"+account.name);
        }
        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null)
        {
            String email = possibleEmails.get(0);
            if (result.isSuccess())
            {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount userAccount = result.getSignInAccount();
                System.out.println("------User Account------>"+userAccount);
                if(userAccount.getPhotoUrl()!=null && !userAccount.getPhotoUrl().equals(""))
                {
                    gmail_personPhotoUrl1 = userAccount.getPhotoUrl().toString();
                    System.out.println("------gmail profile image url-------->"+gmail_personPhotoUrl1);
                }

                String userId = userAccount.getId();
                String displayedUsername="";
                if(userAccount.getDisplayName()!=null && !userAccount.getDisplayName().equals(""))
                {
                    displayedUsername = userAccount.getDisplayName();
                }

                str_email = userAccount.getEmail();

                if(str_email!=null && !str_email.equals(""))
                {
                    if(userAccount.getDisplayName()!=null && !userAccount.getDisplayName().equals(""))
                    {
                        try {
                            displayedUsername = userAccount.getDisplayName();
                            String[] parts = displayedUsername.split(" ");
                            // str_get_first_name = parts[0];
                            // str_get_last_name = parts[1];
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    str_first_name=displayedUsername;
                    System.out.println("-----User Id---->"+userId);
                    System.out.println("----User Name----->"+displayedUsername);
                    System.out.println("-----User Email---->"+str_email);
                    google_sigup("google");
                }

            }
            return email;

        }
        return null;
    }




    public void  google_sigup(final String login_type)
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("firstname", str_first_name);
        map.put("lastname",str_last_name);
        map.put("email", str_email);
        map.put("gcm_key",GCM_Id);


        if(login_type!=null && !login_type.equals(""))
        {
             if(login_type.equals("google"))
            {
                map.put("login_type", "google");
                map.put("googleImage", gmail_personPhotoUrl1);
                System.out.println("--------while gmail login-post----->"+gmail_personPhotoUrl1);
            }
        }

        Set keys = map.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) map.get(key);

            System.out.println(""+key+":"+value);
        }

        Call<ResponseBody> call = apiService.user_register(map);
        System.out.println("-----------Login url------>"+call.request().url().toString());
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

                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status=response_obj.getString("status");
                        String str_message=response_obj.getString("msg");
                        if(str_status.equals("1"))
                        {
                            //Alert_Success("Success!!!",str_message);
                            JSONObject json_Common_Array=object.getJSONObject("commonArr");
                            str_get_user_email=json_Common_Array.getString("email");
                            //  str_get_user_name=json_Common_Array.getString("commonId");
                            str_get_user_id=json_Common_Array.getString("commonId");
                            str_get_user_iamge=json_Common_Array.getString("profile_pic");
                            session.set_user_details(str_get_user_id,str_get_user_email,"",str_get_user_iamge,"",json_Common_Array.has("signature_image")? json_Common_Array.getString("signature_image"):"");


                            Intent intent = new Intent(Sign_up_page.this, Main_homepage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();

                        }
                        else
                        {
                            Alert("Failure",str_message);
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


    public void Alert_Success(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(Sign_up_page.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                Intent intent = new Intent(Sign_up_page.this, Main_homepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        });

        mDialog.show();
    }





    public void mobile_verification()
    {
        final Dialog dialog = new Dialog(Sign_up_page.this);
        dialog.setContentView(R.layout.mobile_verification);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout Rel_cancel=(RelativeLayout)dialog.findViewById(R.id.rel_close);
        Rel_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();

    }




    public void send_otp()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> post = new HashMap<>();
        post.put("email", Edit_text_email.getText().toString());
        post.put("ph_country", txt_country_code.getText().toString());
        post.put("phone_no",edit_mobile_number.getText().toString());
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
                             verification_oode=response_obj.getString("mobile_verification_code");
                            //save_data("normal");

//                            map.put("firstname", );
//                            map.put("lastname",Edit_Last_Name.getText().toString());
//                            map.put("gcm_key", GCM_Id);
//                            if(edit_mobile_number.getText().toString().trim().length()>0)
//                            {
//                                map.put("phone_no", edit_mobile_number.getText().toString());
//                                map.put("ph_country", txt_country_code.getText().toString());
//                            }
//
//                            if(str_password!=null && !str_password.equals(""))
//                            {
//                                map.put("password", edit_password.getText().toString());
//                            }
//
//                            if(editText_referal_code.getText().toString().trim().length()>0)
//                            {
//                                map.put("referral_code", editText_referal_code.getText().toString());
//                            }
//
//
//
//                            String str_verify = "";
//                            map.put("login_type","normal");
//                            if(str_mode!=null  && !str_mode.equals("") && str_mode.equals("production"))
//                            {
//                                map.put("ph_verified","Yes");
//                                str_msg = "Yes";
//                            }
//                            else
//                            {
//                                map.put("ph_verified","No");
//                                str_msg = "No";
//                            }

                            Intent intent = new Intent(Sign_up_page.this, OTP.class);
                            intent.putExtra("mode",str_mode);
                            intent.putExtra("verification_ocde",verification_oode);

                            //intent.putExtra("user_id",str_get_user_id);
                            intent.putExtra("user_email",Edit_text_email.getText().toString());
                            intent.putExtra("firstname",edit_First_Name.getText().toString());
                            intent.putExtra("lastname",Edit_Last_Name.getText().toString());
                            intent.putExtra("gcm_key",GCM_Id);
                            intent.putExtra("phone_no",edit_mobile_number.getText().toString());
                            intent.putExtra("ph_country",txt_country_code.getText().toString());
                            intent.putExtra("password",edit_password.getText().toString());
                            intent.putExtra("referral_code",editText_referal_code.getText().toString());
                            //intent.putExtra("user_image",str_get_user_iamge);
                            //intent.putExtra("user_password",str_password);

                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
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




    class MyClickableSpan extends ClickableSpan {
        public MyClickableSpan(String string) {
            super();
        }
        public void onClick(View tv) {
            Toast.makeText(Sign_up_page.this, "Thanks for the click!",
                    Toast.LENGTH_SHORT).show();
        }
        public void updateDrawState(TextPaint ds) {

            ds.setColor(getResources().getColor(R.color.app_color_1));
            ds.setUnderlineText(true); // set to false to remove underline
        }
    }


    private void doClickSpanForString(String firstString,
                                      String lastString,String st_and,String st_privacy, TextView txtSpan) {
        txtSpan.setMovementMethod(LinkMovementMethod.getInstance());
        String changeString = (lastString != null ? lastString : "");
        String totalString = firstString + changeString;
        Spannable spanText = new SpannableString(totalString);

        spanText.setSpan(new MyClickableSpan(totalString),
                String.valueOf(firstString).length(),
                totalString.length(), 0);
        txtSpan.setText(spanText);

    }
}
