package com.ridesharerental.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ridesharerental.adapter.Map_List_Recycler_Adapter;
import com.ridesharerental.adapter.Map_Property_Adapter;
import com.ridesharerental.pojo.Find_Car_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.ads.AdRequest.LOGTAG;

/**
 * Created by user65 on 12/18/2017.
 */

public class Find_Car_Map_List extends AppCompatActivity
{
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    RelativeLayout img_back;
    RecyclerView Recyler_Data;
    Common_Loader Loader;
    Context context;

    SessionManager sessionManager;
    String user_id = "";
    String str_search_Location="";
    ArrayList<Find_Car_Bean> arrayList_City = new ArrayList<Find_Car_Bean>();
    Map_List_Recycler_Adapter adapter;

    private int overallXScroll = 0;

    LinearLayoutManager mLLM ;
    DisplayMetrics displaymetrics = new DisplayMetrics();
    private int mScreenWidth = 0;
    private int mHeaderItemWidth = 0;
    private int mCellWidth = 0;
    int firstItemPosition=0;

    ViewPager viewPager;
    boolean pageStateChanged = false;
    Map_Property_Adapter adapter_pager;
    ArrayList<Marker> markersList;
    ArrayList<Find_Car_Bean> arraylist = new ArrayList<Find_Car_Bean>();

    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_list_layout);
        context=getApplicationContext();
        Loader=new Common_Loader(Find_Car_Map_List.this);

        sessionManager = new SessionManager(Find_Car_Map_List.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);

        init();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Find_Car_Map_List.this.finish();
                overridePendingTransition(R.anim.from_middle, R.anim.to_middle);

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                System.out.println("=======Ganesh======pageStateChanged=======" + pageStateChanged);
                System.out.println("=======Ganesh======position=======" + position);
            }
            @Override
            public void onPageSelected(int position)
            {
                if (googleMap != null && markersList != null && markersList.size() > 0)
                {

                    LatLng selectedLatLng = markersList.get(position).getPosition();
                    System.out.println("---------Lat-------->"+selectedLatLng.latitude);
                    System.out.println("---------Lang-------->"+selectedLatLng.longitude);
                    CameraUpdate center = CameraUpdateFactory.newLatLng(selectedLatLng);
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                    pageStateChanged = false;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state)
            {
                pageStateChanged = true;
            }
        });


/*
        Recyler_Data.setOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dx;
                Log.i("check","overallXScroll->" + overallXScroll);
            }
        });*/


       /* Recyler_Data.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {


            @Override
            public void onLoadMore(int current_page)
            {
                // do something...
                System.out.println("--------scroll get positions------------->"+current_page);
            }
        });*/

       // Recyler_Data.addOnScrollListener(new SnapScrollListener());


