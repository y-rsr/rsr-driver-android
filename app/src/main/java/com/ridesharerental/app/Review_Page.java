package com.ridesharerental.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 1/10/2018.
 */

public class Review_Page extends Activity
{
    RelativeLayout Rel_Back;
    MaterialRatingBar ratingBar;
    TextView txt_rating_count;
    Common_Loader Loader;
    RelativeLayout Rel_Next;
    EditText editText_comment;

    String user_id="",str_booking_no="";
    SessionManager sessionManager;
    int ratig_count=0;
    boolean selected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_layout);
        Loader=new Common_Loader(Review_Page.this);
        sessionManager = new SessionManager(Review_Page.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);
        init();

        Rel_Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
                Review_Page.this.finish();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                ratig_count=math(ratingBar.getRating());
               // String rateValue = String.valueOf(ratingBar.getRating());
               // System.out.println("Rate for Module is"+rateValue);

                if(ratig_count>0)
                {
                    if(ratig_count==1)
                    {
                        txt_rating_count.setText(String.valueOf(ratig_count) +"Star");
                    }
                    else
                    {
                        txt_rating_count.setText(String.valueOf(ratig_count) +"Stars");
                    }
                }

            }
        });


        Rel_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    if(editText_comment.getText().toString().trim().length()>0)
                    {
                        if(ratig_count>0)
                        {
                            add_Review();
                        }
                        else
                        {
                            snack_bar("Choose your rating","");
                        }
                    }
                    else
                    {
                        snack_bar("Enter your review","");
                    }
            }
        });
    }
    public void init()
    {
        Rel_Back=(RelativeLayout)findViewById(R.id.chat_detail_backLAY);
        ratingBar=(MaterialRatingBar)findViewById(R.id.review_adapter_ratingBAR);
        txt_rating_count=(TextView)findViewById(R.id.review_star_count);

        Rel_Next=(RelativeLayout)findViewById(R.id.rel_next);
        editText_comment=(EditText)findViewById(R.id.edit_des);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_booking_no=bundle.getString("booking_no");
        }

        show_review();
    }



    public void add_Review()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> poast = new HashMap<>();
        poast.put("booking_no", str_booking_no);
        poast.put("review_text", editText_comment.getText().toString());
        poast.put("review_count", String.valueOf(ratig_count));

        Call<ResponseBody> call = apiService.add_review(header,poast);
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
                        String str_status_code=response_obj.getString("status");
                        String str_msg=response_obj.getString("msg");
                        if(str_status_code.equals("1"))
                        {
                            snack_bar_success(str_msg,"");
                            editText_comment.setEnabled(false);
                            ratingBar.setEnabled(false);
                            Rel_Next.setEnabled(false);
                        }
                        else
                        {
                            snack_bar(str_msg,"");
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




    public void show_review()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> poast = new HashMap<>();
        poast.put("booking_no", str_booking_no);

        Call<ResponseBody> call = apiService.show_review(header,poast);
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
                        JSONObject respnse_obj=object.getJSONObject("responseArr");
                        String str_status_code=respnse_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            Object intervention = respnse_obj.get("reviews");
                            if (intervention instanceof JSONArray)
                            {
                                // It's an array
                             JSONArray   interventionJsonArray = (JSONArray)intervention;
                                if(interventionJsonArray.length()>0)
                                {

                                    for(int i=0;i<interventionJsonArray.length();i++)
                                    {
                                        JSONObject obj=interventionJsonArray.getJSONObject(i);

                                        String review_count=obj.getString("review_count");
                                        String str_comment=obj.getString("review_text");
                                        editText_comment.setText(str_comment);
                                        ratingBar.setRating(Float.parseFloat(review_count));
                                        if(review_count.equals("1"))
                                        {
                                            txt_rating_count.setText(String.valueOf(review_count) +"Star");
                                        }
                                        else
                                        {
                                            txt_rating_count.setText(String.valueOf(review_count) +"Stars");
                                        }

                                    }

                                    editText_comment.setEnabled(false);
                                    ratingBar.setEnabled(false);
                                    Rel_Next.setEnabled(false);

                                    selected=false;

                                }
                                else
                                {
                                    editText_comment.setEnabled(true);
                                    ratingBar.setEnabled(true);
                                    Rel_Next.setEnabled(true);
                                }

                            }
                            else if (intervention instanceof JSONObject)
                            {
                                // It's an object
                                selected=true;
                                editText_comment.setEnabled(false);
                                ratingBar.setEnabled(false);
                                Rel_Next.setEnabled(false);
                             JSONObject   interventionObject = (JSONObject)intervention;
                                String review_count=interventionObject.getString("review_count");
                                String str_comment=interventionObject.getString("review_text");
                                editText_comment.setText(str_comment);
                                ratingBar.setRating(Float.parseFloat(review_count));
                                if(review_count.equals("1"))
                                {
                                    txt_rating_count.setText(String.valueOf(review_count) +"Star");
                                }
                                else
                                {
                                    txt_rating_count.setText(String.valueOf(review_count) +"Stars");
                                }

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

    public  int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
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
        AppMsg snack = AppMsg.makeText(Review_Page.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }



    //-----------------------snack bar--------------
    private void snack_bar_success(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.success_snack, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        // TextView Tv_message = (TextView) view.findViewById(R.id.txt_message);

        Tv_title.setText(title);
        // Tv_message.setText(message);

        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.app_color_1);
        AppMsg snack = AppMsg.makeText(Review_Page.this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.PRIORITY_HIGH);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }

}
