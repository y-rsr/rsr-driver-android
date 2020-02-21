package com.ridesharerental.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Line;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ridesharerental.adapter.DetailPageImage_Adapter;
import com.ridesharerental.adapter.FeatureCarDetailPage_Adapter;
import com.ridesharerental.adapter.Review_Adapter;
import com.ridesharerental.pojo.FeaturePojo;
import com.ridesharerental.pojo.ReviewPojo;
import com.ridesharerental.pojo.price_dedution_pojo;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.CircleImageView;
import com.ridesharerental.widgets.CirclePageIndicator;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightListView;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.google.ads.AdRequest.LOGTAG;

import java.util.Iterator;
import java.util.Set;

public class CarDetailPage extends AppCompatActivity {

    private ExpandableHeightListView myReviewList;
    private RecyclerView myRecycleList;
    private ArrayList<ReviewPojo> myreviewARR;
    private RelativeLayout Rel_Back, Rel_Book,Rl_features;
    private Common_Loader Loader;
    String pos = "";

    private ArrayList<FeaturePojo> myfeatureARR;
    private ArrayList<String> myCarImages;
    private TextView txt_action_title, txt_action_title_1;
    ImageView img_share, img_host_profile, imgFeatureleft, imgFeatureRight;

    TextView txt_car_title, txt_per_day, txt_per_week, txt_per_month, txt_host_name, txt_cardescripition,txt_week_offer,txt_monthly_offer;
    TextView txt_responsse_time, txt_response_rating, txt_total_car_rent, txt_mileague, txt_owner_approval;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private ViewPager myViewPager;
    private CirclePageIndicator carImage_CirclePageIndicator;
    int str_mileage = 0;
    private String CarId = "", latitude = "", longitude = "", Str_minumu_sty = "";
    String str_get_check_in = "", str_get_check_out = "", str_get_wish_list = "";
    LinearLayoutManager horizontalLayoutManagaer;
    ArrayList<price_dedution_pojo> deduction_list;

    RatingBar rating;
    TextView txt_vin_Number;
    //TextView txt_notes;
    CircleImageView img_circle_profile;
    LikeButton like_wish_List;
    RelativeLayout month_offer_ll,week_offer_ll;


    SessionManager sessionManager;
    String user_id = "";
    String shareUrl = "";

    LinearLayout linear_empty;
    LinearLayout Rel_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        Loader = new Common_Loader(CarDetailPage.this);

        sessionManager = new SessionManager(CarDetailPage.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            CarId = bundle.getString("CarId");
            str_get_check_in = getIntent().hasExtra("check_in")?bundle.getString("check_in"):"";
            str_get_check_out = getIntent().hasExtra("check_out")?bundle.getString("check_out"):"";
            str_get_wish_list = bundle.getString("wishlist");
            pos = getIntent().hasExtra("position")?bundle.getString("position"):"0";

        }
        init();


        ///---------------------------- on click listener function

