package com.ridesharerental.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ridesharerental.adapter.Inbox_Adapter;
import com.ridesharerental.app.Chat_detail;
import com.ridesharerental.app.R;
import com.ridesharerental.app.Uread_count;
import com.ridesharerental.pojo.DeleteMessage;
import com.ridesharerental.pojo.Inbox_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.utils.DeleteSelectedMessage;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerInbox extends Fragment {
    ArrayList<Inbox_Bean> multiselect_list = new ArrayList<>();
    Message_Recycler_Adapter adapter_recycler;
    boolean isMultiSelect = false;
    ActionMode mActionMode;
    Menu context_menu;
    Context context;
    ArrayList<Inbox_Bean> MyInboxARR;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Common_Loader Loader;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    RecyclerView recyclerView_List;
    SessionManager sessionManager;
    String user_id = "";
    TextView txt_no_data;
    Uread_count unread_message_count;
    String str_msg_coutn="";
    EditText et_search;
    public OwnerInbox() {
        // Required empty public constructor
    }


//    public static OwnerInbox newInstance(String param1, String param2) {
//        OwnerInbox fragment = new OwnerInbox();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_owner_inbox, container, false);
        context = getActivity();
        Loader = new Common_Loader(context);


        sessionManager = new SessionManager(context);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->" + user_id);
        init(view);
        Load_Data();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Load_Data();
            }
        });

        recyclerView_List.addOnItemTouchListener( new RecyclerItemClickListener(getActivity(), recyclerView_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else {
                   // Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
                    cd = new ConnectionDetector(getActivity());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        if (MyInboxARR.size() > 0) {
                            if (MyInboxARR.get(position).getBooking_no() != null && !MyInboxARR.get(position).getBooking_no().equals("")) {
                                Intent val = new Intent(getActivity(), Chat_detail.class);
                                val.putExtra("booking_no", MyInboxARR.get(position).getBooking_no());
                                val.putExtra("sender_name", MyInboxARR.get(position).getSender_name());
                                val.putExtra("from", "inbox");
                                startActivity(val);
                                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                            } else {
                                Toast.makeText(context, context.getResources().getString(R.string.invalid_booking), Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(getActivity().findViewById(R.id.rel_message), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.RED);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Inbox_Bean>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);
            }
        }));

        return view;
    }


    private void init(View rootView) {

        MyInboxARR = new ArrayList<Inbox_Bean>();

       // MyInboxLIST = (ListView) rootView.findViewById(R.id.fragment_inbox_listview);
        recyclerView_List = (RecyclerView) rootView.findViewById(R.id.recycle_vehicle_car);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        txt_no_data=(TextView)rootView.findViewById(R.id.txt_no_data);
        txt_no_data.setVisibility(View.GONE);
        et_search=(EditText) rootView.findViewById(R.id.et_search);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_List.setLayoutManager(mLayoutManager);
    }


    //-----------------service---------------------
    public void Load_Data() {
        Loader.show();
        MyInboxARR.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.list_inbox(header);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
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
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj = object.getJSONObject("responseArr");




                        String status_code = response_obj.getString("status");
                        if (status_code.equals("1"))
                        {
                            JSONObject common_Obj=object.getJSONObject("commonArr");

                            String str_profile_image = common_Obj.getString("profile_pic");
                            sessionManager.set_profile(str_profile_image);

                            str_msg_coutn=common_Obj.getString("unread_message_count");
                            if(unread_message_count!=null)
                            {
                                if(str_msg_coutn!=null && !str_msg_coutn.equals("") && !str_msg_coutn.equals(null) && !str_msg_coutn.equals("0"))
                                {
                                    unread_message_count.aMethod(str_msg_coutn);
                                }
                                else
                                {
                                    unread_message_count.aMethod("");
                                }

                                System.out.println("-------put message count interface-------->");
                            }


                            sessionManager.set_msg_count(str_msg_coutn);

                            System.out.println("--------session count---------->"+str_msg_coutn);


                            JSONArray array_message = response_obj.getJSONArray("messages");
                            if (array_message.length() > 0)
                            {
                                for (int i = 0; i < array_message.length(); i++) {
                                    JSONObject obj = array_message.getJSONObject(i);
                                    Inbox_Bean bean = new Inbox_Bean();
                                    bean.setBooking_no(obj.getString("booking_no"));
                                    bean.setSender_pic(obj.getString("sender_pic"));
                                    bean.setSender_name(obj.getString("sender_name"));
                                    bean.setCar_make(obj.getString("car_make"));
                                    bean.setCar_model(obj.getString("car_model"));
                                    bean.setYear(obj.getString("year"));
                                    bean.setDateAdded(obj.getString("dateAdded"));
                                    bean.setRead_status(obj.getString("read_status"));
                                    MyInboxARR.add(bean);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }

                        if (MyInboxARR.size() > 0)
                        {
                            txt_no_data.setVisibility(View.GONE);
                            recyclerView_List.setVisibility(View.VISIBLE);
                            LinearLayoutManager horizontalLayoutManagaer
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView_List.setHasFixedSize(false);
                            adapter_recycler = new Message_Recycler_Adapter(getActivity(), MyInboxARR,multiselect_list,"no");
                            recyclerView_List.setAdapter(adapter_recycler);

                            adapter_recycler.notifyDataSetChanged();
                            recyclerView_List.setLayoutManager(horizontalLayoutManagaer);
                            Loader.dismiss();
                            filterProcess();
                            //prepareListioner();
                        }
                        else
                        {
                            txt_no_data.setVisibility(View.VISIBLE);
                            recyclerView_List.setVisibility(View.GONE);
                        }
                        if (mSwipeRefreshLayout.isRefreshing() == true) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                    }
                    else
                    {
                        txt_no_data.setVisibility(View.VISIBLE);
                        recyclerView_List.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    txt_no_data.setVisibility(View.VISIBLE);
                    recyclerView_List.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Loader.dismiss();
                String str_server_erro = t.getMessage();
                System.out.println("----failure----->" + str_server_erro);
            }
        });

        Loader.dismiss();
    }

    private void filterProcess() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(adapter_recycler != null)
                        adapter_recycler.filter(et_search.getText().toString());
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void prepareListioner() {

//        MyInboxLIST.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
//                final int checkedCount = MyInboxLIST.getCheckedItemCount();
//                // Set the CAB title according to total checked items
//                mode.setTitle(checkedCount + " Selected");
//                // Calls toggleSelection method from ListViewAdapter Class
//                adapter.toggleSelection(position);
//            }
//
//            @Override
//            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
//                mode.getMenuInflater().inflate(R.menu.list_delete_menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.delete:
//                        // Calls getSelectedIds method from ListViewAdapter Class
//                        JSONArray selected_id = new JSONArray();
//                        SparseBooleanArray selected = adapter
//                                .getSelectedIds();
//                        // Captures all selected ids with a loop
//                        for (int i = (selected.size() - 1); i >= 0; i--) {
//                            if (selected.valueAt(i)) {
//                                Inbox_Bean selecteditem = (Inbox_Bean) adapter.getItem(selected.keyAt(i));
//                                // Remove selected items following the ids
//                                Log.e("Selected Item",selecteditem.getBooking_no());
//                                try {
//                                    selected_id.put(i,selecteditem.getBooking_no());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                adapter.remove(selecteditem);
//                            }
//                        }
//                        // Close CAB
//                        mode.finish();
//                        Log.e("selected_id",selected_id.toString());
//                        deleteMessages(selected_id.toString());
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(android.view.ActionMode mode) {
//                adapter.removeSelection();
//            }
//        });
//
    }
    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(MyInboxARR.get(position)))
                multiselect_list.remove(MyInboxARR.get(position));
            else
                multiselect_list.add(MyInboxARR.get(position));

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter()
    {
        adapter_recycler.selected_usersList=multiselect_list;
        adapter_recycler.ArrayLists=MyInboxARR;
        adapter_recycler.notifyDataSetChanged();
    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.list_delete_menu, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    AlertDelete(getActivity().getString(R.string.confirm),getActivity().getString(R.string.delete_message));

                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Inbox_Bean>();
            refreshAdapter();
        }
    };
    private void AlertDelete(String title,String message)
    {
        final PkDialog mDialog = new PkDialog(getActivity());
        mDialog.setDialogTitle(title);
        mDialog.setDialogMessage(message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

                String delete_ids="";
                String comma="";
                if(multiselect_list.size()>0)
                {
                    for(int i=0;i<multiselect_list.size();i++) {

                            delete_ids = delete_ids+comma+multiselect_list.get(i).getBooking_no();
                            comma = ",";
                      MyInboxARR.remove(multiselect_list.get(i));
                    }
                    adapter_recycler.notifyDataSetChanged();

                    if (mActionMode != null) {
                        mActionMode.finish();
                    }

                    deleteMessages(delete_ids);
                }

            }
        });

        mDialog.setNegativeButton(getResources().getString(R.string.Cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();



    }
    private void deleteMessages(String selected_ids) {



        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("booking_nos", selected_ids);
        post.put("device","android");

        Set keys = post.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            String value = (String) post.get(key);

            System.out.println("" + key + ":" + value);
        }

        List<String> selected_id = new ArrayList<>();
        for(int i=0;i<multiselect_list.size();i++) {

                selected_id.add(multiselect_list.get(i).getBooking_no());

            //MyInboxARR.remove(multiselect_list.get(i));
        }

        Call<ResponseBody> call = apiService.delete_messages(header,post);
        System.out.println("-------delete_messages Url------>" + call.request().url().toString());
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
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----delete_direct_messages Response-->", Str_response);
//                        JSONObject object = new JSONObject(Str_response);


                        Loader.dismiss();




                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Loader.dismiss();
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
//    @Override
//    public void onAttach(Activity activity)
//    {
//        super.onAttach(activity);
//        // This makes sure that the container activity has implemented
//        // the callback interface. If not, it throws an exception
//        try {
//            unread_message_count = (Uread_count) activity;
//            if(str_msg_coutn!=null && !str_msg_coutn.equals("") && !str_msg_coutn.equals(null) && !str_msg_coutn.equals("0"))
//            {
//                unread_message_count.aMethod(str_msg_coutn);
//                System.out.println("------interface-unread count------>"+str_msg_coutn);
//            }
//            else
//            {
//                unread_message_count.aMethod("");
//            }
//            System.out.println("------Interface object created--------->");
//        } catch (ClassCastException e)
//        {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnHeadlineSelectedListener");
//        }
//    }
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
}
}
