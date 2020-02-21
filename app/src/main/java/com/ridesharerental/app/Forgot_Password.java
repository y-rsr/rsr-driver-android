package com.ridesharerental.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.devspark.appmsg.AppMsg;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 12/4/2017.
 */

public class Forgot_Password extends Activity
{
    LinearLayout Linear_Back;
    EditText edit_forgot_Email;
    RelativeLayout Rel_submit;
    Common_Loader Loader;
    ConnectionDetector cd;
    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_layout);
        Loader=new Common_Loader(Forgot_Password.this);
        cd = new ConnectionDetector(this);
        init();
        Linear_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                Forgot_Password.this.finish();
            }
        });

        Rel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(edit_forgot_Email.getText().toString().trim().length()>0)
                {
                    if (edit_forgot_Email.getText().toString().matches( "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))
                    {
                        if (cd.isConnectingToInternet())
                        {
                            post_Data();

                        }else {
                            Alert(getResources().getString(R.string.action_no_internet_title),getResources().getString(R.string.action_no_internet_message));
                        }
                    }
                    else
                    {
                        snack_bar(getResources().getString(R.string.notvalid_email),"");
                    }

                }
                else
                {
                    snack_bar(getResources().getString(R.string.enter_email),"");
                }
            }
        });
    }

    public void init()
    {
        Linear_Back=(LinearLayout)findViewById(R.id.lin_back);
        edit_forgot_Email=(EditText)findViewById(R.id.edit_forgot_email);
        Rel_submit=(RelativeLayout)findViewById(R.id.rel_submit);
    }


    //-----------------service---------------------

    public void post_Data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("email", edit_forgot_Email.getText().toString());
        Call<ResponseBody> call = apiService.post_ResetPassword(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();

                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        if (response != null && response.body() != null) {
                            String Str_response = response.body().string();
                            Log.e("----  Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);


                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1"))
                            {
                                Alert(getResources().getString(R.string.action_success),object.getJSONObject("responseArr").getString("msg"));
                            }
                            else
                            {
                                Alert(getResources().getString(R.string.action_opps),object.getJSONObject("responseArr").getString("msg"));
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


    //-----------------------snack bar--------------
    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        // TextView Tv_message = (TextView) view.findViewById(R.id.txt_message);

        Tv_title.setText(title);
        // Tv_message.setText(message);

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(Forgot_Password.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }

    //--------------------------------alert

    public void Alert(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(Forgot_Password.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                  finish();
            }
        });

        mDialog.show();
    }
}
