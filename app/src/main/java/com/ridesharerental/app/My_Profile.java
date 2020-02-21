package com.ridesharerental.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.Common_Loader;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 11/29/2017.
 */

public class My_Profile extends Fragment implements View.OnClickListener
{
    private ActionBar actionBar;
    private Button MyEdit_profileBTN;
    Context context;
    Common_Loader Loader;
    CircleImageView profile_image;
    ImageView license_img;
    TextView txt_host_name,txt_host_email,txt_contact,txt_city,txt_state_view;
    Button btn_edit_profile;
    TextView txt_dob,txt_address,txt_zip_code,txt_license_number,txt_expire_date,txt_state,txt_background_verification;
    String str_first_name="",str_last_Name="",str_email="",str_contact_no="",str_profile_Image="",str_date_of_borth="",str_address="",
    str_zip_code="",str_license_number="",str_expiration_date="",str_city = "",str_state="",str_background_check="",str_license_img;


    SessionManager sessionManager;
    String str_user_id="";

    LinearLayout linear_empty;

    TextView txt_apt_no;
    String str_apt_no="",str_license_state="";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.my_profile_layout, container, false);
        context=getActivity();
        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        str_user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("-----CommonId-------->"+str_user_id);
        Loader=new Common_Loader(context);
        init(rootView);



        return rootView;
    }


    private void init(View rootView)
    {


        linear_empty=(LinearLayout)rootView.findViewById(R.id.linear_empty_data) ;
        linear_empty.setVisibility(View.VISIBLE);

        MyEdit_profileBTN = (Button)rootView.findViewById(R.id.my_profile_layout_editProfile);
        MyEdit_profileBTN.setOnClickListener(this);

        txt_apt_no=(TextView)rootView.findViewById(R.id.txt_apt_no);

        profile_image=(CircleImageView)rootView.findViewById(R.id.my_profile_layout_imageview);
        license_img=(ImageView)rootView.findViewById(R.id.img_document);
        txt_host_name=(TextView)rootView.findViewById(R.id.my_profile_layout_name);
        txt_host_email=(TextView)rootView.findViewById(R.id.my_profile_layout_mailID);
        txt_contact=(TextView)rootView.findViewById(R.id.my_profile_layout_phnumber);
        btn_edit_profile=(Button)rootView.findViewById(R.id.my_profile_layout_editProfile);

        txt_dob=(TextView)rootView.findViewById(R.id.txt_dob);
        txt_address=(TextView)rootView.findViewById(R.id.txt_address);
        txt_zip_code=(TextView)rootView.findViewById(R.id.txt_zip_code);
        txt_license_number=(TextView)rootView.findViewById(R.id.txt_license_number);
        txt_expire_date=(TextView)rootView.findViewById(R.id.txt_expire_number);
        txt_state=(TextView)rootView.findViewById(R.id.txt_state);
        txt_state_view=(TextView)rootView.findViewById(R.id.txt_state_view);
        txt_city=(TextView)rootView.findViewById(R.id.txt_city);
        txt_background_verification=(TextView)rootView.findViewById(R.id.txt_background_verification);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.my_profile_layout_editProfile:
                Intent val = new Intent(getActivity(),My_Edit_profile.class);
                startActivityForResult(val,200);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }

    }


    //-----------------service---------------------
    public void Load_Data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", str_user_id);
        Call<ResponseBody> call = apiService.show_driver_profile(header);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful())
                    {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        Log.e("----profile Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        JSONObject common_obj=object.getJSONObject("commonArr");
                        String status_code=response_obj.getString("status");
                        if(status_code!=null && !status_code.equals("") && status_code.equals("1"))
                        {
                            linear_empty.setVisibility(View.GONE);
                          JSONObject driver_detail_obj=response_obj.getJSONObject("driverDetails");
                            str_first_name=driver_detail_obj.getString("firstname");
                            str_last_Name=driver_detail_obj.getString("lastname");
                            str_profile_Image=driver_detail_obj.getString("profile_pic");
                            if(driver_detail_obj.getString("profile_pic")!=null && !driver_detail_obj.getString("profile_pic").equals(""))
                            {
                                Log.e("profile_pic",driver_detail_obj.getString("profile_pic"));
                                sessionManager.set_profile(driver_detail_obj.getString("profile_pic"));
                                Picasso.with(getActivity())
                                        .load(str_profile_Image)
                                        .placeholder(getResources().getDrawable(R.drawable.icn_profile))
                                        .into(profile_image);
                            }

                            str_contact_no=driver_detail_obj.getString("phone_no");

                            str_date_of_borth=driver_detail_obj.getString("birthday");
                            str_apt_no=driver_detail_obj.getString("apt_no");

                            str_license_state=driver_detail_obj.getString("licence_state");

                            str_address=driver_detail_obj.getString("address");
                            str_zip_code=driver_detail_obj.getString("zip");
                            str_license_number=driver_detail_obj.getString("licence_number");
                            str_expiration_date=driver_detail_obj.getString("licence_exp_date");
                            str_state=driver_detail_obj.getString("state");
                            str_city=driver_detail_obj.getString("state");
                            str_license_img=driver_detail_obj.getString("licence_image");
                            str_background_check=driver_detail_obj.getString("background_check");



                            txt_host_name.setText(str_first_name+" "+str_last_Name);
                            txt_host_email.setText(str_email);
                            txt_contact.setText(str_contact_no);
                            txt_city.setText(str_contact_no);
                            txt_state_view.setText(str_state);

                            if(str_apt_no!=null && !str_apt_no.equals(""))
                            {
                                txt_apt_no.setText(str_apt_no);
                            }
                            else
                            {
                                txt_apt_no.setText(context.getResources().getString(R.string.no_data));
                            }


                            if(str_date_of_borth!=null && !str_date_of_borth.equals("") && !str_date_of_borth.equals("0000-00-00"))
                            {
                                txt_dob.setText(str_date_of_borth);
                            }
                            else
                            {
                                txt_dob.setText(context.getResources().getString(R.string.no_data));
                            }

                            if(str_address!=null && !str_address.equals(""))
                            {
                                txt_address.setText(str_address);
                            }
                            else
                            {
                                txt_address.setText(context.getResources().getString(R.string.no_data));
                            }

                            if(str_zip_code!=null && !str_zip_code.equals(""))
                            {
                                txt_zip_code.setText(str_zip_code);
                            }
                            else
                            {
                                txt_zip_code.setText(context.getResources().getString(R.string.no_data));
                            }
                            if(str_license_number!=null && !str_license_number.equals(""))
                            {
                                txt_license_number.setText(str_license_number);
                            }
                            else
                            {
                                txt_license_number.setText(context.getResources().getString(R.string.no_data));
                            }

                            if(str_expiration_date!=null && !str_expiration_date.equals("") && !str_date_of_borth.equals("0000-00-00") )
                            {
                                txt_expire_date.setText(str_expiration_date);
                            }
                            else
                            {
                                txt_expire_date.setText(context.getResources().getString(R.string.no_data));
                            }

                            if(str_license_state!=null && !str_license_state.equals(""))
                            {
                                txt_state.setText(str_license_state);
                            }
                            else
                            {
                                txt_state.setText(context.getResources().getString(R.string.no_data));
                            }



                            if(str_background_check!=null && !str_background_check.equals(""))
                            {
                                if(str_background_check.equalsIgnoreCase("Inactive") || str_background_check.equalsIgnoreCase("Waiting"))
                                {
                                    txt_background_verification.setText(context.getResources().getString(R.string.waiting));
                                    txt_background_verification.setTextColor(getResources().getColor(R.color.red));
                                }
                                else
                                {
                                    txt_background_verification.setText(context.getResources().getString(R.string.ready));
                                    txt_background_verification.setTextColor(getResources().getColor(R.color.change_green));
                                }

                            }
                            else
                            {
                                txt_background_verification.setText(context.getResources().getString(R.string.waiting));
                            }


                            if(str_license_img!=null && !str_license_img.equals(""))
                            {
                                Picasso.with(context)
                                        .load(str_license_img)
                                        .placeholder(R.drawable.icn_profile)
                                        //.error(R.drawable.icn_profile)         // optional
                                        .into(license_img);
                            }

                            str_email=common_obj.getString("email");
                            txt_host_email.setText(str_email);
                        }

                    }
                    else
                    {
                        linear_empty.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    linear_empty.setVisibility(View.VISIBLE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == 200)
            {
                String first_name = data.getStringExtra("first_name");
                String last_name = data.getStringExtra("location_name");
                txt_host_name.setText(first_name+" "+last_name);
            }
         }
    }

    @Override
    public void onResume() {
        super.onResume();
        Load_Data();
    }
}
