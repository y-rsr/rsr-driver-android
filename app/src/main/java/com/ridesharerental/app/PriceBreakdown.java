package com.ridesharerental.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ridesharerental.adapter.PricingAdapter;
import com.ridesharerental.pojo.PricingPojo;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.ExpandableHeightListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PriceBreakdown extends Activity {
    Common_Loader Loader;
    String car_Name = "", car_hostname = "", checkIndate = "", checkoutdate = "", CarId = "",deductibleId="";
    ImageView img_close;
    SessionManager sessionManager;

    TextView txt_length, txt_rental_price, txt_car_name, txt_host_name, txt_insurance_price, txt_transcation_price, txt_check_in, txt_check_out, txt_onetime_check, txt_service_fee, txt_totalprice;
    RelativeLayout layout_total, layout_rentalTax, layout_onetimecheck, layout_rentalTransaction, layout_rentalinsurance, layout_Rentalprice, layout_Rentallength;
    ExpandableHeightListView pricingListView;
    ArrayList<PricingPojo> PriceARR = new ArrayList<PricingPojo>();

    RelativeLayout wholelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_breakdown);
        sessionManager = new SessionManager(PriceBreakdown.this);
        Loader = new Common_Loader(PriceBreakdown.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            car_Name = bundle.getString("Car_Name");
            car_hostname = bundle.getString("Car_HostName");
            checkIndate = bundle.getString("Car_CheckIn");
            checkoutdate = bundle.getString("Car_CheckOut");
            CarId = bundle.getString("CarId");
            deductibleId = bundle.getString("deductibleId");
        }

        init();
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                PriceBreakdown.this.finish();

            }
        });
    }

    public void init() {
        img_close = (ImageView) findViewById(R.id.advance_filter_closeIMG);
        txt_length = (TextView) findViewById(R.id.txt_length);
        txt_rental_price = (TextView) findViewById(R.id.txt_rental_price);

        wholelayout = (RelativeLayout)findViewById(R.id.reservation_extent_layout1);
        pricingListView = (ExpandableHeightListView) findViewById(R.id.pricingListView);
        wholelayout.setVisibility(View.GONE);
        txt_check_in = (TextView) findViewById(R.id.txt_check_in);
        txt_check_out = (TextView) findViewById(R.id.txt_check_out);
        txt_host_name = (TextView) findViewById(R.id.txt_host_name);
        txt_car_name = (TextView) findViewById(R.id.txt_car_name);
        txt_insurance_price = (TextView) findViewById(R.id.txt_insurance_price);
        txt_transcation_price = (TextView) findViewById(R.id.txt_transcation_price);
        txt_onetime_check = (TextView) findViewById(R.id.txt_onetime_check);
        txt_service_fee = (TextView) findViewById(R.id.txt_service_fee);
        txt_totalprice = (TextView) findViewById(R.id.txt_totalprice);

        layout_total = (RelativeLayout) findViewById(R.id.layout_total);
        layout_rentalTax = (RelativeLayout) findViewById(R.id.layout_rentalTax);
        layout_onetimecheck = (RelativeLayout) findViewById(R.id.layout_onetimecheck);
        layout_rentalTransaction = (RelativeLayout) findViewById(R.id.layout_rentalTransaction);
        layout_rentalinsurance = (RelativeLayout) findViewById(R.id.layout_rentalinsurance);
        layout_Rentalprice = (RelativeLayout) findViewById(R.id.layout_Rentalprice);
        layout_Rentallength = (RelativeLayout) findViewById(R.id.layout_Rentallength);

        txt_car_name.setText(car_Name);
        txt_host_name.setText(car_hostname);
        Load_Data();
    }



    //-----------------service---------------------
    public void Load_Data() {
        Loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> header = new HashMap<>();
        header.put("CommonId", sessionManager.getUserDetails().get(sessionManager.KEY_USER_ID));

        HashMap<String, String> map = new HashMap<>();
        map.put("carId", CarId);
        map.put("date_from", checkIndate);
        map.put("date_to", checkoutdate);
        map.put("deductible", deductibleId);

        System.out.println("----deductible-----------"+deductibleId);


        Call<ResponseBody> call = apiService.pricing(header, map);
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
                    } else
                    {

                        if (response != null && response.body() != null) {
                            String Str_response = response.body().string();
                            Log.e("----Pricing Response-->", Str_response);
                            JSONObject object = new JSONObject(Str_response);

                            if (object.getJSONObject("responseArr").getString("status").equalsIgnoreCase("1")) {
                                JSONObject data = object.getJSONObject("responseArr");
                                JSONObject data1 = object.getJSONObject("commonArr");
                                JSONArray price = data.getJSONArray("pricingArr");
                                if (price.length() > 0) {
                                    PriceARR.clear();
                                    for (int priceARR = 0; priceARR < price.length(); priceARR++) {
                                        JSONObject obj = price.getJSONObject(priceARR);
                                        if (obj.length() > 0) {
                                            if (obj.getString("key").equalsIgnoreCase("Date From")) {
                                                if (obj.getString("value") != null && !obj.getString("value").equalsIgnoreCase("null") && !obj.getString("value").equalsIgnoreCase(null)) {
                                                   // String date = converdateformatservice((obj.getString("value")));
                                                    txt_check_in.setText(obj.getString("value"));
                                                }
                                            }
                                            if (obj.getString("key").equalsIgnoreCase("Date To")) {
                                                if (obj.getString("value") != null && !obj.getString("value").equalsIgnoreCase("null") && !obj.getString("value").equalsIgnoreCase(null)) {
                                                    //String date = converdateformatservice((obj.getString("value")));
                                                    txt_check_out.setText(obj.getString("value"));
                                                }
                                            }
                                            if (!obj.getString("key").equalsIgnoreCase("Date From") && !obj.getString("key").equalsIgnoreCase("Date To")) {

                                                PricingPojo pricepojo = new PricingPojo();
                                                pricepojo.setKey(obj.getString("key"));
                                                pricepojo.setAmount(obj.getString("value"));
                                                PriceARR.add(pricepojo);
                                            }
                                        }
                                    }
                                    if(PriceARR.size()>0)
                                    {
                                        PricingAdapter padapter = new PricingAdapter(PriceBreakdown.this, PriceARR , data1.getString("currencySymbol"));
                                        pricingListView.setAdapter(padapter);
                                        pricingListView.setExpanded(true);
                                    }

                                }
                                wholelayout.setVisibility(View.VISIBLE);

                            } else {
                                finish();
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