        Rel_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                CarDetailPage.this.finish();
            }
        });


        Rel_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_intent = new Intent(CarDetailPage.this, Booking_Step1.class);
                new_intent.putParcelableArrayListExtra("deductionarry",deduction_list);
                new_intent.putExtra("Car_Name", txt_action_title.getText().toString());
                new_intent.putExtra("Car_HostName", txt_action_title_1.getText().toString());
                new_intent.putExtra("Car_Day", txt_per_day.getText().toString());
                new_intent.putExtra("Car_Week", txt_per_week.getText().toString());
                new_intent.putExtra("Car_Month", txt_per_month.getText().toString());
                new_intent.putExtra("CarId", CarId);
                new_intent.putExtra("minumu_stay", Str_minumu_sty);
                if (str_get_check_in != null && !str_get_check_in.equals("")) {
                    new_intent.putExtra("check_in", str_get_check_in);
                }
                if (str_get_check_out != null && !str_get_check_out.equals("")) {
                    new_intent.putExtra("check_out", str_get_check_out);
                }

                startActivity(new_intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

//        img_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
//                i.putExtra(Intent.EXTRA_TEXT, shareUrl);
//                startActivity(Intent.createChooser(i, "Share with"));
//            }
//        });

        Rel_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                i.putExtra(Intent.EXTRA_TEXT, shareUrl);
                startActivity(Intent.createChooser(i, "Share with"));
            }
        });

        imgFeatureRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRecycleList.getLayoutManager().scrollToPosition(horizontalLayoutManagaer.findLastVisibleItemPosition() + 1);
            }
        });

        imgFeatureleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRecycleList.getLayoutManager().scrollToPosition(horizontalLayoutManagaer.findFirstVisibleItemPosition() - 1);
            }
        });

        like_wish_List.setOnLikeListener(new OnLikeListener()
        {
            @Override
            public void liked(LikeButton likeButton)
            {
                add_wish_list();

            }

            @Override
            public void unLiked(LikeButton likeButton)
            {
                add_wish_list();

            }
        });


    }

    private void init() {

        deduction_list = new ArrayList<price_dedution_pojo>();

        Rel_share = (LinearLayout) findViewById(R.id.activity_car_detailmenuLAY);
        linear_empty = (LinearLayout) findViewById(R.id.empty_data);
        linear_empty.setVisibility(View.VISIBLE);
        like_wish_List = (LikeButton) findViewById(R.id.like_wish);
        //txt_notes = (TextView) findViewById(R.id.txt_notes_value);
        img_circle_profile = (CircleImageView) findViewById(R.id.my_profile_layout_imageview);
        txt_vin_Number = (TextView) findViewById(R.id.txt_vin_number);
        rating = (RatingBar) findViewById(R.id.ratingbardetail1);

        myfeatureARR = new ArrayList<FeaturePojo>();
        myCarImages = new ArrayList<>();
        Rel_Back = (RelativeLayout) findViewById(R.id.activity_car_detailbackLAY);
        Rel_Book = (RelativeLayout) findViewById(R.id.activity_car_detailbookLAY);
        Rl_features = (RelativeLayout) findViewById(R.id.Rl_features);
        month_offer_ll = (RelativeLayout) findViewById(R.id.month_offer_ll);
        week_offer_ll = (RelativeLayout) findViewById(R.id.week_offer_ll);
        myreviewARR = new ArrayList<ReviewPojo>();
        myReviewList = (ExpandableHeightListView) findViewById(R.id.activity_car_detailReviewlist);
        myRecycleList = (RecyclerView) findViewById(R.id.activity_car_detailrecylceLIST);

        txt_cardescripition = (TextView) findViewById(R.id.car_descripition);
        txt_action_title = (TextView) findViewById(R.id.activity_car_detailheadTXT);
        txt_action_title_1 = (TextView) findViewById(R.id.action_title_1);
        txt_monthly_offer = (TextView) findViewById(R.id.txt_monthly_offer);
        txt_week_offer = (TextView) findViewById(R.id.txt_week_offer);

        myViewPager = (ViewPager) findViewById(R.id.view_pager_carIMG);
        myViewPager.setPageMargin(3);


        imgFeatureRight = (ImageView) findViewById(R.id.activity_car_recycle_rightIMG);
        imgFeatureleft = (ImageView) findViewById(R.id.activity_car_recycle_leftIMG);
        img_share = (ImageView) findViewById(R.id.img_share);
        txt_car_title = (TextView) findViewById(R.id.activity_car_detailtxt1);
        txt_per_day = (TextView) findViewById(R.id.txt_per_day);
        txt_per_week = (TextView) findViewById(R.id.txt_per_week);
        txt_per_month = (TextView) findViewById(R.id.txt_per_month);
        img_host_profile = (ImageView) findViewById(R.id.cardetailIMG);
        txt_host_name = (TextView) findViewById(R.id.activity_car_detailheaderTXT);

        txt_responsse_time = (TextView) findViewById(R.id.txt_respnse_hour);
        txt_response_rating = (TextView) findViewById(R.id.txt_response_rating);
        txt_total_car_rent = (TextView) findViewById(R.id.txt_total_car_rent);
        txt_mileague = (TextView) findViewById(R.id.txt_milage);
        txt_owner_approval = (TextView) findViewById(R.id.txt_owner_approval);
        carImage_CirclePageIndicator = (CirclePageIndicator) findViewById(R.id.car_detail_circlepageindicator);


        horizontalLayoutManagaer = new LinearLayoutManager(CarDetailPage.this, LinearLayoutManager.HORIZONTAL, false);
        myRecycleList.setHasFixedSize(true);
        myRecycleList.setLayoutManager(horizontalLayoutManagaer);




//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                //Write your code here
//                Load_Data();
//
//            }
//        }, 1000);


        Load_Data();


        if (str_get_wish_list != null && !str_get_wish_list.equals("")) {
            if (str_get_wish_list.equalsIgnoreCase("No")) {
                like_wish_List.setLiked(false);
                System.out.println("-----wishlist--status------->" + str_get_wish_list);
                like_wish_List.setUnlikeDrawable(getResources().getDrawable(R.drawable.like_wish));
            } else {
                like_wish_List.setLiked(true);
                //like_wish_List.setBackgroundColor(getResources().getColor(R.color.red));
                System.out.println("-----wishlist--status------->" + str_get_wish_list);
                like_wish_List.setLikeDrawable(getResources().getDrawable(R.drawable.icn_unlike_icon));
            }
        } else {
                Log.e("Wishlist set ","ERROR");
        }
    }


    public void loadMap(GoogleMap arg) {
        googleMap = arg;
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(false);

    }


    private boolean checkGooglePlayServicesAvailable() {

            final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if (status == ConnectionResult.SUCCESS) {
                return true;
            }

            Log.e(LOGTAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));

            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
                if (errorDialog != null) {
                    errorDialog.show();
                }
            }


        return false;
    }


    //-----------------service---------------------
    public void Load_Data() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("carId", CarId);

       /* Set keys = map.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) map.get(key);

            System.out.println(""+key+":"+value);
        }*/

        System.out.println("-----");

