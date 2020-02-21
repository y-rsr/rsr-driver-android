package com.ridesharerental.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ridesharerental.adapter.Discover_Adapter;
import com.ridesharerental.pojo.Find_Car_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
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
 * Created by user65 on 1/23/2018.
 */

public class Wish_List extends Fragment
{
    private ActionBar actionBar;
    Context context;
    Common_Loader Loader;
    SessionManager sessionManager;
    String user_id = "";
    public ArrayList<Find_Car_Bean> arrayList_City = new ArrayList<Find_Car_Bean>();
    public Discover_Adapter adapter;
    ListView List_cars;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView txt_empty_wishlist;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.wish_list, container, false);


        context=getActivity();
        Loader=new Common_Loader(context);

        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(rootView);

        load_wish_list();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load_wish_list();
            }
        });

        List_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)

            {

//               Intent in=new Intent(getActivity(),Dummy.class);
//                startActivity(in);
//                 getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);




                if (arrayList_City.size() > 0) {


                    Intent val = new Intent(getActivity(), CarDetailPage.class);
                    val.putExtra("CarId", arrayList_City.get(position).getId().toString());
                    val.putExtra("wishlist", arrayList_City.get(position).getWishlist().toString());
                    val.putExtra("position", ""+position);
                    startActivity(val);
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });
        return rootView;
    }

    public void init(View rootView)
    {
        List_cars=(ListView)rootView.findViewById(R.id.list_data);
        txt_empty_wishlist=(TextView) rootView.findViewById(R.id.txt_empty_wishlist);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
    }



    public void load_wish_list()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("commonId", user_id);
        Call<ResponseBody> call = apiService.show_wish_list(map);
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
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        Log.e("----wishlistCars-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        if (str_status_code.equals("1")) {

                            arrayList_City.clear();
                            JSONArray jsonArray_cars_details = response_obj.getJSONArray("wishlistCars");
                            if (jsonArray_cars_details.length() > 0) {


                                for (int i = 0; i < jsonArray_cars_details.length(); i++) {
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
                                    bean.setVin_no(obj.getString("vin_no"));
                                    bean.setV_no(obj.getString("v_no"));
                                    bean.setWishlist("yes");
                                    bean.setTag(obj.getString("tag"));

//                                    System.out.println("------Vehicle Number-json-------->"+obj.getString("v_no"));

                                    JSONArray array_car_Image = obj.getJSONArray("car_images");
                                    if (array_car_Image.length() > 0) {
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
                        if (arrayList_City.size() > 0) {
                            List_cars.setVisibility(View.VISIBLE);
                            txt_empty_wishlist.setVisibility(View.GONE);
                            //txt_near_car_count.setText(String.valueOf(arrayList_City.size() + " " + ctx.getResources().getString(R.string.car_found)));
                            adapter = new Discover_Adapter(getActivity(), arrayList_City,"yes");
                            List_cars.setAdapter(adapter);
                            performClickEvent();
                            adapter.notifyDataSetChanged();
                            Loader.dismiss();
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            //txt_near_car_count.setText(ctx.getResources().getString(R.string.no_car_found));
                            List_cars.setVisibility(View.GONE);
                            txt_empty_wishlist.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setRefreshing(false);
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

    private void performClickEvent() {
        adapter.setOnListItemClick(new Discover_Adapter.OnListItemClick() {
            @Override
            public void OnWishClicked(String value) {
                add_wish_list(value);
            }
        });
    }
    public void add_wish_list(String id) {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        HashMap<String, String> post = new HashMap<>();
        post.put("carId", id);
        Log.e("CArId",id);

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
        final PkDialog mDialog = new PkDialog(getActivity());
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
