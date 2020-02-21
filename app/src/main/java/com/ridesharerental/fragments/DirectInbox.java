package com.ridesharerental.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesharerental.adapter.Chat_detail_Adapter;
import com.ridesharerental.adapter.Inbox_Adapter;
import com.ridesharerental.app.Booking_Step1;
import com.ridesharerental.app.Chat_detail;
import com.ridesharerental.app.R;
import com.ridesharerental.app.Sign_up_page;
import com.ridesharerental.app.Uread_count;
import com.ridesharerental.pojo.Chat_Bean;
import com.ridesharerental.pojo.Inbox_Bean;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DirectInbox extends Fragment {

    // private RecyclerView MyInboxLIST;
    ArrayList<Inbox_Bean> MyInboxARR;

    Context context;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Common_Loader Loader;

    SessionManager sessionManager;
    Animation animation_enter;
    Animation animation_exit;

    String user_id = "";
    TextView txt_no_data;
    Uread_count unread_message_count;
    String str_msg_coutn="";
    ArrayList<Inbox_Bean> multiselect_list = new ArrayList<>();
    android.view.ActionMode mActionMode;
    Menu context_menu;
    boolean isMultiSelect = false;
    Message_Recycler_Adapter adapter_recycler;
    private ConnectionDetector cd;
    private Boolean isInternetPresent = false;
    RecyclerView recyclerView_List;
    ArrayList<Chat_Bean> receiver_List = new ArrayList<>();
    ArrayList<String> receiver_name_List = new ArrayList<>();
    Typeface tf;
    String str_receiverId="";
    //i created List of int type to store id of data, you can create custom class type data according to your need.
    private List<String> selectedIds = new ArrayList<>();
    RelativeLayout rl_compose_new;
    EditText et_search;

    AlertDialog alertDialog = null;
    public DirectInbox() {
        // Required empty public constructor
    }


//    public static DirectInbox newInstance(String param1, String param2) {
//        DirectInbox fragment = new DirectInbox();
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
        View view = inflater.inflate(R.layout.fragment_direct_inbox, container, false);
        context = getActivity();
        Loader = new Common_Loader(context);

        animation_enter = AnimationUtils.loadAnimation(getActivity(), R.anim.enter);
        animation_exit = AnimationUtils.loadAnimation(getActivity(), R.anim.exit);
        sessionManager = new SessionManager(context);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Sofia Pro Regular.ttf");
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
                    //Toast.makeText(getActivity(), "Details Page", Toast.LENGTH_SHORT).show();
                    cd = new ConnectionDetector(getActivity());
                    isInternetPresent = cd.isConnectingToInternet();
                    if (isInternetPresent) {
                        if (MyInboxARR.size() > 0) {
                            if (MyInboxARR.get(position).getBooking_no() != null && !MyInboxARR.get(position).getBooking_no().equals("")) {
                                Intent val = new Intent(getActivity(), Chat_detail.class);
                                val.putExtra("booking_no", MyInboxARR.get(position).getBooking_no());
                                val.putExtra("sender_name", MyInboxARR.get(position).getSender_name());
                                val.putExtra("from", "direct_owner");
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
//        MyInboxLIST.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
//            {
//                if (MyInboxARR.size() > 0) {
//                    if(MyInboxARR.get(i).getBooking_no()!=null && !MyInboxARR.get(i).getBooking_no().equals(""))
//                    {
//                        Intent val = new Intent(getActivity(), Chat_detail.class);
//                        val.putExtra("booking_no", MyInboxARR.get(i).getBooking_no());
//                        val.putExtra("sender_name", MyInboxARR.get(i).getSender_name());
//                        val.putExtra("from","direct_owner");
//                        startActivity(val);
//                        getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
//                    }
//                    else
//                    {
//                        Toast.makeText(context,context.getResources().getString(R.string.invalid_booking),Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
//        });

        rl_compose_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rl_compose_container.startAnimation(animation_enter);
//                rl_compose_container.setVisibility(View.VISIBLE);
//                rl_compose_new.setVisibility(View.GONE);
                showCustomDialog();
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(adapter_recycler != null)
                    adapter_recycler.filter(et_search.getText().toString());
            }
        });
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isValid())
//                {
//                    send_message();
//                }
//
//            }
//        });





        return view;
    }
    private void showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup

        final Spinner spinner_receiver;
        final EditText et_subject,et_message;
        Button btn_submit;
        ImageView img_compose_close;
        TextView txt_dialog;

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.compose_message, null);
        dialogBuilder.setView(dialogView);

        spinner_receiver = (Spinner) dialogView.findViewById(R.id.spinner_receiver);
        et_subject = (EditText) dialogView.findViewById(R.id.et_subject);
        et_message = (EditText) dialogView.findViewById(R.id.et_message);
        btn_submit = (Button) dialogView.findViewById(R.id.btn_submit);
        txt_dialog = (TextView) dialogView.findViewById(R.id.txt_dialog);
        img_compose_close = (ImageView) dialogView.findViewById(R.id.img_compose_close);
        CustomSpinnerAdapter customSpinnerAdapter1 = new CustomSpinnerAdapter(getActivity(), receiver_name_List);
        spinner_receiver.setAdapter(customSpinnerAdapter1);
        txt_dialog.setText(getString(R.string.conversation_owner_title));
        alertDialog = dialogBuilder.create();

        spinner_receiver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_receiverId = receiver_List.get(position).getReceiverId();
                Log.e("str_receiverId",str_receiverId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner_receiver.getSelectedItemPosition()==0)
                {
                    Alert(getActivity().getResources().getString(R.string.action_opps),getActivity().getResources().getString(R.string.conversation_alert_please_select_owner));

                }else if(et_subject.getText().toString().isEmpty())
                {
                    Alert(getActivity().getResources().getString(R.string.action_opps),getActivity().getResources().getString(R.string.conversation_alert_enter_subject));

                }else if(et_message.getText().toString().isEmpty())
                {
                    Alert(getActivity().getResources().getString(R.string.action_opps),getActivity().getResources().getString(R.string.conversation_alert_enter_message));
                }else
                {
                    if(str_receiverId != null && !str_receiverId.equalsIgnoreCase("007"))
                         send_message(str_receiverId,et_subject.getText().toString(),et_message.getText().toString());
                }
            }
        });

        final AlertDialog finalAlertDialog = alertDialog;
        img_compose_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalAlertDialog.dismiss();
            }
        });
        alertDialog.show();

    }


    private void init(View rootView) {

        MyInboxARR = new ArrayList<Inbox_Bean>();
        recyclerView_List = (RecyclerView) rootView.findViewById(R.id.recycle_vehicle_car);
        rl_compose_new = (RelativeLayout) rootView.findViewById(R.id.rl_compose_new);
        et_search=(EditText) rootView.findViewById(R.id.et_search);
        //  MyInboxLIST = (RecyclerView) rootView.findViewById(R.id.fragment_inbox_listview);
        //MyInboxLIST.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        txt_no_data=(TextView)rootView.findViewById(R.id.txt_no_data);

        txt_no_data.setVisibility(View.GONE);


    }

    public void Load_Data() {
        Loader.show();
        MyInboxARR.clear();
        multiselect_list.clear();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        Call<ResponseBody> call = apiService.inbox_direct(header);
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
                        Log.e("----inbox_direct Response-->", Str_response);
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
                            JSONArray array_receiversArr = response_obj.getJSONArray("receiversArr");

                            if (array_message.length() > 0)
                            {
                                for (int i = 0; i < array_message.length(); i++) {
                                    JSONObject obj = array_message.getJSONObject(i);
                                    Inbox_Bean bean = new Inbox_Bean();
                                    bean.setBooking_no(obj.getString("booking_no"));
                                    bean.setSender_pic(obj.getString("sender_pic"));
                                    bean.setSender_name(obj.getString("sender_name"));
//                                    bean.setCar_make(obj.getString("car_make"));
//                                    bean.setCar_model(obj.getString("car_model"));
//                                    bean.setYear(obj.getString("year"));
                                    bean.setDateAdded(obj.getString("dateAdded"));
                                    bean.setRead_status(obj.getString("read_status"));
                                    bean.setStr_message(obj.getString("message"));
                                    MyInboxARR.add(bean);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                            receiver_name_List.clear();
                            Chat_Bean chat_bean2 = new Chat_Bean();
                            chat_bean2.setReceiverId("007");
                            chat_bean2.setReceiver_name("Select Owner");
                            receiver_List.add(chat_bean2);
                            receiver_name_List.add("Select Owner");
                            if(array_receiversArr.length()>0)
                            {
                                Chat_Bean chat_bean1 = new Chat_Bean();
                                chat_bean1.setReceiverId("007");
                                chat_bean1.setReceiver_name("Select Owner");
                                receiver_List.add(chat_bean1);
                                receiver_name_List.add("Select Owner");
                                for(int i=0;i<array_receiversArr.length();i++)
                                {
                                    JSONObject object1 = array_receiversArr.getJSONObject(i);
                                    // "id": "126",
                                    //"name": "owner owner"
                                    Chat_Bean chat_bean = new Chat_Bean();
                                    chat_bean.setReceiverId(object1.getString("id"));
                                    chat_bean.setReceiver_name(object1.getString("name"));
                                    receiver_List.add(chat_bean);
                                    receiver_name_List.add(object1.getString("name"));
                                }

                            }

//                            if(array_message.length()>1)
//                            {
//                                rl_compose_new.setVisibility(View.GONE);
//                            }else
//                                rl_compose_new.setVisibility(View.VISIBLE);
                        }

                        if (MyInboxARR.size() > 0)
                        {
                            txt_no_data.setVisibility(View.GONE);
                            recyclerView_List.setVisibility(View.VISIBLE);
                            LinearLayoutManager horizontalLayoutManagaer
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView_List.setHasFixedSize(false);
                            recyclerView_List.setLayoutManager(horizontalLayoutManagaer);
                            adapter_recycler = new Message_Recycler_Adapter(getActivity(), MyInboxARR,multiselect_list,"yes");
                            recyclerView_List.setAdapter(adapter_recycler);

                            /*SnapHelper snapHelper = new PagerSnapHelper();
                            try {
                                snapHelper.attachToRecyclerView(Recycle_Active);
                            } catch (Exception e) {

                            }*/
                           // adapter_recycler.notifyDataSetChanged();

                            Loader.dismiss();
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
    private android.view.ActionMode.Callback mActionModeCallback = new android.view.ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.list_delete_menu, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    AlertDelete(getActivity().getString(R.string.confirm),getActivity().getString(R.string.delete_message));
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Inbox_Bean>();
            refreshAdapter();
        }
    };
    private void deleteMessages(String selected_ids) {

        Loader.show();
        //RequestQueue queue = Volley.newRequestQueue(this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("booking_nos", selected_ids);
        post.put("device","android");


        Call<ResponseBody> call = apiService.delete_direct_messages(header,post);
        System.out.println("-------delete_direct_messages Url------>" + call.request().url().toString());
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
                            Loader.dismiss();
                        }
                    }
                    if (response != null && response.body() != null) {
                        String Str_response = response.body().string();
                        Log.e("----delete_direct_messages Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);


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
    public void send_message(String str_receiverId, String s, String message) {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);

        HashMap<String, String> post = new HashMap<>();
        post.put("receiverId", str_receiverId);
        post.put("subject", s);
        post.put("message", message);


        Call<ResponseBody> call = apiService.start_new_direct_conversation(header, post);

        System.out.println("-----------loading url------>" + call.request().url().toString());
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
                        Log.e("----send_direct_admin_message Response-->", Str_response);
                        Load_Data();
                        JSONObject object = new JSONObject(Str_response);

                        JSONObject response_obj = object.getJSONObject("responseArr");



                        String str_status_code = response_obj.getString("status");
                        if (str_status_code.equals("1")) {
                            alertDialog.dismiss();
                            Load_Data();
                        } else {
                            alertDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    Loader.dismiss();
                    alertDialog.dismiss();
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

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }


        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(14);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            txt.setTypeface(tf);
            txt.setTextColor(Color.parseColor("#464646"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.LEFT);
            txt.setPadding(0, 0, 0, 0);
            txt.setTextSize(16);
            txt.setTypeface(tf);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#464646"));
            return txt;
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

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
}
