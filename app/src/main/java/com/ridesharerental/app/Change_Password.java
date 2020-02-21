package com.ridesharerental.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by user65 on 11/29/2017.
 */

public class Change_Password extends Fragment
{
    private ActionBar actionBar;
    Context context;
    Common_Loader Loader;
    EditText edit_current_password,edit_new_password,edit_confirm_password;
    RelativeLayout Rel_next;
    SessionManager sessionManager;
    HashMap<String, String> details;
    ConnectionDetector cd;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.change_password_layout, container, false);
        context=getActivity();
        Loader=new Common_Loader(context);
        cd = new ConnectionDetector(context);
        sessionManager = new SessionManager(context);
        init(rootView);



        Rel_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            if(edit_current_password.getText().toString().trim().length()>0)
            {
                if(edit_new_password.getText().toString().trim().length()>0)
                {

                    if (edit_new_password.getText().toString().trim().length()>=6)

                    {
                        if(edit_confirm_password.getText().toString().length()>0)
                        {


                            if(edit_new_password.getText().toString().equals(edit_confirm_password.getText().toString()))
                            {


                                if(edit_current_password.getText().toString().equalsIgnoreCase(details.get(sessionManager.KEY_USER_PASSWORD)))
                                {
                                   if (!edit_current_password.getText().toString().equalsIgnoreCase(edit_new_password.getText().toString()))
                                   {
                                       if (cd.isConnectingToInternet())
                                       {
                                           ChangePassword();
                                       }else {
                                           Alert(getResources().getString(R.string.action_no_internet_title),getResources().getString(R.string.action_no_internet_message));
                                       }

                                   }
                                   else {
                                       //---------------current password and new password are same
                                       snack_bar(getResources().getString(R.string.current_new_pawrds_equal),"");
                                   }


                                }else {
                                    //---------------current password is wrong
                                    snack_bar(getResources().getString(R.string.current_paswrd_wrng),"");
                                }
                            }
                            else
                            {
                                //---------------new password and confirm password shoild be same
                                snack_bar(getResources().getString(R.string.pasword_wrong),"");
                            }
                        }
                        else
                        {
                            //------------enter confrim password
                            snack_bar(getResources().getString(R.string.confirm_paswrd),"");
                        }
                    } else
                    {
                        //------------enter new password above length 6 or equal to 6
                        snack_bar(getResources().getString(R.string.validnew_paswrd),"");
                    }

                }
                else
                {
                    //------------------enter new password
                    snack_bar(getResources().getString(R.string.new_paswrd),"");
                }
            }
            else
            {
                //----------------------enter current password
                snack_bar(getResources().getString(R.string.current_paswrd),"");
            }
            }
        });

        return rootView;
    }



    public void init(View rootView)
    {
        details = sessionManager.getUserDetails();
        edit_current_password=(EditText)rootView.findViewById(R.id.edit_curren_pass);
        edit_new_password=(EditText)rootView.findViewById(R.id.edit_new_pass);
        edit_confirm_password=(EditText)rootView.findViewById(R.id.edi_confirm_pass);
        Rel_next=(RelativeLayout)rootView.findViewById(R.id.rel_next);
    }



    //-----------------service---------------------
    public void ChangePassword()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", details.get(sessionManager.KEY_USER_ID));

        HashMap<String, String> map = new HashMap<>();
        map.put("password", edit_current_password.getText().toString());
        map.put("new_password", edit_new_password.getText().toString());
        Call<ResponseBody> call = apiService.post_changPassword(header,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("------success----------");
                    if (!response.isSuccessful())
                    {
                        try {

                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());


                            Loader.dismiss();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {

                        if (response != null && response.body() != null)
                        {
                            String Str_response = response.body().string();
                            Log.e("---- Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);

                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1"))
                            {

                                Log.e("-Success--- Response-->", Str_response);

                                JSONObject json_Common_Array=object.getJSONObject("commonArr");


                                sessionManager.set_user_details(json_Common_Array.getString("commonId"),json_Common_Array.getString("email"),"",json_Common_Array.getString("profile_pic"),edit_new_password.getText().toString(),json_Common_Array.has("signature_image")? json_Common_Array.getString("signature_image"):"");

                                if (object.getJSONObject("responseArr").getString("msg")!=null){
                                    Alert(getResources().getString(R.string.action_success),object.getJSONObject("responseArr").getString("msg"));

                                }
                            }
                            else
                            {

                                Log.e("-Failure--- Response-->", Str_response);
                                if (object.getJSONObject("responseArr").getString("msg")!=null)
                                {
                                    Alert(getResources().getString(R.string.action_opps),object.getJSONObject("responseArr").getString("msg"));

                                }
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
    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar_new, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(getActivity(), msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.LENGTH_SHORT);
       // snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }



    public void Alert(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(context);
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
}
