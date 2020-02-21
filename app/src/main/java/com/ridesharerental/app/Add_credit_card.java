package com.ridesharerental.app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Add_credit_card extends Fragment {
    private ActionBar actionBar;
    Context context;
    Common_Loader Loader;
    EditText edit_credit_card_number,edit_credit_cvv;
    TextView txt_date, txt_year;
    EditText edit_name, edi_security_code;
    SessionManager sessionManager;
    HashMap<String, String> details;
    RelativeLayout relativeLay_exp_year, relativeLay_exp_month,rel_next;
    CheckBox check_creditcard;
    ConnectionDetector cd ;
     EditText et_city;
     EditText et_state;
     EditText et_zipcode;
    PlacesAutocompleteTextView mAutocomplete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View rootView = inflater.inflate(R.layout.fragment_add_credit_card, container, false);
        context = getActivity();
        cd = new ConnectionDetector(context);
        sessionManager = new SessionManager(context);
        Loader = new Common_Loader(context);
        init(rootView);


        return rootView;
    }


    public void init(View rootView) {

        details = sessionManager.getUserDetails();
        edit_credit_card_number = (EditText) rootView.findViewById(R.id.edit_credit_card_number);
        //edit_credit_cvv = (EditText) rootView.findViewById(R.id.edit_credit_cvv);
        edit_name = (EditText) rootView.findViewById(R.id.edit_name);
        edi_security_code = (EditText) rootView.findViewById(R.id.edit_security_code);
        check_creditcard = (CheckBox) rootView.findViewById(R.id.check_creditcard);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_year = (TextView) rootView.findViewById(R.id.txt_year);

        relativeLay_exp_year = (RelativeLayout) rootView.findViewById(R.id.relativeLay_exp_year);
        relativeLay_exp_month = (RelativeLayout) rootView.findViewById(R.id.relativeLay_exp_month);

        et_city = (EditText) rootView.findViewById(R.id.et_city);
        et_state = (EditText) rootView.findViewById(R.id.et_state);
        et_zipcode = (EditText) rootView.findViewById(R.id.et_zipcode);
        mAutocomplete=(PlacesAutocompleteTextView)rootView.findViewById(R.id.ed_adress) ;
        rel_next = (RelativeLayout) rootView.findViewById(R.id.rel_next);
        //--------------------- on click listener
        relativeLay_exp_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp_Year();
            }
        });
        relativeLay_exp_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp_Month();
            }
        });

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Log.d("test", "details " + details);
                       // mAutocomplete.setText(details.name);
                        for (AddressComponent component : details.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {

                                    case LOCALITY:
                                        et_city.setText(component.long_name);
                                        break;
                                    //
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        et_state.setText(component.long_name);
                                        break;

                                    //
                                    case POSTAL_CODE:
                                        et_zipcode.setText(component.long_name);
                                        break;


                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });
        rel_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_credit_card_number.getText().length()>0)
                {
                    if (edit_credit_card_number.getText().length() != 15){

                        if (!mAutocomplete.getText().toString().isEmpty()) {
                        if (!et_city.getText().toString().isEmpty()) {

                            if (!et_state.getText().toString().isEmpty()) {
                            if (!et_zipcode.getText().toString().isEmpty()) {

                                if (txt_date.getText().length() > 0 && !txt_date.getText().toString().equalsIgnoreCase("Month")) {

                                    if (txt_year.getText().length() > 0 && !txt_year.getText().toString().equalsIgnoreCase("Year")) {
                                        if (edit_name.getText().length() > 0) {

                                            if (check_creditcard.isChecked()) {
                                                if (cd.isConnectingToInternet()) {
                                                    save_Data();
                                                } else {
                                                    Alert(getResources().getString(R.string.action_no_internet_title), getResources().getString(R.string.action_no_internet_message));
                                                }

                                            } else {
                                                //-----------------not checked
                                                snack_bar(getResources().getString(R.string.check_creditcard));
                                            }
                                        } else {
                                            //----------------- no card holder name
                                            snack_bar(getResources().getString(R.string.holder_name));
                                        }


                                    } else {
                                        //----------------- no exp year
                                        snack_bar(getResources().getString(R.string.enter_exp_year));
                                    }

                                } else {
                                    //----------------- no exp month
                                    snack_bar(getResources().getString(R.string.enter_exp_date));
                                }
                            }else {
                                //----------------- no exp month
                                snack_bar(getResources().getString(R.string.zipcode_hint));
                            }
                            }else {
                                //----------------- no exp month
                                snack_bar(getResources().getString(R.string.state_hint));
                            }
                        }else
                        {
                            snack_bar(getResources().getString(R.string.city_hint));
                        }
                        }else
                        {
                            snack_bar(getResources().getString(R.string.address_hint));
                        }


                    }else
                        {
                            //----------------- no valid card number
                            snack_bar(getResources().getString(R.string.valid_card_number1));
                    }

                }else {
                    //----------------- no card number
                    snack_bar(getResources().getString(R.string.credit_card_hint));
                }
            }
        });



        if (cd.isConnectingToInternet())
        {
            View_Data();
        }else {
            Alert(getResources().getString(R.string.action_no_internet_title),getResources().getString(R.string.action_no_internet_message));
        }


    }

    //------------------------snack bar---------------
    private void snack_bar(String message)
    {
        String msg =  message;
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar_new, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(msg);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(getActivity(), msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.LENGTH_SHORT);
       // snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }


    public void showPopUp_Month() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.month_picker_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ListView list_month = (ListView) dialog.findViewById(R.id.list_month);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ArrayList<String> arrayList_Month = new ArrayList<String>();
        arrayList_Month.clear();
        arrayList_Month.add("Month");
        arrayList_Month.add("01");
        arrayList_Month.add("02");
        arrayList_Month.add("03");
        arrayList_Month.add("04");
        arrayList_Month.add("05");
        arrayList_Month.add("06");
        arrayList_Month.add("07");
        arrayList_Month.add("08");
        arrayList_Month.add("09");
        arrayList_Month.add("10");
        arrayList_Month.add("11");
        arrayList_Month.add("12");

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, R.layout.single_month_adapter, R.id.txt_single_item, arrayList_Month);
        list_month.setAdapter(itemsAdapter);

        list_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_select_Item = arrayList_Month.get(position).toString();
                System.out.println("--------Select Image--------->" + str_select_Item);
                txt_date.setText(str_select_Item);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showPopUp_Year() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.month_picker_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ListView list_month = (ListView) dialog.findViewById(R.id.list_month);



        DateFormat df = new SimpleDateFormat("yy", Locale.ENGLISH); // Just the year, with 2 digits
        int year = Integer.parseInt(df.format(Calendar.getInstance().getTime()));
        System.out.println("========year: "+year);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ArrayList<String> arrayList_Month = new ArrayList<String>();
        arrayList_Month.clear();
        arrayList_Month.add("Year");
        arrayList_Month.add(String.valueOf(year));
        for (int i = 0; i < 24; i++) {
            year++;
            //System.out.println("--------Show Year----->" + String.valueOf(year));
            arrayList_Month.add(String.valueOf(year));
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(context, R.layout.single_month_adapter, R.id.txt_single_item, arrayList_Month);
        list_month.setAdapter(itemsAdapter);


        list_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str_select_Year = arrayList_Month.get(position).toString();
                txt_year.setText(str_select_Year);
                dialog.dismiss();
            }
        });


        dialog.show();

    }


    //-----------------service---------------------
    public void View_Data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", details.get(sessionManager.KEY_USER_ID));
        Call<ResponseBody> call = apiService.view_card_details(header);
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
                    } else {

                        if (response != null && response.body() != null)
                        {
                            String Str_response = response.body().string();
                            Log.e("----Country Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);
                            Loader.dismiss();
                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1")) {
                                JSONObject object1 = object.getJSONObject("responseArr").getJSONObject("car_details");


                                if (!object1.getString("cc_no").equalsIgnoreCase("")) {
                                    edit_credit_card_number.setText(object1.getString("cc_no"));
                                }
//                                if (!object1.getString("cc_no").equalsIgnoreCase("")) {
//                                    edit_credit_cvv.setText(object1.getString("cc_no"));
//                                }
                                if (!object1.getString("cc_holder_name").equalsIgnoreCase("")) {
                                    edit_name.setText(object1.getString("cc_holder_name"));
                                }
                                if (!object1.getString("cc_exp_date").equalsIgnoreCase("")) {
                                    txt_date.setText(object1.getString("cc_exp_date"));
                                }
                                if (!object1.getString("cc_exp_year").equalsIgnoreCase("")) {
                                    txt_year.setText(object1.getString("cc_exp_year"));
                                }

                                if (!object1.getString("cc_city").equalsIgnoreCase("")) {
                                    et_city.setText(object1.getString("cc_city"));
                                }
                                if (!object1.getString("cc_state").equalsIgnoreCase("")) {
                                    et_state.setText(object1.getString("cc_state"));
                                }
                                if (!object1.getString("cc_zip").equalsIgnoreCase("")) {
                                    et_zipcode.setText(object1.getString("cc_zip"));
                                }


                            } else {
                                Alert_sever(context.getResources().getString(R.string.action_opps), context.getResources().getString(R.string.server_error));
                            }

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


        Loader.dismiss();
    }

    //-----------------------server error alert
    public void Alert_sever(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(context);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent val = new Intent(context, Main_homepage.class);
                context.startActivity(val);
            }
        });

        mDialog.show();
    }

    //-----------------------alert
    public void Alert(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(context);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }


    //-------------save----service---------------------
    public void save_Data() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", details.get(sessionManager.KEY_USER_ID));
        HashMap<String, String> map = new HashMap<>();
        map.put("cc_no",edit_credit_card_number.getText().toString());
        //map.put("cc_ccv",edit_credit_cvv.getText().toString());
        map.put("cc_exp_date", txt_date.getText().toString());
        map.put("cc_exp_year", txt_year.getText().toString());
        map.put("cc_holder_name", edit_name.getText().toString());
        map.put("cc_address", mAutocomplete.getText().toString());
        map.put("cc_state", et_state.getText().toString());
        map.put("cc_city", et_city.getText().toString());
        map.put("cc_zip", et_zipcode.getText().toString());
        Call<ResponseBody> call = apiService.add_card_details(header,map);
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
                    } else {

                        if (response != null && response.body() != null)
                        {
                            String Str_response = response.body().string();
                            Log.e("----Country Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);

                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1"))
                            {
                                Alert(getResources().getString(R.string.action_success),object.getJSONObject("responseArr").getString("msg"));
                            }else {
                                Alert_sever(context.getResources().getString(R.string.action_opps), context.getResources().getString(R.string.server_error));
                            }
                            Loader.dismiss();
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
}