/*
        Recyler_Data.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                //get first visible item
                View firstVisibleItem=Recyler_Data.getRootView();
                 firstVisibleItem = mLayoutManager.findViewByPosition(mLayoutManager.findFirstVisibleItemPosition());

                int leftScrollXCalculated = 0;
                if (firstItemPosition == 0)
                {
                    //if first item, get width of headerview (getLeft() < 0, that's why I Use Math.abs())
                    leftScrollXCalculated = Math.abs(firstVisibleItem.getLeft());
                }
                else{

                    //X-Position = Gap to the right + Number of cells * width - cell offset of current first visible item
                    //(mHeaderItemWidth includes already width of one cell, that's why I have to subtract it again)
                    leftScrollXCalculated = (mHeaderItemWidth - mCellWidth) + firstItemPosition  * mCellWidth + firstVisibleItem.getLeft();
                }

                Log.i("asdf","calculated X to left = " + leftScrollXCalculated);

            }
        });*/

       // adapter.notifyDataSetChanged();
    }

    public void init()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_search_Location=bundle.getString("location");
        }


        viewPager=(ViewPager)findViewById(R.id.view_pager_map_bottom);
        viewPager.setPageMargin(8);
        img_back=(RelativeLayout) findViewById(R.id.chat_detail_backLAY);
        Recyler_Data=(RecyclerView)findViewById(R.id.recycler_data);
       /* Recyler_Data.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        Recyler_Data.setLayoutManager(mLayoutManager);*/



        if(checkGooglePlayServicesAvailable()==true)
        {
            if (googleMap == null) {
                mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap arg) {
                        loadMap(arg);
                    }
                });
            }
        }
        load_data();
    }

    public void loadMap(GoogleMap arg)
    {
        googleMap = arg;
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }

    private boolean checkGooglePlayServicesAvailable()
    {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS)
        {
            return true;
        }
        Log.e(LOGTAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));

        if (GooglePlayServicesUtil.isUserRecoverableError(status))
        {
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            if (errorDialog != null)
            {
                errorDialog.show();
            }
        }

        return false;
    }


    public void load_data()
    {
        Loader.show();
        arrayList_City.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
       // header.put("language", "en");
        header.put("commonId", user_id);
        HashMap<String, String> post = new HashMap<>();
        if(str_search_Location!=null && !str_search_Location.equals(""))
        {
            post.put("location", str_search_Location);
        }
        else
        {
            post.put("location", "Los Angeles,CA,United States");
        }
        Call<ResponseBody> call = apiService.find_cars(header, post);
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
                        arrayList_City.clear();
                        JSONObject response_obj = object.getJSONObject("responseArr");
                        String str_status_code = response_obj.getString("status");

                        if (str_status_code.equals("1"))
                        {
                            JSONArray jsonArray_cars_details = response_obj.getJSONArray("cars_list");
                            if (jsonArray_cars_details.length() > 0)
                            {
                                for (int i = 0; i < jsonArray_cars_details.length(); i++)
                                {
                                    JSONObject obj = jsonArray_cars_details.getJSONObject(i);
                                    Find_Car_Bean bean = new Find_Car_Bean();
                                    bean.setId(obj.getString("id"));
                                    bean.setCar_make(obj.getString("car_make"));
                                    bean.setCar_model(obj.getString("car_model"));
                                    bean.setYear(obj.getString("year"));
                                    bean.setRent_daily(obj.getString("rent_daily"));
                                    bean.setRent_weekly(obj.getString("rent_weekly"));
                                    bean.setRent_monthly(obj.getString("rent_monthly"));
                                    bean.setRating(obj.getString("rating"));
                                    bean.setProfile_pic(obj.getString("profile_pic"));
                                    bean.setLatitude(obj.getString("latitude"));
                                    bean.setLongitude(obj.getString("longitude"));
                                    bean.setVal_tag(obj.getString("tag"));

                                    JSONArray array_car_Image = obj.getJSONArray("car_images");
                                    if (array_car_Image.length() > 0)
                                    {
                                        // for (int j = 0; i < array_car_Image.length(); j++)
                                        // {
                                        JSONObject obj_car_img = array_car_Image.getJSONObject(0);
                                        String car_Image = obj_car_img.getString("image");
                                        bean.setCar_image(obj_car_img.getString("image"));
                                        //  }
                                    }
                                    arrayList_City.add(bean);
                                }

                            }
                        }


                        if(arrayList_City.size()>0)
                        {
                            adapter_pager = new Map_Property_Adapter(context, arrayList_City);
                            viewPager.setAdapter(adapter_pager);
                            adapter_pager.notifyDataSetChanged();
                            load_location();

                        }


                       /* if (arrayList_City.size() > 0)
                        {
                            LinearLayoutManager horizontalLayoutManagaer
                                    = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                            Recyler_Data.setHasFixedSize(false);
                            adapter = new Map_List_Recycler_Adapter(context, arrayList_City);
                            Recyler_Data.setAdapter(adapter);

                            *//*SnapHelper snapHelper = new PagerSnapHelper();
                            try {
                                snapHelper.attachToRecyclerView(Recyler_Data);
                            } catch (Exception e) {

                            }*//*
                            adapter.notifyDataSetChanged();
                            Recyler_Data.setLayoutManager(horizontalLayoutManagaer);
                        }*/
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




    class SnapScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (RecyclerView.SCROLL_STATE_IDLE == newState)
            {
                final int scrollDistance = getScrollDistanceOfColumnClosestToLeft(recyclerView);
                System.out.println("-------Display Data--------->"+scrollDistance);
                if (scrollDistance != 0)
                {
                    System.out.println("--------sssssssssss------------->"+scrollDistance);
                    recyclerView.smoothScrollBy(scrollDistance, newState);
                }
            }
        }
    }


    private int getScrollDistanceOfColumnClosestToLeft(final RecyclerView recyclerView) {
        final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.ViewHolder firstVisibleColumnViewHolder = recyclerView.findViewHolderForAdapterPosition(manager.findFirstVisibleItemPosition());
        if (firstVisibleColumnViewHolder == null) {
            return 0;
        }
        final int columnWidth = firstVisibleColumnViewHolder.itemView.getMeasuredWidth();
        final int left = firstVisibleColumnViewHolder.itemView.getLeft();
        final int absoluteLeft = Math.abs(left);
        return absoluteLeft <= (columnWidth / 2) ? left : columnWidth - absoluteLeft;
    }




    public void load_location()
    {
        markersList = new ArrayList<Marker>();
        markersList.clear();
        if (arrayList_City.size() > 0)
        {
            System.out.println("--------Location Fetch----------->");
            for (int k = 0; k < arrayList_City.size(); k++)
            {
                double lati = 0, longLat = 0;
                lati = Double.parseDouble(arrayList_City.get(k).getLatitude());
                longLat = Double.parseDouble(arrayList_City.get(k).getLongitude());
                LatLng point = new LatLng(lati, longLat);
                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                CameraUpdate cameraZoom = null;
                cameraZoom = CameraUpdateFactory.zoomTo(11);
                // CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

                googleMap.moveCamera(cameraPosition);
                googleMap.animateCamera(cameraZoom);
               // googleMap.addMarker(new MarkerOptions().position(new LatLng(lati, longLat)).title("BMW").snippet("Rs 15000"));
                 // LatLng MELBOURNE = new LatLng(lati, longLat);
                /*Marker melbourne = googleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(arrayList_City.get(k).getCar_make()).snippet("$"+arrayList_City.get(k).getRent_daily()));
                melbourne.showInfoWindow();*/

              /*  MarkerOptions options = new MarkerOptions();
                options.position(point);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_tra));*/

               markersList.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(lati, longLat)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icn_map_new))));
                //mMap.addMarker(new MarkerOptions().position(mLatLng).title("My Title").snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }


}