//        Set keys = map.keySet();
//        for (Iterator i = keys.iterator(); i.hasNext(); ) {
//            String key = (String) i.next();
//            String value = (String) map.get(key);
//
//            System.out.println("" + key + ":" + value);
//        }

        Call<ResponseBody> call = apiService.Car_details(map);

        System.out.println("-----------car details url------>" + call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {


                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());

                    } else {
                        if (response != null && response.body() != null) {

                            String Str_response = response.body().string();
                            Log.e("----CarDetailPage ","Response-->"+ Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1")) {
                                linear_empty.setVisibility(View.GONE);

                                JSONObject resobject = object.getJSONObject("responseArr");

                                JSONObject Objdata = object.getJSONObject("responseArr").getJSONObject("car_details");

                                if (Objdata.getJSONArray("features").length() > 0) {
                                    Rl_features.setVisibility(View.VISIBLE);
                                    linear_empty.setVisibility(View.GONE);
                                    myfeatureARR.clear();
                                    for (int feature = 0; feature < Objdata.getJSONArray("features").length(); feature++) {
                                        FeaturePojo featurePojo = new FeaturePojo();
                                        JSONObject obj1 = Objdata.getJSONArray("features").getJSONObject(feature);
                                        featurePojo.setFeatureName(obj1.getString("name"));
                                        featurePojo.setFeatureImage(obj1.getString("image"));
                                        myfeatureARR.add(featurePojo);

                                    }
                                    if (myfeatureARR.size() > 3) {
                                        imgFeatureleft.setVisibility(View.VISIBLE);
                                        imgFeatureRight.setVisibility(View.VISIBLE);
                                    } else {
                                        imgFeatureleft.setVisibility(View.GONE);
                                        imgFeatureRight.setVisibility(View.GONE);
                                    }
                                }else
                                {
                                    Rl_features.setVisibility(View.GONE);
                                }

                                ///--------------------------- features add

                                if (Objdata.getJSONArray("car_images").length() > 0) {
                                    myCarImages.clear();
                                    for (int img = 0; img < Objdata.getJSONArray("car_images").length(); img++) {
                                        JSONObject obj = Objdata.getJSONArray("car_images").getJSONObject(img);
                                        myCarImages.add(obj.getString("image"));
                                    }
                                }
                                //------------------review
                                if (Objdata.getJSONArray("reviews").length() > 0) {
                                    for (int r = 0; r < Objdata.getJSONArray("reviews").length(); r++) {
                                        ReviewPojo review = new ReviewPojo();
                                        JSONObject obj = Objdata.getJSONArray("reviews").getJSONObject(r);
                                        System.out.println("------Load Reviews--------->" + obj.toString());
                                        if(obj.has("Drivername"))
                                            review.setName(obj.getString("Drivername"));
                                        if(obj.has("rateVal"))
                                            review.setRating(obj.getString("rateVal"));
                                        if(obj.has("review"))
                                            review.setInfo(obj.getString("review"));
                                        if(obj.has("DriverProfile_pic"))
                                            review.setProfileImage(obj.getString("DriverProfile_pic"));
                                        myreviewARR.add(review);
                                    }
                                }



                                Object check_object = resobject.get("deductibleArr");

                                if (check_object instanceof JSONArray) {


                                    JSONArray deductiblearr = resobject.getJSONArray("deductibleArr");


                                    if (deductiblearr.length() > 0) {


                                        deduction_list.clear();


                                        for (int i = 0; i < deductiblearr.length(); i++) {

                                            JSONObject jobj = deductiblearr.getJSONObject(i);

                                            price_dedution_pojo pojo = new price_dedution_pojo();

                                            pojo.setId(jobj.getString("id"));
                                            pojo.setPrice_per_day(jobj.getString("price_per_day"));
                                            pojo.setPayable_amount(jobj.getString("payable_amount"));
                                            pojo.setText(jobj.getString("text"));

                                            if(i==0){

                                                pojo.setSelectflag("yes");

                                            }else {

                                                pojo.setSelectflag("no");

                                            }

                                            deduction_list.add(pojo);

                                        }


                                    }


                                }




                                ///------------------------- textview
                                shareUrl = Objdata.getString("shareUrl");

                                txt_vin_Number.setText(Objdata.getString("vin_no"));
                                txt_owner_approval.setText(Objdata.getString("v_no"));
                                txt_action_title.setText(Objdata.getString("car_make") + "  " + Objdata.getString("car_model") + " " + Objdata.getString("year"));
                                txt_car_title.setText(txt_action_title.getText().toString());
                                 //txt_action_title_1.setText(Objdata.getString("ownername"));


                                if (Objdata.has("ownername")) {
                                    txt_action_title_1.setText(Objdata.getString("ownername"));
                                } else if (Objdata.has("firstname")) {
                                    txt_action_title_1.setText(Objdata.getString("firstname") + " " + Objdata.getString("lastname"));
                                }

                                txt_per_day.setText("$" + Objdata.getString("rent_daily"));
                                txt_total_car_rent.setText(Objdata.getString("total_cars"));
                                txt_per_week.setText("$" + Objdata.getString("rent_weekly"));
                                txt_per_month.setText("$" + Objdata.getString("rent_monthly"));
                                txt_cardescripition.setText(Objdata.getString("description"));
                                txt_week_offer.setText(Objdata.getString("weekly_offer")+"%");
                                txt_monthly_offer.setText(Objdata.getString("monthly_offer")+"%");
                                //txt_notes.setText(Objdata.getString("notes"));

                                if(Objdata.getString("weekly_offer").contains(".") && Double.parseDouble(Objdata.getString("weekly_offer"))> 0)
                                    week_offer_ll.setVisibility(View.VISIBLE);
                                else if(!Objdata.getString("weekly_offer").contains(".") && Integer.parseInt(Objdata.getString("weekly_offer")) > 0)
                                    week_offer_ll.setVisibility(View.VISIBLE);
                                else
                                    week_offer_ll.setVisibility(View.GONE);

                                if(Objdata.getString("monthly_offer").contains(".") && Double.parseDouble(Objdata.getString("monthly_offer"))> 0)
                                    month_offer_ll.setVisibility(View.VISIBLE);
                                else if(!Objdata.getString("monthly_offer").contains(".") && Integer.parseInt(Objdata.getString("monthly_offer")) > 0)
                                    month_offer_ll.setVisibility(View.VISIBLE);
                                else
                                    month_offer_ll.setVisibility(View.GONE);
                                if (Objdata.has("ownername")) {
                                    txt_host_name.setText(Objdata.getString("ownername"));
                                } else if (Objdata.has("firstname")) {
                                    txt_host_name.setText(Objdata.getString("firstname") + " " + Objdata.getString("lastname"));
                                }

                                txt_response_rating.setText(Objdata.getString("response_percent") + "%");
                                str_mileage = Integer.parseInt(Objdata.getString("daily_mileage"));
                                if(str_mileage == 0 && !Objdata.getString("unlimited_mileage").equalsIgnoreCase("No"))
                                    txt_mileague.setText("Unlimited "+getResources().getString(R.string.day_mile) + " - " + "Unlimited "+ getResources().getString(R.string.week_mile) + " - " + "Unlimited "+getResources().getString(R.string.month_mile));
                                else
                                    txt_mileague.setText(str_mileage + getResources().getString(R.string.day_mile) + " - " + str_mileage * 7 + getResources().getString(R.string.week_mile) + str_mileage * 30 + " - " + getResources().getString(R.string.month_mile));
                                if (Objdata.has("profile_pic")) {
                                    String str_profile_pic = Objdata.getString("profile_pic");
                                    if (str_profile_pic != null && !str_profile_pic.equals("")) {
                                        Picasso.with(CarDetailPage.this).load(str_profile_pic)
                                                .placeholder(R.drawable.placeholdercar)
                                                .error(R.drawable.placeholdercar)
                                                .into(img_circle_profile);
                                    }
                                }


                                Float rate = Float.valueOf(Objdata.getString("rating"));
                                if (rate != null) {
                                    rating.setRating(rate);
                                }

                                latitude = Objdata.getString("latitude");
                                longitude = Objdata.getString("longitude");

                                ///--------------------- map function
                                if (checkGooglePlayServicesAvailable() == true) {
                                    if (googleMap == null) {
                                        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
                                        // mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                                            @Override
                                            public void onMapReady(GoogleMap arg) {
                                                //loadMap(arg);
                                                googleMap = arg;
                                                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                                                googleMap.getUiSettings().setCompassEnabled(true);
                                                googleMap.getUiSettings().setAllGesturesEnabled(false);
                                                double lat = Double.parseDouble(latitude);
                                                double lon = Double.parseDouble(longitude);
                                                LatLng point = new LatLng(lat, lon);
                                                CameraUpdate center = CameraUpdateFactory
                                                        .newLatLng(new LatLng(lat, lon));
                                                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                                                googleMap.moveCamera(center);
                                                googleMap.animateCamera(zoom);
                                                MarkerOptions options = new MarkerOptions();
                                                options.position(point);
                                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car_location));
                                                googleMap.addMarker(options);
                                            }
                                        });
                                    }
                                }
