package com.ridesharerental.ambasador;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.ridesharerental.ambasador.holder.IconTreeItemHolder;
import com.ridesharerental.ambasador.holder.ProfileHolder;
import com.ridesharerental.ambasador.holder.SelectableHeaderHolder;
import com.ridesharerental.ambasador.holder.SelectableItemHolder;
import com.ridesharerental.ambasador.holder.TreeElement;
import com.ridesharerental.ambasador.holder.TreeElementI;
import com.ridesharerental.ambasador.holder.TreeViewClassifAdapter;
import com.ridesharerental.app.R;
import com.ridesharerental.expandable.ExpandableListAdapter;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.widgets.Common_Loader;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

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
 * Created by user65 on 2/6/2018.
 */

public class Ambasador_Genomic_Tree extends Fragment
{
    private ActionBar actionBar;
    Context context;
    ViewGroup containerView;

    private AndroidTreeView tView;
    public  Common_Loader loader;
    ListView list_dynamic;
    List<TreeElementI> array_dy=new ArrayList<>();
    TreeViewClassifAdapter adapter;
    TreeElement parent=null;
    ArrayList<DataEntity> finalResult = new ArrayList<>();



    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.ambasador_genomic_tree, container, false);
        context = getActivity();
        loader=new Common_Loader(context);
        init(rootView,savedInstanceState);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

        return rootView;
    }




    public void init(View rootView,Bundle savedInstanceState)
    {
        containerView = (ViewGroup) rootView.findViewById(R.id.container);
        list_dynamic=(ListView)rootView.findViewById(R.id.list_dynamic);
        expListView = (ExpandableListView)rootView. findViewById(R.id.lvExp);
        prepareListData();
        load_data(savedInstanceState);
       // tree_View(savedInstanceState);

    }


    public void tree_View(Bundle savedInstanceState)
    {
       /* TreeNode root = TreeNode.root();

        TreeNode s1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Master ambassador")).setViewHolder(new ProfileHolder(getActivity()));

        TreeNode s2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_sd_storage, "Storage2")).setViewHolder(new ProfileHolder(getActivity()));
        s1.setSelectable(false);
        s2.setSelectable(false);

        TreeNode folder1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Executive Ambassador 1")).setViewHolder(new SelectableHeaderHolder(getActivity()));
        TreeNode folder2_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Ambassador 1")).setViewHolder(new SelectableHeaderHolder(getActivity()));
        TreeNode folder2_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Ambassador 2")).setViewHolder(new SelectableHeaderHolder(getActivity()));
        TreeNode folder2_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Ambassador 3")).setViewHolder(new SelectableHeaderHolder(getActivity()));

        TreeNode folder3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Folder 3")).setViewHolder(new SelectableHeaderHolder(getActivity()));

      //  fillFolder(folder2);
       // fillFolder(folder3);

        s1.addChildren(folder1, folder2_1,folder2_2,folder2_3);
        //folder1.addChildren(folder2_1,folder2_2);
        //s2.addChildren(folder3);
        fillFolder(folder1);

        root.addChildren(s1);

        s1.setClickListener(new TreeNode.TreeNodeClickListener()
        {
            @Override
            public void onClick(TreeNode node, Object value)
            {
                Toast.makeText(context,value.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);

        tView.setUse2dScroll(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        //tView.setDefaultNodeClickListener(Ambasador_Genomic_Tree.this);
       // tView.setDefaultViewHolder(Ambasador_Genomic_Tree.class);

        containerView.addView(tView.getView());
        tView.expandAll();

        if (savedInstanceState != null)
        {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }*/

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }


    private void fillFolder(TreeNode folder)
    {
        TreeNode file1 = new TreeNode("Ambassador 1").setViewHolder(new SelectableItemHolder(getActivity()));
        TreeNode file2 = new TreeNode("Ambassador 2").setViewHolder(new SelectableItemHolder(getActivity()));
       // TreeNode file3 = new TreeNode("File3").setViewHolder(new SelectableItemHolder(getActivity()));
        folder.addChildren(file1, file2);
    }


    public void load_data(final  Bundle savedInstanceState)
    {
        loader.show();

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("commonId", "290");

        Call<ResponseBody> call = apiService.genomic_tree(map,map);
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
                        // Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject resp_obj=object.getJSONObject("responseArr");


                        JSONArray result = resp_obj.getJSONArray("treeArr");

                        if(result.length()>0)
                        {
                            for(int i=0;i<result.length();i++)
                            {
                                DataEntity dataEntity = parseObject(result.getJSONObject(i));
                               // System.out.println("----Parent-------->"+dataEntity.id);
                                finalResult.add(dataEntity);
                            }
                        }





                       /* for(int i=0;i<result.length();i++)
                        {
                            System.out.println("------Parent------>"+result.getJSONObject(i).getString("name"));
                            JSONObject obj_child=result.getJSONObject(i);
                            JSONArray array_child=obj_child.getJSONArray("childs");

                            for(int j=0;j<array_child.length();j++)
                            {
                               System.out.println("------Name------>"+array_child.getJSONObject(j).getString("name"));
                                System.out.println("------Rank------>"+array_child.getJSONObject(j).getString("rank"));
                                String id=array_child.getJSONObject(j).getString("id");
                                String rank=array_child.getJSONObject(j).getString("rank");
                                parent=new TreeElement(id,rank);
                                DataEntity dataEntity = parseObject(array_child.getJSONObject(j));
                                //finalResult.add(dataEntity);

                            }
                            DataEntity dataEntity = parseObject(array_child.getJSONObject(i));
                            //finalResult.add(dataEntity);

                        }

                        if(array_dy.size()>0)
                        {
                            adapter=new TreeViewClassifAdapter(context,array_dy);
                            list_dynamic.setAdapter(adapter);
                        }*/

                        if(array_dy.size()>0)
                        {
                            for(int j=0;j<finalResult.size();j++)
                            {
                                System.out.println("------GetId---->"+finalResult.get(j).id);
                                for(int k=0;k<finalResult.get(j).children.size();k++)
                                {
                                    System.out.println("----SubId------------>"+finalResult.get(j).children.get(k).id);
                                }


                            }
                            adapter=new TreeViewClassifAdapter(context,array_dy);
                            list_dynamic.setAdapter(adapter);
                        }





                      //  parseObject(object);

                       // parseJson(object);
                       // parseJsonResponse(Str_response);
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

        loader.dismiss();
    }





    public DataEntity parseObject(JSONObject dataEntityObject) throws JSONException
    {
        DataEntity dataEntity = new DataEntity();
        dataEntity.id = dataEntityObject.getString("name");
        System.out.println("-----Parent---->"+ dataEntity.id);
        parent=new TreeElement(dataEntity.id,dataEntity.name);



       //
        if(dataEntityObject.has("childs"))
        {
            JSONArray array = dataEntityObject.getJSONArray("childs");
            for(int i=0;i<array.length();i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                DataEntity temp = parseObject(jsonObject);
                System.out.println("--------------ID----------------------->"+temp.id);
                TreeElement child=new TreeElement(temp.id,temp.name,true,true,parent,i,true);
                //System.out.println("------>"+temp.id);
                dataEntity.children.add(temp);
                array_dy.add(child);
            }
        }

        return dataEntity;
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

       // loader.show();
        String parent="";
        if (data != null)
        {
            Iterator<String> it = data.keys();
            while (it.hasNext())
            {
                String key = it.next();
                try {
                    if (data.get(key) instanceof JSONArray)
                    {

                        System.out.println("-----------111----------------->"+data.getString("id"));
                        parent=data.getString("id");
                        JSONArray arry = data.getJSONArray(key);
                        int size = arry.length();
                        for (int i = 0; i < size; i++)
                        {
                           // System.out.println("-------2222------>"+arry.getJSONObject(i).getString("id"));
                            parseJson(arry.getJSONObject(i));

                        }
                    }


                    else if (data.get(key) instanceof JSONObject)
                    {
                        System.out.println("-----------222222----------------->");
                        parseJson(data.getJSONObject(key));

                    }


                    else
                    {
                        System.out.println("---parent node--->"+parent + ":" + data.getString(key));

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
        loader.dismiss();
    }


















    public class AsyncTaskRunner extends AsyncTask<String, String, String>
    {

        private String resp="";
        ProgressDialog progressDialog;
        Bundle saved_instance;
        TreeNode root;
        Common_Loader load;


        public  AsyncTaskRunner(Bundle instance,String string,Common_Loader loader)
        {
            this.resp=string;
            this.saved_instance=instance;
            this.load=loader;
        }
        @Override
        protected void onPreExecute()
        {
            progressDialog = ProgressDialog.show(context,"ProgressDialog","");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params)
        {
          try {

              TreeNode root = TreeNode.root();

              TreeNode s1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder,resp)).setViewHolder(new ProfileHolder(getActivity()));
              s1.setSelectable(true);

              TreeNode folder1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Executive Ambassador 1")).setViewHolder(new SelectableHeaderHolder(getActivity()));
              // TreeNode folder2_1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Ambassador 1")).setViewHolder(new SelectableHeaderHolder(getActivity()));
              // TreeNode folder2_2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Ambassador 2")).setViewHolder(new SelectableHeaderHolder(getActivity()));
              // TreeNode folder2_3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Ambassador 3")).setViewHolder(new SelectableHeaderHolder(getActivity()));

              TreeNode folder3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Folder 3")).setViewHolder(new SelectableHeaderHolder(getActivity()));

              //  fillFolder(folder2);
              // fillFolder(folder3);

              s1.addChildren(folder1);
              //folder1.addChildren(folder2_1,folder2_2);
              //s2.addChildren(folder3);
              // fillFolder(folder1);

              root.addChildren(s1);
          }catch (Exception e)
          {
              e.printStackTrace();
          }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();



            tView = new AndroidTreeView(getActivity(), root);
            tView.setDefaultAnimation(true);

            tView.setUse2dScroll(true);
            tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
            //tView.setDefaultNodeClickListener(Ambasador_Genomic_Tree.this);
            // tView.setDefaultViewHolder(Ambasador_Genomic_Tree.class);

            containerView.addView(tView.getView());
            tView.expandAll();

            if (saved_instance != null)
            {
                String state = saved_instance.getString("tState");
                if (!TextUtils.isEmpty(state)) {
                    tView.restoreState(state);
                }
            }
        }

    }



    public void parseJsonResponse(String response) {
        try {

            JSONObject object=new JSONObject(response);

            JSONArray jsonArray =object.getJSONArray("childs");

            if (jsonArray != null && jsonArray.length() > 0)
            {
                for (int cnt = 0; cnt < jsonArray.length(); cnt++)
                {
                    JSONObject jsonObj = null;
                    jsonObj = jsonArray.getJSONObject(cnt);
                    Log.d("tag", "jsonObj Menu_Cat_Id->" + jsonObj.optString("id"));
                    JSONArray jsonArrSubCat = jsonObj.getJSONArray("childs");
                    if (jsonArrSubCat != null && jsonArrSubCat.length() > 0)
                    {


                        for (int subCnt = 0; subCnt < jsonArrSubCat.length(); subCnt++)
                        {
                            JSONObject jsonObjSubCategory = jsonArrSubCat.getJSONObject(subCnt);
                            Log.d("tag", "jsonArrSubCat Item_NameEN->" + jsonObjSubCategory.optString("id"));
                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject get_json_array(JSONArray array_value)
    {
        JSONObject obj=null;
        try {
            if(array_value.length()>0)
            {
                for(int i=0;i<array_value.length();i++)
                {
                    obj=array_value.getJSONObject(i);
                }
            }
        }catch (Exception e)
        {
         e.printStackTrace();
        }
        return  obj;
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();



        // Adding child data
       // listDataHeader.add("Top 250");
      //  listDataHeader.add("Now Showing");
       // listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

       // Header, Child data
      //  listDataChild.put(listDataHeader.get(1), nowShowing);
      //  listDataChild.put(listDataHeader.get(2), comingSoon);


        if(finalResult.size()>0)
        {
            for(int j=0;j<finalResult.size();j++)
            {
                System.out.println("----Parent Data id---->"+finalResult.get(j).id);
                listDataHeader.add(finalResult.get(j).id);

                for(int k=0;k<finalResult.get(j).children.size();k++)
                {
                    System.out.println("------Sub Data id--------->"+finalResult.get(j).children.get(k).id);
                }
            }
        }

        listDataChild.put(listDataHeader.get(0), top250);




        listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }
}
