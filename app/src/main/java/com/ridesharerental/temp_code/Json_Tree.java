package com.ridesharerental.temp_code;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.LinearLayout;

import com.ridesharerental.app.R;
import com.ridesharerental.utils.JSONObjectKeeped;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by user65 on 2/23/2018.
 */

public class Json_Tree extends LinearLayout
{
    Handler handler = new Handler();
    private int level = 0;
    Context contx;
    private LinearLayout rootContainer;

    LayoutParams lp;
    public Json_Tree(Context context, final Map<String, Object> jsonMap)
    {
        super(context);
        this.contx = context;
        rootContainer = (LinearLayout) getRootView();
        rootContainer.setOrientation(VERTICAL);
        createTreeViewOfKeepedOptimize((LinkedHashMap<String, Object>) jsonMap, true);
    }
    public void createTreeViewOfKeepedOptimize(LinkedHashMap<String, Object> jsonMap, Boolean... isVirtualNode)
    {
        String key, size = null;
        Object obj;

        String name = "", rank = "", colorcode = "", childs = "", btnText = "-", string_status = "false";
        String email_data="",referal_code="",host_iamge="";
        level += 1;
        Tree_Item itemView  = new Tree_Item(getContext());
        lp = new LayoutParams(700, LayoutParams.WRAP_CONTENT, -2);
        for (LinkedHashMap.Entry<String, Object> entry : jsonMap.entrySet())
        {

            Object value = null;
             key = entry.getKey();
            Boolean isExpend = false;
            if (level < 4)
            {
                isExpend = true;
//                btnText = "-";
            }
            key = entry.getKey();
             obj = entry.getValue();
            System.out.println("----Tempkey------>"+key+"--------Value-------->"+entry.getValue());

            if (obj instanceof JSONObjectKeeped)
            {
                LinkedHashMap<String, Object> map = ((JSONObjectKeeped) obj).getMap();
                size = "{" + String.valueOf(map.size()) + "}";
                if (map.size() == 0) {
                }
                createTreeViewOfKeepedOptimize(map);
            }
            else if (obj instanceof org.json.JSONArray)
            {
                org.json.JSONArray jsonArray = (org.json.JSONArray) obj;
                size = "[" + String.valueOf(jsonArray.length()) + "]";
                if (jsonArray.length() == 0) {
                }
                for (int i = 0; i < (jsonArray).length(); i++)
                {
                    Object each = null;
                    try {
                        each = jsonArray.get(i);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    if (each != null) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        map.put(String.valueOf(i), each);
                        createTreeViewOfKeepedOptimize(map, true);
                    }
                }
            }
            else
            {
                value = obj;
            }



            if(key.equals("name"))
            {
                name=(String) entry.getValue();
            }


            if(key.equals("email"))
            {
                email_data=(String) entry.getValue();
            }

            if(key.equals("childs"))
            {
                JSONArray array= (JSONArray) entry.getValue();
                String status="false";
                if(array.length()>0)
                {
                    System.out.println("-----Size of Array------------->"+array.length());
                    status="true";
                }
                else
                {
                    System.out.println("-----Size of Array----Empty--------->"+array.length());
                    status="false";
                }

                lp.setMargins(level*15,10,0,0);
                itemView.setTag(R.string.name, name);
                itemView.setTag(R.string.email_data,email_data);
                itemView.setTag(R.string.child_status, status);
                rootContainer.addView(itemView, lp);

                String name1_data = (String) itemView.getTag(R.string.name);
                itemView.my_set_data(name1_data,isExpend);

            }
        }
        level -= 1;
    }

    public int getParentNodeIndex(LinearLayout rootView, int currtLevel, int currtIndex)
    {
        for (int i = currtIndex; i >= 0; i--)
        {
            if (Integer.valueOf(rootView.getChildAt(i).getTag().toString()) < currtLevel) {
                return i;
            }
        }
        return 0;
    }




    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        Tree_Item itemView_;
        LayoutParams lp_;
        private String resp;
        public AsyncTaskRunner(Tree_Item itemView,LayoutParams lp)
        {
            this.itemView_=itemView;
            this.lp_=lp;
        }
        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected String doInBackground(String... params)
        {
            return resp;
        }

        @Override
        protected void onPostExecute(String result)
        {
            rootContainer.post(new Runnable() {
                @Override
                public void run() {
                    rootContainer.addView(itemView_, lp_);
                }
            });

        }

    }
}
