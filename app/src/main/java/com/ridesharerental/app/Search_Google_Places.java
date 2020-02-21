package com.ridesharerental.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ridesharerental.pojo.Search_Bean;
import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.gps.PlaceDetailsJSONParser;
import com.ridesharerental.services.gps.PlaceJSONParser;
import com.ridesharerental.widgets.Common_Loader;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.value;
import static com.google.ads.AdRequest.LOGTAG;

/**
 * Created by user65 on 12/12/2017.
 */

public class Search_Google_Places extends Activity
{
    AutoCompleteTextView ed_search;
    ListView list_location;
    final int PLACES_DETAILS = 1;
    final int PLACES = 0;
    ParserTask placeDetailsParserTask;
    ParserTask placesParserTask;
    ArrayList<Search_Bean> array_temp = new ArrayList<>();
    String str_location_name = "";
    Common_Loader loader;
    Context ctx;
    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_google_places);
        loader = new Common_Loader(Search_Google_Places.this);
        ctx = getApplicationContext();
        init();
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {


                if(checkGooglePlayServicesAvailable()==true)
                {
                    array_temp.clear();
                    if (s.length() > -1)
                    {
                        s = s.toString().toLowerCase().replace(" ", "%20");
                        DownloadTask placesDownloadTask = new DownloadTask(PLACES);
                        String url = null;
                        try {
                            System.out.println("---------Charseq--------------->" + s);

                            if (isConnectingToInternet()) {
                                s = URLEncoder.encode(s.toString(), "UTF-8").replace("%2520", "%20");
                                url = getAutoCompleteUrl(s.toString());
                            }

                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if(url!=null && !url.equals(""))
                        {
                        System.out.println("Location url-------------------->" + url.toString());
                        placesDownloadTask.execute(url);
                        }

                        //System.out.println("Location url------rag-------------->"+url);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        list_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (array_temp.size() > 0) {
                    if(checkGooglePlayServicesAvailable()==true)
                    {
                        str_location_name = array_temp.get(position).getLoc_name();
                        System.out.println("---------onclick success-----------" + value);
                        loader.show();
                        SimpleAdapter adapter = (SimpleAdapter) parent.getAdapter();
                        @SuppressWarnings("unchecked")
                        HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(position);
                        DownloadTask placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);
                        String url = getPlaceDetailsUrl(hm.get("reference"));
                        placeDetailsDownloadTask.execute(url);
                    }
                }
            }
        });
    }


    public void init()
    {
        ed_search = (AutoCompleteTextView) findViewById(R.id.edit_search);
        list_location = (ListView) findViewById(R.id.list_country_data);
        list_location.setClickable(true);
        list_location.setTextFilterEnabled(true);
    }



    private String getPlaceDetailsUrl(String ref) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=" + IConstant_WebService.Google_Map_Place_Search_Api;

        // reference of place
        String reference = "reference=" + ref;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = reference + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/" + output + "?" + parameters;

        return url;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private int downloadType = 0;

        // Constructor
        public DownloadTask(int type) {
            this.downloadType = type;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            switch (downloadType) {
                case PLACES:
                    // Creating ParserTask for parsing Google Places
                    placesParserTask = new ParserTask(PLACES);

                    // Start parsing google places json data
                    // This causes to execute doInBackground() of ParserTask class
                    placesParserTask.execute(result);

                    break;

                case PLACES_DETAILS:
                    // Creating ParserTask for parsing Google Places
                    placeDetailsParserTask = new ParserTask(PLACES_DETAILS);

                    // Starting Parsing the JSON string
                    // This causes to execute doInBackground() of ParserTask class
                    placeDetailsParserTask.execute(result);
            }
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            Log.d("URL CHECK3243", strUrl);
            URL url = new URL(strUrl);
            Log.d("URL CHECK", url.toString());

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            Log.d("buffered", br.toString());
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("data", data);
            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        int parserType = 0;

        public ParserTask(int type) {
            this.parserType = type;
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            JSONObject jObject;
            List<HashMap<String, String>> list = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                switch (parserType) {
                    case PLACES:
                        PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeJsonParser.parse(jObject);
                        loader.dismiss();
                        break;
                    case PLACES_DETAILS:
                        PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                        // Getting the parsed data as a List construct
                        list = placeDetailsJsonParser.parse(jObject);
                        loader.dismiss();
                        Log.d("Location-----------place_detail", "" + list);
                }

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            Log.d("Location-----------Result", "" + result);
            switch (parserType) {
                case PLACES:
                    loader.dismiss();
                    // ArrayList<String> add_data=new ArrayList<>();
                    JSONArray array = new JSONArray(result);
                    if (array.length() > 0) {
                        // array_temp.clear();
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                Search_Bean bean = new Search_Bean();
                                JSONObject object = array.getJSONObject(i);
                                String place = object.getString("description");
                                bean.setLoc_name(place);
                                System.out.println("-----My seaech place----->" + place);
                                array_temp.add(bean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        String[] from = new String[]{"description"};
                        // System.out.println("---ggggggg----->" + from);
                        //System.out.println("---rrrr----->" + from[0]);
                        int[] to = new int[]{R.id.list_valc};
                        // int[] to = new int[]{R.id.list_val};
                        Log.d("From-------", from.toString());
                        Log.d("To", to.toString());
                        SimpleAdapter adapter = new SimpleAdapter(ctx, result, R.layout.county_adapter_layout, from, to);
                        //SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result,R.layout.county_adapter_layout, from, to);
                        list_location.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        list_location.setItemsCanFocus(true);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                   /* try {
                        adapter11 = new Google_Map_Search_Adapter(Map_Search.this, array_temp);
                        list_location.setAdapter(adapter11);
                        adapter1.notifyDataSetChanged();
                    } catch (Exception e) {
                    }*/
                    break;
                case PLACES_DETAILS:
                    if (isConnectingToInternet() == true) {
                        loader.dismiss();
                        double latitude = 0;
                        double longitude = 0;
                        HashMap<String, String> hm = result.get(0);
                        latitude = Double.parseDouble(hm.get("lat"));
                        longitude = Double.parseDouble(hm.get("lng"));
                        Log.e("latitude----->", "" + latitude);
                        Log.e("longitude----->", "" + longitude);
                        Intent returnIntent = new Intent();
                        if (str_location_name != null && !str_location_name.equals(""))
                        {
                            returnIntent.putExtra("location_name", str_location_name);
                            returnIntent.putExtra("latitude", String.valueOf(latitude));
                            returnIntent.putExtra("langitude", String.valueOf(longitude));
                            setResult(Activity.RESULT_OK, returnIntent);
                            Search_Google_Places.this.finish();
                        }

                    } else {
                        loader.dismiss();
                    }
                    break;
            }
        }
    }

    private String getAutoCompleteUrl(String place) {

        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=" + IConstant_WebService.Google_Map_Place_Search_Api;

        // place to be be searched
        String input = "input=" + place;

        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input + "&" + types + "&" + sensor + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

        return url;
    }

    public Boolean isConnectingToInternet() {
        Boolean isConnected = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            isConnected = info != null && info.isAvailable() && info.isConnected();
            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("isConnected: ", "" + isConnected);
        return isConnected;
    }


    private boolean checkGooglePlayServicesAvailable()
    {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS)
        {
            return true;
        }

        Log.e(LOGTAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));

        if (GooglePlayServicesUtil.isUserRecoverableError(status))
        {
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            if (errorDialog != null)
            {
                errorDialog.show();
            }
        }

        return false;
    }

}
