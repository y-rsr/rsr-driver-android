package com.ridesharerental.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.adapter.LeaveReviewAdapater;
import com.ridesharerental.pojo.LeaveReviewPojo;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user129 on 10/1/2018.
 */
public class LeaveReview extends Fragment {

    private ActionBar actionBar;
    private TextView Tv_no_review;
    Context context;
    private ListView reviewList;
    Common_Loader Loader;
    SessionManager sessionManager;
    private SwipeRefreshLayout refresh;
    String user_id = "";
    ArrayList<LeaveReviewPojo> review_list;
    private Boolean isReviewAvilable = false;
    private LeaveReviewAdapater adapter;
    ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    String rating_count = "0";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.leave_review, container, false);
        context = getActivity();


        initization(rootView);


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                cd = new ConnectionDetector(getActivity());
                isInternetPresent = cd.isConnectingToInternet();

                if (isInternetPresent) {

                    LeaveReviewApiRequestService();

                } else {

                    Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
                }


            }
        });


        return rootView;
    }


    private void initization(View rootView) {

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        Loader = new Common_Loader(getActivity());
        sessionManager = new SessionManager(context);
        review_list = new ArrayList<LeaveReviewPojo>();
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);

        reviewList = (ListView) rootView.findViewById(R.id.leave_review_list);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        Tv_no_review = (TextView) rootView.findViewById(R.id.Tv_no_review);


        if (isInternetPresent) {

            LeaveReviewApiRequestService();

        } else {

            Alert(getResources().getString(R.string.alert_label_title), getResources().getString(R.string.alert_nointernet));
        }


    }


    private void LeaveReviewApiRequestService() {

        Loader.show();

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.leave_review(header);

        System.out.println("-------leave review url---------" + call.request().url().toString());
        System.out.println("-----------leave review url------>"+call.request().url().toString()+"?commonId="+user_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String Sstatus = "", Smsg = "";

                try {

                    if (response.isSuccessful() && response.code() == 200) {

                        String Str_response = response.body().string();

                        System.out.println("---------leave review-Response------------" + Str_response);


                        JSONObject object = new JSONObject(Str_response);

                        JSONObject resobject = object.getJSONObject("responseArr");

                        Sstatus = resobject.getString("status");

                        if (Sstatus.equalsIgnoreCase("1")) {

                            review_list.clear();
                            Object check_review_object = resobject.get("active_reservations");

                            if (check_review_object instanceof JSONArray) {

                                JSONArray review_arry = resobject.getJSONArray("active_reservations");


                                if (review_arry.length() > 0) {



                                    for (int i = 0; i < review_arry.length(); i++) {

                                        JSONObject jobj = review_arry.getJSONObject(i);
                                        LeaveReviewPojo pojo = new LeaveReviewPojo();

                                        pojo.setCarId(jobj.getString("carId"));
                                        pojo.setPlat_no(jobj.getString("plat_no"));
                                        pojo.setVin_no(jobj.getString("vin_no"));
                                        pojo.setV_no(jobj.getString("v_no"));
                                        pojo.setNotes(jobj.getString("notes"));
                                        pojo.setStreet(jobj.getString("street"));
                                        pojo.setCity(jobj.getString("city"));
                                        pojo.setState(jobj.getString("state"));
                                        if(jobj.has("rc"))
                                            pojo.setRc(jobj.getString("rc"));
                                        if(jobj.has("lift_uber"))
                                            pojo.setLift_uber(jobj.getString("lift_uber"));
                                        if(jobj.has("ins"))
                                            pojo.setIns(jobj.getString("ins"));
                                        pojo.setRent_daily(jobj.getString("rent_daily"));
                                        pojo.setRent_weekly(jobj.getString("rent_weekly"));
                                        pojo.setRent_monthly(jobj.getString("rent_monthly"));
                                        pojo.setYear(jobj.getString("year"));
                                        pojo.setCar_make(jobj.getString("car_make"));
                                        pojo.setCar_model(jobj.getString("car_model"));
                                        pojo.setOwnername(jobj.getString("ownername"));
                                        pojo.setEmail(jobj.getString("email"));
                                        pojo.setPhone_no(jobj.getString("phone_no"));
                                        pojo.setDate_from(jobj.getString("date_from"));
                                        pojo.setDate_to(jobj.getString("date_to"));
                                        pojo.setExtended_date(jobj.getString("extended_date"));
                                        pojo.setNo_of_days(jobj.getString("no_of_days"));
                                        pojo.setDeposit(jobj.getString("deposit"));
                                        pojo.setTotal_amount(jobj.getString("total_amount"));
                                        pojo.setBooking_no(jobj.getString("booking_no"));
                                        if(jobj.has("insurance_doc"))
                                            pojo.setInsurance_doc(jobj.getString("insurance_doc"));
                                        pojo.setDateAdded(jobj.getString("dateAdded"));
                                        pojo.setAllow_extend(jobj.getString("allow_extend"));
                                        pojo.setExtended(jobj.getString("extended"));
                                        pojo.setTimer_date(jobj.getString("timer_date"));
                                        pojo.setStatus(jobj.getString("status"));
                                        if(jobj.has("similar_cars"))
                                            pojo.setSimilar_cars(jobj.getString("similar_cars"));
                                        pojo.setCar_image(jobj.getString("car_image"));


                                        review_list.add(pojo);


                                    }


                                    isReviewAvilable = true;

                                } else {



                                }


                            }


                            JSONArray review_arry = resobject.getJSONArray("past_reservations");
                            {
                                if (review_arry.length() > 0) {

                                    for (int i = 0; i < review_arry.length(); i++) {

                                        JSONObject jobj = review_arry.getJSONObject(i);
                                        LeaveReviewPojo pojo = new LeaveReviewPojo();

                                        pojo.setCarId(jobj.getString("carId"));
                                        pojo.setPlat_no(jobj.getString("plat_no"));
                                        pojo.setVin_no(jobj.getString("vin_no"));
                                        pojo.setV_no(jobj.getString("v_no"));
                                        pojo.setNotes(jobj.getString("notes"));
                                        pojo.setStreet(jobj.getString("street"));
                                        pojo.setCity(jobj.getString("city"));
                                        pojo.setState(jobj.getString("state"));
                                        if(jobj.has("rc"))
                                            pojo.setRc(jobj.getString("rc"));
                                        if(jobj.has("lift_uber"))
                                            pojo.setLift_uber(jobj.getString("lift_uber"));
                                        if(jobj.has("ins"))
                                            pojo.setIns(jobj.getString("ins"));
                                        pojo.setRent_daily(jobj.getString("rent_daily"));
                                        pojo.setRent_weekly(jobj.getString("rent_weekly"));
                                        pojo.setRent_monthly(jobj.getString("rent_monthly"));
                                        pojo.setYear(jobj.getString("year"));
                                        pojo.setCar_make(jobj.getString("car_make"));
                                        pojo.setCar_model(jobj.getString("car_model"));
                                        pojo.setOwnername(jobj.getString("ownername"));
                                        pojo.setEmail(jobj.getString("email"));
                                        pojo.setPhone_no(jobj.getString("phone_no"));
                                        pojo.setDate_from(jobj.getString("date_from"));
                                        pojo.setDate_to(jobj.getString("date_to"));
                                        pojo.setExtended_date(jobj.getString("extended_date"));
                                        pojo.setNo_of_days(jobj.getString("no_of_days"));
                                        pojo.setDeposit(jobj.getString("deposit"));
                                        pojo.setTotal_amount(jobj.getString("total_amount"));
                                        pojo.setBooking_no(jobj.getString("booking_no"));
                                        if(jobj.has("insurance_doc"))
                                            pojo.setInsurance_doc(jobj.getString("insurance_doc"));
                                        pojo.setDateAdded(jobj.getString("dateAdded"));
                                        pojo.setAllow_extend(jobj.getString("allow_extend"));
                                        pojo.setExtended(jobj.getString("extended"));
                                        pojo.setTimer_date(jobj.getString("timer_date"));
                                        pojo.setStatus(jobj.getString("status"));
                                        if(jobj.has("similar_cars"))
                                            pojo.setSimilar_cars(jobj.getString("similar_cars"));
                                        pojo.setCar_image(jobj.getString("car_image"));


                                        review_list.add(pojo);


                                    }


                                    isReviewAvilable = true;

                                } else {

                                    //isReviewAvilable = false;

                                }
                            }


                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                Loader.dismiss();


//                ------------------post-------------------

                if (Sstatus.equalsIgnoreCase("1")) {

                    if (isReviewAvilable) {

                        Tv_no_review.setVisibility(View.GONE);
                        reviewList.setVisibility(View.VISIBLE);

                        adapter = new LeaveReviewAdapater(getActivity(), review_list);
                        reviewList.setAdapter(adapter);
                        PerformClickEvent();

                    } else {

                        Tv_no_review.setVisibility(View.VISIBLE);
                        reviewList.setVisibility(View.GONE);

                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Loader.dismiss();

            }
        });


        if (refresh.isRefreshing()) {

            refresh.setRefreshing(false);
        }


    }


    private void PerformClickEvent() {


        adapter.ItemClickPerformance(new LeaveReviewAdapater.ReviewClickListner() {
            @Override
            public void ItemClickListner(int pos, String BookingId, String CarId, String title) {

                AddReviewDialog(pos, BookingId, CarId, title);

            }
        });


    }


    private void AddReviewDialog(int pos, final String bookingId, final String carId, String title) {


        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.leave_review_dialog);

        RelativeLayout close = (RelativeLayout) dialog.findViewById(R.id.rl_close);
        final TextView Tv_info = (TextView) dialog.findViewById(R.id.Tv_info);
        final EditText Ed_review_txt = (EditText) dialog.findViewById(R.id.Ed_review_txt);
        RatingBar Rb_ratingbar = (RatingBar) dialog.findViewById(R.id.Rb_ratingbar);
        RelativeLayout Rl_submit = (RelativeLayout) dialog.findViewById(R.id.Rl_submit);
        final TextView star_count = (TextView) dialog.findViewById(R.id.star_count);


        Tv_info.setText(title);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        Rb_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {

                System.out.println("rating position kannan " +/*String.valueOf(rating)*/String.format("%.0f", rating));

                rating_count = String.format("%.0f", rating);

                star_count.setText(String.format("%.0f", rating)+" "+"Star");


            }
        });


        Rl_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Ed_review_txt.getText().toString().length() == 0) {

                    Toastdisplay("Enter your review");

                } else if (rating_count.equalsIgnoreCase("0")) {

                    Toastdisplay("Choose your rating");

                } else {

                    dialog.dismiss();
                    System.out.println("------booking id---------" + bookingId);
                    System.out.println("------Edit_text---------" + Ed_review_txt.getText().toString());
                    System.out.println("------star---------" + rating_count);

                    add_Review(bookingId, Ed_review_txt.getText().toString(), rating_count);


                }


            }
        });


        dialog.show();

    }


    public void add_Review(String booking_no, String comment, String rating_star) {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> poast = new HashMap<>();
        poast.put("booking_no", booking_no);
        poast.put("review_text", comment);
        poast.put("review_count", rating_star);

        Call<ResponseBody> call = apiService.add_review(header, poast);
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
                        String str_status_code = response_obj.getString("status");
                        String str_msg = response_obj.getString("msg");
                        if (str_status_code.equals("1")) {

                            LeaveReviewApiRequestService();
                            Toastdisplay("Review Updated");

//                            editText_comment.setEnabled(false);
//                            ratingBar.setEnabled(false);
//                            Rel_Next.setEnabled(false);
                        } else {
                            Toastdisplay(str_msg);
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




    private void Toastdisplay(String txt){

        Toast.makeText(getActivity(),txt,Toast.LENGTH_SHORT).show();

    }




    //--------------Alert Method-----------
    private void Alert(String title, String alert) {
        final PkDialog mDialog = new PkDialog(getActivity());
        mDialog.setDialogTitle(title);
        mDialog.setDialogMessage(alert);
        mDialog.setPositiveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

}
