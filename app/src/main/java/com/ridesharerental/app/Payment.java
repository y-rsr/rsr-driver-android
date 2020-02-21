package com.ridesharerental.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devspark.appmsg.AppMsg;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user65 on 12/12/2017.
 */

public class Payment extends Activity
{
    RelativeLayout Rel_back;
    RelativeLayout Rel_Booking,change_cardLAY;
    String str_Booking_Number="",str_booking_amount="";

    RelativeLayout Rel_Paypal,Rel_Credit_Card;
    TextView txt_paypal,txt_crdeit_card;

    RelativeLayout Rel_Month,Rel_Year;

    TextView txt_Val_Month,txt_Val_Year;

    Common_Loader Loader;

    SessionManager sessionManager;
    String user_id="";
    EditText edit_card_Number,edit_security_code;
    String str_post_card_number="",str_post_month="",str_post_year="",str_card_security_code="";
    String str_calling_type="";
    Call<ResponseBody> call;
    TextView txt_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        Loader=new Common_Loader(Payment.this);

        sessionManager = new SessionManager(Payment.this);
        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);

        init();
        Rel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Payment.this.finish();
            }
        });

        change_cardLAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAvailToChangeCArd();
            }
        });

        Rel_Booking.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(edit_card_Number.getText().toString().trim().length()>0)
                {
                    if(!txt_Val_Month.getText().toString().equalsIgnoreCase("MM"))
                    {
                        if(!txt_Val_Year.getText().toString().equalsIgnoreCase("YYYY"))
                        {
                            if(edit_security_code.getText().toString().trim().length()>0)
                            {
                                str_post_card_number=edit_card_Number.getText().toString();
                                str_post_month=txt_Val_Month.getText().toString();
                                str_post_year=txt_Val_Year.getText().toString();
                                str_card_security_code=edit_security_code.getText().toString();
                                Make_Payment();
                            }
                            else
                                {
                            //-------------enter security code
                                    snack_bar( getResources().getString(R.string.enter_security_code),"");
                            }
                        }
                        else
                            {
                            //--------select year
                                snack_bar(getResources().getString(R.string.select_year),"");
                        }
                    }
                    else
                    {
                        //-------------select month
                        snack_bar(getResources().getString(R.string.select_month),"");
                    }
                }
                else
                {
                    //----- Enter card Number
                    snack_bar(getResources().getString(R.string.enter_card_number),"");
                }

            }
        });


        Rel_Paypal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Rel_Credit_Card.setBackground(getResources().getDrawable(R.drawable.white_border));
                Rel_Paypal.setBackground(getResources().getDrawable(R.drawable.textview_border));

                txt_paypal.setTextColor(getResources().getColor(R.color.app_color_1));
                txt_crdeit_card.setTextColor(getResources().getColor(R.color.ligth_gray));

                Alert_ask(getResources().getString(R.string.rideshare_rental),getResources().getString(R.string.continuw_with_paypal));

            }
        });



        Rel_Credit_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Rel_Paypal.setBackground(getResources().getDrawable(R.drawable.white_border));
                Rel_Credit_Card.setBackground(getResources().getDrawable(R.drawable.textview_border));

                txt_paypal.setTextColor(getResources().getColor(R.color.ligth_gray));
                txt_crdeit_card.setTextColor(getResources().getColor(R.color.app_color_1));
            }
        });



        Rel_Month.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPopUp_Month();
            }
        });

        Rel_Year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp_Year();
            }
        });
    }

    private void checkAvailToChangeCArd() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        System.out.println("-----commonId-------->"+user_id);


            call = apiService.check_cc_status(header);


            Log.e("url",call.request().url().toString());

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
                        Log.e("----check_ Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");

                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            showALertAddCard();
                        }
                        else if(str_status_code.equals("0"))
                        {

                            showALert(response_obj.getJSONObject("msg"));
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

    private void showALert(JSONObject msg) {

        final Dialog dialog = new Dialog(Payment.this);
        dialog.setContentView(R.layout.alert_change_credit_card);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView custom_dialog_library_title_textview = (TextView) dialog.findViewById(R.id.custom_dialog_library_title_textview);
        TextView custom_dialog_library_message_textview = (TextView) dialog.findViewById(R.id.custom_dialog_library_message_textview);
        TextView txt_email = (TextView) dialog.findViewById(R.id.txt_email);
        TextView txt_mobile = (TextView) dialog.findViewById(R.id.txt_mobile);
        Button custom_dialog_library_cancel_button = (Button) dialog.findViewById(R.id.custom_dialog_library_cancel_button);

        try {
            custom_dialog_library_title_textview.setText(getString(R.string.hi)+" "+msg.getString("holder_name"));
            custom_dialog_library_message_textview.setText(msg.getString("alert"));
            txt_email.setText(": "+msg.getString("admin_email"));
            txt_mobile.setText(": "+msg.getString("admin_tel"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        custom_dialog_library_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void showALertAddCard() {

        final Dialog dialog = new Dialog(Payment.this);
        dialog.setContentView(R.layout.alert_add_card_details);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText edit_credit_card_number = (EditText) dialog.findViewById(R.id.edit_credit_card_number);
        RelativeLayout  relativeLay_exp_month = (RelativeLayout) dialog.findViewById(R.id.relativeLay_exp_month);
        RelativeLayout  relativeLay_exp_year = (RelativeLayout) dialog.findViewById(R.id.relativeLay_exp_year);
        final EditText edit_name = (EditText) dialog.findViewById(R.id.edit_name);
        final TextView txt_date = (TextView) dialog.findViewById(R.id.txt_date);
        final TextView txt_year = (TextView) dialog.findViewById(R.id.txt_year);
        final EditText et_city = (EditText) dialog.findViewById(R.id.et_city);
        final EditText et_state = (EditText) dialog.findViewById(R.id.et_state);
        final EditText et_zipcode = (EditText) dialog.findViewById(R.id.et_zipcode);
        CheckBox check_creditcard = (CheckBox) dialog.findViewById(R.id.check_creditcard);
        Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);
        Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
        final PlacesAutocompleteTextView mAutocomplete=(PlacesAutocompleteTextView)dialog.findViewById(R.id.ed_adress) ;
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

        relativeLay_exp_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp_MonthDialog(txt_date);
            }
        });

        relativeLay_exp_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp_YearDialog(txt_year);
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_Data(dialog,edit_name.getText().toString(),edit_credit_card_number.getText().toString(),txt_date.getText().toString(),txt_year.getText().toString(),"",et_city.getText().toString(),et_state.getText().toString(),et_zipcode.getText().toString());
            }
        });

        dialog.show();

    }
    public void save_Data(final Dialog dialog,String str_name,String str_card_num, String str_mm,String str_yy, String str_address, String str_city, String str_state, String str_zip) {

        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", user_id);
        HashMap<String, String> map = new HashMap<>();
        map.put("cc_no",str_card_num);
        map.put("cc_exp_date", str_mm);
        map.put("cc_exp_year", str_yy);
        map.put("cc_holder_name", str_name);
        map.put("cc_address", str_address);
        map.put("cc_state", str_state);
        map.put("cc_city", str_city);
        map.put("cc_zip", str_zip);

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
                                dialog.dismiss();
                                Alertsuccess(getResources().getString(R.string.action_success),object.getJSONObject("responseArr").getString("msg"));
                            }else {
                                Alert_sever(getResources().getString(R.string.action_opps), getResources().getString(R.string.server_error));
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
    public void Alert_sever(final String str_title, final String str_message) {
        final PkDialog mDialog = new PkDialog(Payment.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
//                Intent val = new Intent(context, Main_homepage.class);
//                context.startActivity(val);
            }
        });

        mDialog.show();
    }
    public void init()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_Booking_Number =bundle.getString("Booking_number");
            System.out.println("-------Bookign NUmber------>"+str_Booking_Number);
            str_calling_type=bundle.getString("calling");
            str_booking_amount=bundle.getString("total_amount");
        }

        txt_pay=(TextView)findViewById(R.id.text_pay);
        if(str_calling_type!=null && !str_calling_type.equals("") && str_calling_type.equalsIgnoreCase("extend"))
        {
            txt_pay.setText(getResources().getString(R.string.extend_credit_card));
        }
        else
        {
            txt_pay.setText(getResources().getString(R.string.book_it_using));
        }
        Rel_back=(RelativeLayout)findViewById(R.id.chat_detail_backLAY);
        Rel_Booking=(RelativeLayout)findViewById(R.id.booking_step2leftLAY);
        change_cardLAY=(RelativeLayout)findViewById(R.id.change_cardLAY);
        Rel_Paypal=(RelativeLayout)findViewById(R.id.rel_paypal);
        Rel_Credit_Card=(RelativeLayout)findViewById(R.id.rel_credit_card);

        /*Rel_Paypal.setBackground(getResources().getDrawable(R.drawable.textview_border));
        Rel_Credit_Card.setBackground(getResources().getDrawable(R.drawable.white_border));*/


        Rel_Paypal.setBackground(getResources().getDrawable(R.drawable.white_border));
        Rel_Credit_Card.setBackground(getResources().getDrawable(R.drawable.textview_border));


        txt_paypal=(TextView)findViewById(R.id.txt_paypal);
        txt_crdeit_card=(TextView)findViewById(R.id.txt_credit_card);
        txt_crdeit_card.setTextColor(getResources().getColor(R.color.app_color_1));

        Rel_Month=(RelativeLayout)findViewById(R.id.rel_month);
        Rel_Year=(RelativeLayout)findViewById(R.id.rel_year);

        txt_Val_Month=(TextView)findViewById(R.id.txt_val_month);
        txt_Val_Year=(TextView)findViewById(R.id.txt_val_year);

        edit_card_Number=(EditText)findViewById(R.id.edit_card_number);
        edit_security_code=(EditText)findViewById(R.id.edit_security_code);

        View_Data();
    }



    public void showPopUp_Month()
    {
        final Dialog dialog = new Dialog(Payment.this);
        dialog.setContentView(R.layout.month_picker_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ListView list_month=(ListView)dialog.findViewById(R.id.list_month);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ArrayList<String> arrayList_Month=new ArrayList<String>();
        arrayList_Month.clear();
        arrayList_Month.add("MM");
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
                new ArrayAdapter<String>(this, R.layout.single_month_adapter,R.id.txt_single_item ,arrayList_Month);
        list_month.setAdapter(itemsAdapter);

        list_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            String str_select_Item=arrayList_Month.get(position).toString();
                System.out.println("--------Select Image--------->"+str_select_Item);
                txt_Val_Month.setText(str_select_Item);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showPopUp_MonthDialog(final TextView tv)
    {
        final Dialog dialog = new Dialog(Payment.this);
        dialog.setContentView(R.layout.month_picker_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ListView list_month=(ListView)dialog.findViewById(R.id.list_month);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ArrayList<String> arrayList_Month=new ArrayList<String>();
        arrayList_Month.clear();
        arrayList_Month.add("MM");
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
                new ArrayAdapter<String>(this, R.layout.single_month_adapter,R.id.txt_single_item ,arrayList_Month);
        list_month.setAdapter(itemsAdapter);

        list_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            String str_select_Item=arrayList_Month.get(position).toString();
                System.out.println("--------Select Image--------->"+str_select_Item);
                tv.setText(str_select_Item);
                dialog.dismiss();
            }
        });

        dialog.show();

    }




    public void showPopUp_Year()
    {
        final Dialog dialog = new Dialog(Payment.this);
        dialog.setContentView(R.layout.month_picker_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ListView list_month=(ListView)dialog.findViewById(R.id.list_month);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ArrayList<String> arrayList_Month=new ArrayList<String>();
        arrayList_Month.clear();
        arrayList_Month.add("YYYY");
        arrayList_Month.add(String.valueOf(year));
        for(int i=0;i<24;i++)
        {
            year ++;
            System.out.println("--------Show Year----->"+String.valueOf(year));
            arrayList_Month.add(String.valueOf(year));
        }
        /*arrayList_Month.add("01");
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
        arrayList_Month.add("12");*/

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, R.layout.single_month_adapter,R.id.txt_single_item ,arrayList_Month);
        list_month.setAdapter(itemsAdapter);


        list_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            String str_select_Year=arrayList_Month.get(position).toString();
                txt_Val_Year.setText(str_select_Year);
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void showPopUp_YearDialog(final TextView tv)
    {
        final Dialog dialog = new Dialog(Payment.this);
        dialog.setContentView(R.layout.month_picker_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        ListView list_month=(ListView)dialog.findViewById(R.id.list_month);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final ArrayList<String> arrayList_Month=new ArrayList<String>();
        arrayList_Month.clear();
        arrayList_Month.add("YYYY");
        arrayList_Month.add(String.valueOf(year));
        for(int i=0;i<24;i++)
        {
            year ++;
            System.out.println("--------Show Year----->"+String.valueOf(year));
            arrayList_Month.add(String.valueOf(year));
        }
        /*arrayList_Month.add("01");
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
        arrayList_Month.add("12");*/

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, R.layout.single_month_adapter,R.id.txt_single_item ,arrayList_Month);
        list_month.setAdapter(itemsAdapter);


        list_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
            String str_select_Year=arrayList_Month.get(position).toString();
                tv.setText(str_select_Year);
                dialog.dismiss();
            }
        });


        dialog.show();

    }






    public void  Make_Payment()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);
        System.out.println("-----commonId-------->"+user_id);


        HashMap<String, String> post = new HashMap<>();

        if(str_calling_type!=null && !str_calling_type.equals("") && str_calling_type.equalsIgnoreCase("extend"))
        {
            post.put("extend_no", str_Booking_Number);
            System.out.println("------Extent Booking No---->"+str_Booking_Number);
        }
        else
        {
            post.put("booking_no", str_Booking_Number);
        }

        post.put("card_number",str_post_card_number);
        post.put("expire_month", str_post_month);
        post.put("expire_year", str_post_year);
        post.put("security_code",str_card_security_code);


        Set keys = post.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) post.get(key);
            System.out.println(""+key+":"+value);
        }

        if(str_calling_type!=null && !str_calling_type.equals("") && str_calling_type.equalsIgnoreCase("extend"))
        {
            call = apiService.Extent_Payment(header,post);
        }
        else
        {
            call = apiService.Payment(header,post);
        }



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
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            Intent payment_intent = new Intent(Payment.this, Payment_Completed.class);
                            payment_intent.putExtra("total_amount",str_booking_amount);
                            startActivity(payment_intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                        else if(str_status_code.equals("0"))
                        {
                            String str_msg=response_obj.getString("msg");
                            Alert(getResources().getString(R.string.action_opps),str_msg);
                        }
                        else
                        {
                            Alert(getResources().getString(R.string.action_opps), getResources().getString(R.string.payment_failed));
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




    //------------------------snack bar---------------
    private void snack_bar(String title, String message)
    {
        String msg = title + "\n" + message;
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_snack_bar, null, false);
        TextView Tv_title = (TextView) view.findViewById(R.id.txt_alert);
        Tv_title.setText(title);
        AppMsg.Style style = new AppMsg.Style(AppMsg.LENGTH_SHORT, R.color.red);
        AppMsg snack = AppMsg.makeText(this, msg.toUpperCase(), AppMsg.STYLE_ALERT);
        snack.setView(view);
        snack.setDuration(650);
        snack.setLayoutGravity(Gravity.TOP);
        snack.setPriority(AppMsg.LENGTH_SHORT);
       // snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.setAnimation(R.anim.slide_down_anim, R.anim.slide_up_anim);
        snack.show();

    }


    public void Alert(final String str_title, final String str_message)
    {
        final PkDialog mDialog = new PkDialog(Payment.this);
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

    public void Alertsuccess(final String str_title, final String str_message)
    {
        final PkDialog mDialog = new PkDialog(Payment.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View_Data();
                mDialog.dismiss();

            }
        });

        mDialog.show();
    }













    //-----------------service---------------------
    public void View_Data()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", user_id);
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

                            JSONObject response_obj=object.getJSONObject("responseArr");
                            String str_status_code=response_obj.getString("status");
                            if(str_status_code.equalsIgnoreCase("1"))
                            {
                                JSONObject card_details=response_obj.getJSONObject("car_details");
                                edit_card_Number.setText(card_details.getString("cc_no"));
                                if(card_details.getString("cc_exp_date")!=null && !card_details.getString("cc_exp_date").equals(""))
                                {
                                    txt_Val_Month.setText(card_details.getString("cc_exp_date"));
                                }
                                else
                                {
                                    txt_Val_Month.setText("");
                                }

                                txt_Val_Year.setText("20"+card_details.getString("cc_exp_year"));
                                //txt_Val_Year.setText(card_details.getString("cc_exp_year"));
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




    public void load_paypal()
    {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("commonId", user_id);


        HashMap<String, String> post = new HashMap<>();

        if(str_calling_type!=null && !str_calling_type.equals("") && str_calling_type.equalsIgnoreCase("extend"))
        {
            post.put("extend_no", str_Booking_Number);
            System.out.println("------Extent Booking No---->"+str_Booking_Number);
        }
        else
        {
            post.put("booking_no", str_Booking_Number);
        }

        if(str_calling_type!=null && !str_calling_type.equals("") && str_calling_type.equalsIgnoreCase("extend"))
        {
            call = apiService.extent_paypal_driver_payment(header,post);
        }
        else
        {
            call = apiService.paypal_driver_payment(header,post);
        }


        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try
                {
                    Loader.dismiss();
                    System.out.println("------success----------");
                    if (!response.isSuccessful()) {
                        try {
                            Log.e("LOG", "Retrofit Response: " + response.errorBody().string());
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if (response != null && response.body() != null)
                    {
                        String Str_response = response.body().string();
                        Log.e("----Country Response-->", Str_response);
                        JSONObject object = new JSONObject(Str_response);
                        JSONObject response_obj=object.getJSONObject("responseArr");
                        String str_status_code=response_obj.getString("status");
                        if(str_status_code.equals("1"))
                        {
                            String str_payal_ur=response_obj.getString("paypalUrl");
                            Intent intent=new Intent(Payment.this,Paypal.class);
                             intent.putExtra("booking_no",str_payal_ur);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e)
                {
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


    public void Alert_ask(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.no), new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                //do


            }
        });

        mDialog.setNegativeButton(getResources().getString(R.string.yes), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                load_paypal();
            }
        });

        mDialog.show();
    }
}
