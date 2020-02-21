package com.ridesharerental.ambasador;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.ambasador.updatedtree.AdvancedJsonTreeView_Updated_Tree;
import com.ridesharerental.app.R;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.utils.JsonFormatUtils;
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
 * Created by user65 on 2/23/2018.
 */

public class Updated_Tree extends Activity
{
    private ActionBar actionBar;
    Context context;
    Common_Loader Loader;
    SessionManager sessionManager;
    String user_id = "";
    String id="";
    private HorizontalScrollView llRoot;

    RelativeLayout Rel_back;

    TextView txt_no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updated_tree_view);

        Loader=new Common_Loader(Updated_Tree.this);
        sessionManager = new SessionManager(Updated_Tree.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init();


        Rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                Updated_Tree.this.finish();

            }
        });
    }


    public void init()
    {
        llRoot = (HorizontalScrollView) findViewById(R.id.ll_root_container);
        Rel_back=(RelativeLayout)findViewById(R.id.chat_detail_backLAY);

        txt_no_data=(TextView)findViewById(R.id.txt_no_child);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            id=bundle.getString("ID");
            load_updated_tree();
        }
    }

    public void load_updated_tree()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        HashMap<String, String> post = new HashMap<>();
        post.put("changeId", id);

        System.out.println("-------changeId----->"+id);
        System.out.println("-------commonId----->"+user_id);


        Call<ResponseBody> call = apiService.updated_tree(header,post);
        System.out.println("-------Updated Tree------->"+call.request().url().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
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
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String status_code=response_obj.getString("status");
                        if(status_code.equals("1"))
                        {
                            JSONArray array=response_obj.getJSONArray("treeArr");
                            if(array.length()>0)
                            {
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object_another=array.getJSONObject(i);
                                    JSONArray array_data=object_another.getJSONArray("childs");
                                    new AsyncTaskRunner(object_another.toString(),Loader).execute();
                                    if(array_data.length()>0)
                                    {
                                        txt_no_data.setVisibility(View.GONE);

                                    }
                                    else
                                    {
                                        txt_no_data.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                            else
                            {
                                txt_no_data.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            txt_no_data.setVisibility(View.VISIBLE);
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









    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        String data;
        AdvancedJsonTreeView_Updated_Tree view;
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
            view = new AdvancedJsonTreeView_Updated_Tree(Updated_Tree.this, map);
            llRoot.addView(view);
            loader_data.dismiss();
        }

    }
}
