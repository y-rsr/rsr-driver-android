package com.ridesharerental.ambasador;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.app.R;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.widgets.Common_Loader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 2/15/2018.
 */

public class Genomic_tree_New extends Fragment
{
    private ActionBar actionBar;
    Context context;
    Common_Loader loader;

    LinearLayout lm;
    LinearLayout.LayoutParams params;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.genomic_tree, container, false);
        context = getActivity();
        loader=new Common_Loader(context);
        init(rootView);

        return rootView;
    }


    public void init(View rortview)
    {
        lm = (LinearLayout) rortview.findViewById(R.id.liear_parent);
        params = new LinearLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        load_data();
    }



    public void load_data()
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("language", "en");
        map.put("currency", "USD");

        Call<ResponseBody> call = apiService.genomic_tree(map,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    loader.dismiss();
                    System.out.println("----kannan555--success----------");
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
                       // Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        String rank=object.getString("rank");
                        parseJson(object);
                        JSONArray array_parent=object.getJSONArray("childs");
                       // System.out.println("-------childs-------->"+array_parent);

                        if(array_parent.length()>0)
                        {


                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });
    }

    private List<JSONObject> getChiild(JSONArray array){

    List<JSONObject> result=new ArrayList<>();
        if(array!=null && array.length()>0){
            for(int i=0;i<array.length();i++){
                try {
                    result.add((JSONObject) array.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }





    private void parseJson(JSONObject data)
    {

        boolean parent= false;
        if (data != null) {
            Iterator<String> it = data.keys();
            while (it.hasNext())
            {
                String key = it.next();
                try {
                    if (data.get(key) instanceof JSONArray)
                    {

                        System.out.println("-----------111----------------->");
                        parent=true;
                        JSONArray arry = data.getJSONArray(key);
                        int size = arry.length();
                        for (int i = 0; i < size; i++)
                        {
                            parseJson(arry.getJSONObject(i));
                        }
                    } else if (data.get(key) instanceof JSONObject)
                    {
                        System.out.println("-----------222222----------------->");
                        parseJson(data.getJSONObject(key));

                    } else
                        {
                            System.out.println("-----------3333----------------->");
                            parent=false;

                            if(parent==false)
                            {
                                System.out.println("---parent node--->"+key + ":" + data.getString(key));
                            }
                            else
                            {
                                System.out.println("---child node--->"+key + ":" + data.getString(key));
                            }



                        if(key.equals("id"))
                        {
                            LinearLayout ll = new LinearLayout(context);
                            ll.setOrientation(LinearLayout.VERTICAL);
                            ll.setLayoutParams(new RelativeLayout.LayoutParams(1080, 100));
                            ll.setBackground(getResources().getDrawable(R.drawable.gray_border));

                            // Create TextView
                            TextView product = new TextView(context);
                            product.setText(data.getString("id"));
                            product.setTextSize(20.f);
                            product.setTextColor(Color.RED);
                            product.setBackground(getResources().getDrawable(R.drawable.gray_border));
                            ll.addView(product);
                            lm.addView(ll);
                        }

                       // String referal_code=data.getString("referral_code");
                       // System.out.println("---------Referal code------->"+referal_code);
                    }
                } catch (Throwable e) {
                    try {
                        System.out.println(key + ":" + data.getString(key));
                    } catch (Exception ee) {
                    }
                    e.printStackTrace();

                }
            }

        }
    }

}
