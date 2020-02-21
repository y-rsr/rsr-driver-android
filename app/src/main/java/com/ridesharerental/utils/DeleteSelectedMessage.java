package com.ridesharerental.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ridesharerental.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user110 on 6/3/2019.
 */

public class DeleteSelectedMessage {

    Context context;
    public DeleteSelectedMessage(Context activity) {
        this.context = activity;
    }

    public void DeleteMessages(JSONArray jsonArray)
    {
        try {
            JSONObject jsonObject = new JSONObject("");

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, ApiInterface.delete_messages, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        queue.add(jobReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
