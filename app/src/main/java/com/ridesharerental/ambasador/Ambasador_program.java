package com.ridesharerental.ambasador;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.app.R;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.SimpleSpanBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by user65 on 1/19/2018.
 */

public class Ambasador_program extends Fragment
{
    private ActionBar actionBar;
    Context context;

    Common_Loader Loader;

    SessionManager sessionManager;
    String user_id = "";

    TextView txt_refereal_code;
    String str_referal_code="",str_link="",str_share_link="";
    RelativeLayout Rel_connect;

    ImageView img_message,img_facebook,img_gmail,img_twitter;
    TextView txt_user_name,txt_password;
    RelativeLayout Rel_referel_code;
    TextView txt_rank;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.ambasador_program, container, false);


        context=getActivity();
        Loader=new Common_Loader(context);

        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);

        init(rootView);
        load_data();



        Rel_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(str_link!=null && !str_link.equals(""))
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(str_link));
                    startActivity(i);
                }

            }
        });

        img_message.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               /* Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", "Ride share");
                startActivity(sendIntent);*/
               // sendSMS();
               // send_email();
                //dummy();
                send_email_id();
            }
        });

        img_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                send_Facebook();
            }
        });


        img_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent shareIntent = new PlusShare.Builder(context)
                        .setType("text/plain")
                        .setText("Ride Share")
                        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                        .getIntent();

                startActivityForResult(shareIntent, 0);*/

                sendShareTwit();
            }
        });


        img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShareTwit();
            }
        });


        Rel_referel_code.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if(str_link!=null && !str_link.equals(""))
                {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_link));
                    startActivity(browserIntent);
                }

            }
        });

        return rootView;
    }


    public void init(View rootView)
    {
        txt_refereal_code=(TextView)rootView.findViewById(R.id.txt_refere_code);
        Rel_connect=(RelativeLayout)rootView.findViewById(R.id.rel_connect_link);

        img_message=(ImageView)rootView.findViewById(R.id.img_message);
        img_facebook=(ImageView)rootView.findViewById(R.id.img_fb);
        img_gmail=(ImageView)rootView.findViewById(R.id.img_gmail);
        img_twitter=(ImageView)rootView.findViewById(R.id.img_twitter);

        txt_user_name=(TextView)rootView.findViewById(R.id.user_name);
        txt_password=(TextView)rootView.findViewById(R.id.txt_password);

        Rel_referel_code=(RelativeLayout)rootView.findViewById(R.id.rel_click_here);

        txt_rank=(TextView)rootView.findViewById(R.id.txt_rank);
    }



    public void load_data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.ambasador_program(header);
        System.out.println("--------Ambasador Program---->"+call.request().url().toString()+"?commonId="+user_id);
        call.enqueue(new Callback<ResponseBody>()
        {
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
                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            str_referal_code=response_obj.getString("referral_code");
                            str_link=response_obj.getString("referral_link");
                            str_share_link=response_obj.getString("share_link");
                            String str_user_name="",str_password="";

                            txt_user_name.setText("User Name : "+response_obj.getString("uname"));
                            txt_password.setText("Password : "+response_obj.getString("pwd"));
                            txt_rank.setText(response_obj.getString("rank"));

                           // txt_refereal_code.setText(str_referal_code);

                            SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                            ssb.appendWithSpace(context.getResources().getString(R.string.ambasador_pgm));
                            ssb.append(str_referal_code,new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),new RelativeSizeSpan(1.1f));
                            txt_refereal_code.setText(ssb.build());
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




    private void sendSMS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            if(str_referal_code!=null && !str_referal_code.equals(""))
            {
                sendIntent.putExtra(Intent.EXTRA_TEXT, str_referal_code);
            }
            else
            {
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            }

           // sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address","phoneNumber");
            smsIntent.putExtra("sms_body","message");
            startActivity(smsIntent);
        }
    }

    private void send_Facebook()
    {
        // String urlToShare = "https://stackoverflow.com/questions/7545254";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if(str_share_link!=null && !str_share_link.equals(""))
        {
            intent.putExtra(Intent.EXTRA_TEXT, str_share_link);
        }
        else
        {
            intent.putExtra(Intent.EXTRA_TEXT, "");
        }

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana"))
            {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
            else
            {
              //  Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
            }
        }
// As fallback, launch sharer.php in a browser
        if (!facebookAppFound)
        {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + "Ride share";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        else
        {
           // Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
        }
        if(facebookAppFound==true)
        {
            startActivity(intent);
        }
        else
        {
            Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
        }


    }


    private void sendShareTwit()
    {
        String msg = "";
        Uri uri = Uri
                .parse("android.resource://com.code2care.example.sharetextandimagetwitter/drawable/mona");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if(str_share_link!=null && !str_share_link.equals(""))
        {
            intent.putExtra(Intent.EXTRA_TEXT, str_share_link);
        }
        else
        {
            intent.putExtra(Intent.EXTRA_TEXT, "");
        }

        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
        boolean facebookAppFound = false;

        List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter.android"))
            {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
            else
            {
                //  Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
            }
        }
        if (facebookAppFound==true)
        {
            startActivity(intent);
        }
        else {
            Toast.makeText(context,"Install twitter application in your mobile",Toast.LENGTH_SHORT).show();
        }
       // intent.putExtra(Intent.EXTRA_STREAM, uri);
       // intent.setType("image/jpeg");
       // intent.setPackage("com.twitter.android");

    }


    public void send_email()
    {


       /* if(str_referal_code!=null && !str_referal_code.equals(""))
        {
            emailIntent.putExtra(Intent.EXTRA_TEXT, str_referal_code);
            startActivity(Intent.createChooser(emailIntent, str_referal_code));
        }
        else
        {
            startActivity(Intent.createChooser(emailIntent, ""));
        }*/

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Referal Code ");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(str_share_link));
        if(str_share_link!=null && !str_share_link.equals(""))
        {
            intent.putExtra(Intent.EXTRA_TEXT, str_share_link);
        }
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        Intent mailer = Intent.createChooser(intent, null);
        startActivity(mailer);

    }


    public  void dummy()
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(str_share_link));
        startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }


    public void send_email_id()
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.share_email_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout Rel_close = (RelativeLayout) dialog.findViewById(R.id.rel_close);
        RelativeLayout Rel_send = (RelativeLayout) dialog.findViewById(R.id.rel_send);
        final EditText edit_email=(EditText)dialog.findViewById(R.id.edit_email);
        final String str_email_match_key = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Rel_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(edit_email.getText().toString().trim().length()>0)
                {
                    if(edit_email.getText().toString().matches(str_email_match_key))
                    {
                        dialog.dismiss();
                        share_emial_link(edit_email.getText().toString());
                    }
                    else
                    {
                        snack_bar(context.getResources().getString(R.string.enter_vali_email),"");
                    }
                }
                else
                {
                    snack_bar(context.getResources().getString(R.string.enter_email_addre),"");
                }
            }
        });


        Rel_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }




    public void share_emial_link(String email_id)
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        HashMap<String, String> post = new HashMap<>();
        post.put("mailids", email_id);

        Call<ResponseBody> call = apiService.share_link(header,post);
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
                        JSONObject resp_object=object.getJSONObject("responseArr");
                        String str_status_code=resp_object.getString("status");
                        String msg=resp_object.getString("msg");
                        if(str_status_code.equals("1"))
                        {
                            snack_bar_success(msg,"");
                        }
                        else
                        {
                            snack_bar(msg,"");
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
        snack.setDuration(600);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }


    //-----------------------snack bar--------------
    private void snack_bar_success(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.snack_success_new, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        // TextView Tv_message = (TextView) view.findViewById(R.id.txt_message);

        Tv_title.setText(title);
        // Tv_message.setText(message);

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.app_color_1);
        AppMsg snack = AppMsg.makeText(getActivity(), msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }

}
