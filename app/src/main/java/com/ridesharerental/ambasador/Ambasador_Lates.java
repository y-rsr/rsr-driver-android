package com.ridesharerental.ambasador;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.ridesharerental.app.R;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.utils.JsonFormatUtils;
import com.ridesharerental.view.AdvancedJsonTreeView;
import com.ridesharerental.widgets.Common_Loader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 2/19/2018.
 */

public class Ambasador_Lates extends Fragment {
    private ActionBar actionBar;
    Context context;
    public Common_Loader loader;
    private HorizontalScrollView llRoot;
    SessionManager sessionManager;
    String user_id = "";

    private static final String TAG = "Ambasador_Lates";

    //TextView txt_no_data;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.ambasador_latest, container, false);
        context = getActivity();
        loader = new Common_Loader(context);
        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
//        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);
        llRoot = (HorizontalScrollView) rootView.findViewById(R.id.ll_root_container);
      //  txt_no_data=(TextView)rootView.findViewById(R.id.txt_no_child);

        load_data();

        return rootView;
    }


    public void load_data()
    {
        loader.show();
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("commonId", user_id);

        Call<ResponseBody> call = apiService.genomic_tree(map, map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try {

                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response:Genomic tree " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        // Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject resp_obj = object.getJSONObject("responseArr");

                        JSONArray result = resp_obj.getJSONArray("treeArr");


                        String ss = "";

                        if(result.length()>0)
                        {
                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject obj = result.getJSONObject(i);

                                LinkedHashMap<String, Object> map = JsonFormatUtils.jsonToMapKeeped(obj.toString());
                                Log.d(TAG, "onResponse: start");
                                AdvancedJsonTreeView view = new AdvancedJsonTreeView(context, map);
                                Log.d(TAG, "onResponse: end");

                                llRoot.addView(view);
                                Log.d(TAG, "onResponse: end 2");
                                loader.dismiss();
                                JSONArray array_data=obj.getJSONArray("childs");
                                if(array_data.length()>0)
                                {
                                 //   txt_no_data.setVisibility(View.GONE);
                                }
                                else
                                {
                                 //   txt_no_data.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        else
                        {
                         //   txt_no_data.setVisibility(View.VISIBLE);
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
       //
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String data;
        AdvancedJsonTreeView view;
        LinkedHashMap<String, Object> map;
        Common_Loader loader_data;

        public AsyncTaskRunner(String str_obj, Common_Loader loader_) {
            this.data = str_obj;
            this.loader_data = loader_;
        }

        private String resp;

        @Override
        protected void onPreExecute() {
            loader_data.show();
        }

        @Override
        protected String doInBackground(String... params)
        {
            return resp;
        }

        @Override
        protected void onPostExecute(String result)
        {
            // execution of result of Long time consuming operation

            map = JsonFormatUtils.jsonToMapKeeped(data);
            view = new AdvancedJsonTreeView(context, map);
            llRoot.addView(view);
            loader_data.dismiss();
        }

    }
}
