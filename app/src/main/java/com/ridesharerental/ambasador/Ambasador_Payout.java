package com.ridesharerental.ambasador;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 2/6/2018.
 */

public class Ambasador_Payout extends Fragment {
    private ActionBar actionBar;
    Context context;

    Common_Loader Loader;

    SessionManager sessionManager;
    String user_id = "";

    TextView txt_refereal_code;
    String str_referal_code = "", str_link = "", str_share_link = "";
    RelativeLayout Rel_connect;

    ImageView img_message, img_facebook, img_gmail, img_twitter;
    TextView txt_user_name, txt_password;
    RelativeLayout Rel_referel_code;

    EditText edi_email;

    Button byn_edit, btn_update, btn_cancel;
    LinearLayout linear_update;

    String str_email_match_key = "";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.ambasador_payout, container, false);
        context = getActivity();
        Loader = new Common_Loader(context);

        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(rootView);
        load_payout();

        byn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_update.setVisibility(View.VISIBLE);
                byn_edit.setVisibility(View.GONE);
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_update.setVisibility(View.GONE);
                byn_edit.setVisibility(View.VISIBLE);
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edi_email.getText().toString().trim().length() > 0) {
                    if (edi_email.getText().toString().matches(str_email_match_key)) {
                        byn_edit.setVisibility(View.VISIBLE);
                        linear_update.setVisibility(View.GONE);
                        load_update_id();
                    } else {
                        Alert(context.getResources().getString(R.string.alert_ss), getResources().getString(R.string.enter_valid_email));
                    }

                } else {
                    Alert(context.getResources().getString(R.string.alert_ss), "Enter your paypal email");
                }

            }
        });


        return rootView;
    }


    public void init(View rootview) {
        str_email_match_key = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        edi_email = (EditText) rootview.findViewById(R.id.edit_email);
        byn_edit = (Button) rootview.findViewById(R.id.edit);
        btn_update = (Button) rootview.findViewById(R.id.btn_upadete);
        btn_cancel = (Button) rootview.findViewById(R.id.btn_cancel);
        linear_update = (LinearLayout) rootview.findViewById(R.id.linear_update);
        linear_update.setVisibility(View.GONE);
    }


    public void load_payout() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.ambasador_program(header);
        System.out.println("--------Ambasador Program---->" + call.request().url().toString() + "?commonId=" + user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();

                    if (response.isSuccessful()) {
//                        try {
//                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }


                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);

                        System.out.println("------kannan reponse----------" + Str_response);


                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1")) {
                            str_referal_code = response_obj.getString("referral_code");
                            str_link = response_obj.getString("referral_link");
                            str_share_link = response_obj.getString("share_link");
                            String str_user_name = "", str_password = "";
                            edi_email.setText(response_obj.getString("amb_paypal_id"));

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


    public void load_update_id() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        HashMap<String, String> post = new HashMap<>();
        post.put("amb_paypal_id", edi_email.getText().toString());
        Call<ResponseBody> call = apiService.update_paypal(header, post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
//                    System.out.println("------kannan response two----------" + response.errorBody().string());
                    if (response.isSuccessful()) {
//                        try {
//                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        String Str_response = response.body().string();
//                        Log.e("----Country Response-->", Str_response);
                        System.out.println("----------kannna reponse second----------"+Str_response);


                        JSONObject object = new JSONObject(Str_response);

                        JSONObject resp_ob = object.getJSONObject("responseArr");
                        String status_code = resp_ob.getString("status");
                        String status_msg = resp_ob.getString("msg");
                        if (status_code.equals("1")) {
                            Alert(context.getResources().getString(R.string.action_success), status_msg);
                        } else {
                            Alert(context.getResources().getString(R.string.alert_ss), status_msg);
                        }


                    }
//
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


        Loader.dismiss();
    }


    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(context);
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