//                                if (checkGooglePlayServicesAvailable() == true) {
//
//                                        if (!latitude.equals("")) {
//
//                                        }
//
//                                }
                                ///------------------------- image set in view pager

                                if (myreviewARR.size() > 0) {
                                    myReviewList.setAdapter(new Review_Adapter(CarDetailPage.this, myreviewARR));
                                    myReviewList.setExpanded(true);
                                }

                                if (myCarImages.size() > 0) {
                                    DetailPageImage_Adapter adapter = new DetailPageImage_Adapter(CarDetailPage.this, myCarImages);
                                    myViewPager.setAdapter(adapter);
                                    carImage_CirclePageIndicator.setViewPager(myViewPager);
                                    myViewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
                                }

                                if (myfeatureARR.size() > 0) {

                                    FeatureCarDetailPage_Adapter feature_recycle_adapter = new FeatureCarDetailPage_Adapter(CarDetailPage.this, myfeatureARR, myRecycleList);
                                    myRecycleList.setAdapter(feature_recycle_adapter);
                                }


                                JSONObject comarryobj = object.getJSONObject("commonArr");
                                if (comarryobj.length() > 0) {

                                    Str_minumu_sty = comarryobj.getString("minimum_stay");


                                }

                                linear_empty.setVisibility(View.GONE);

                            } else {
                                finish();
                            }
                        } else {
                            linear_empty.setVisibility(View.VISIBLE);
                        }
                    }
                    Loader.dismiss();
                } catch (Exception e) {
                    linear_empty.setVisibility(View.VISIBLE);
                    Loader.dismiss();
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----Car Details Error Response----->" + str_server_erro);
            }
        });
    }


    public void add_wish_list() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        HashMap<String, String> post = new HashMap<>();
        post.put("carId", CarId);
        Log.e("CArId",CarId);

        Call<ResponseBody> call = apiService.add_wish_list(header, post);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {

                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());

                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----W Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        //Find_Cars.currentPosition = Integer.parseInt(pos);
                        Alert("",object.getJSONObject("responseArr").getString("msg"));
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
        final PkDialog mDialog = new PkDialog(this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();

            }
        });


        mDialog.show();

    }
}
