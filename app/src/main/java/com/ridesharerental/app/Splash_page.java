package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Config;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.services.gps.GPSTracker;
import com.ridesharerental.services.utils.ConnectionDetector;
import com.ridesharerental.services.utils.ConnectivityReceiver;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by user113 on 11/28/2017.
 */

public class Splash_page extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static int SPLASH_TIME_OUT = 2000;
    final int PERMISSION_REQUEST_CODE = 111;
    SessionManager sessionManager;
    private Boolean isInternetPresent = false;
    private ConnectionDetector cd;
    String user_id="";
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    GPSTracker gps;

    public  Common_Loader loader;
    String currentVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);

        sessionManager = new SessionManager(Splash_page.this);
        gps = new GPSTracker(Splash_page.this);
        mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        Log.i("Service", "Started");
        loader=new Common_Loader(Splash_page.this);
        try {
            currentVersion = Splash_page.this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //shopsy_check();

        String live_base_url= IConstant_WebService.baseurl;
        System.out.println("--------Live Url--------->"+live_base_url);


      /*  String core_base_url= IConstant_WebService.baseurl_core_team;
        System.out.println("--------Core Url--------->"+core_base_url);

        String rental_base_url= IConstant_WebService.baseurl_rental;
        System.out.println("--------rental Url--------->"+rental_base_url);*/

       // System.out.println("------Print all ----->"+ApiInterface.class());

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
        }
        catch (NoSuchAlgorithmException e) {
        }

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23)
                {
                    // Marshmallow+
                    if (!checkAccessFineLocationPermission() || !checkAccessCoarseLocationPermission() || !checkCamera() ||  !check_get_account() ||!checkWriteExternalStoragePermission() || !checksmssend() || !checkphonecall()) {
                        requestPermission();
                    } else {
                       // set_process();
                        GetVersionCode versionCode = new GetVersionCode();
                        versionCode.execute();
                    }
                } else {
                    //set_process();
                    GetVersionCode versionCode = new GetVersionCode();
                    versionCode.execute();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void set_process()
    {
        cd = new ConnectionDetector(Splash_page.this);
        isInternetPresent = cd.isConnectingToInternet();
        if(isInternetPresent)
        {
            if (gps.isgpsenabled()) {
                if (user_id.equals(""))
                {
                    Intent intent = new Intent(Splash_page.this, Slider_page.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else
                    {
                        //load_profile_data();
                    Intent intent = new Intent(Splash_page.this, Main_homepage.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            } else {
                enableGpsService();
            }
        }else{
            final PkDialog mDialog = new PkDialog(Splash_page.this);
            mDialog.setDialogTitle(getResources().getString(R.string.action_no_internet_title));
            mDialog.setDialogMessage(getResources().getString(R.string.action_no_internet_message));
            mDialog.setPositiveButton(getResources().getString(R.string.action_retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    set_process();
                }
            });
            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    finish();
                }
            });
            mDialog.show();
        }

    }

    private void enableGpsService() {

            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(30 * 1000);
            mLocationRequest.setFastestInterval(5 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);


            result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    Log.e("Location Success",""+status);
                    //final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:

                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            //...
                            launchNextScreen();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(Splash_page.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            //...
                            break;
                    }
                }
            });

    }



    private boolean checkAccessFineLocationPermission()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkAccessCoarseLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkWriteExternalStoragePermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checksmssend() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private boolean checkphonecall() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private boolean checkCamera()
    {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }


    private boolean check_get_account() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.SEND_SMS, android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.CAMERA,android.Manifest.permission.GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   //set_process();
                    GetVersionCode versionCode = new GetVersionCode();
                    versionCode.execute();
                } else  {
                    GetVersionCode versionCode = new GetVersionCode();
                    versionCode.execute();
                }
                break;
            case REQUEST_LOCATION:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //set_process();
//                    launchNextScreen();
//                } else {
//                    //finish();
//                    //launchNextScreen();
//                    AlertNew("Alert","To Continue, Please Enable the device Location.");
//                }
                break;
        }
    }


    public void load_profile_data()
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("commonId", user_id);
        Call<ResponseBody> call = apiService.show_driver_profile(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try {
                    loader.dismiss();
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
                        String str_status_code=response_obj.getString("status");

                        if(str_status_code.equalsIgnoreCase("1"))
                        {
                            JSONObject common_obj=object.getJSONObject("commonArr");
                            String str_msg_count=common_obj.getString("unread_message_count");
                            Intent intent = new Intent(Splash_page.this, Main_homepage.class);
                            intent.putExtra("unread_count",str_msg_count);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                        else
                        {
                            Intent intent = new Intent(Splash_page.this, Main_homepage.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }

                    }
                    else
                    {
                        Intent intent = new Intent(Splash_page.this, Main_homepage.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                } catch (Exception e)
                {
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
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }


    //Code to check playStore update version
    public class GetVersionCode extends AsyncTask<Void, String, String>

    {
        Common_Loader loader=new Common_Loader(Splash_page.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loader.show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = "";
            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(7)
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion)
        {
            super.onPostExecute(onlineVersion);

            if (!onlineVersion.equals(""))
            {
                if (onlineVersion != null && !onlineVersion.isEmpty())
                {
                    if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion))
                    {
                        if (Splash_page.this != null && !Splash_page.this.isFinishing())
                        {

                            Alert(getResources().getString(R.string.app_name), "There is newer version of this application available, click OK to upgrade now?");
                        }
                        //                Alert(getResources().getString(R.string.app_name), "There is newer version of this application available, click OK to upgrade now?");
                    } else {
                        set_process();
                    }
                }

            } else {
                set_process();
            }
            Log.d(" ", "Current version " + currentVersion + "play store version " + onlineVersion);
            loader.dismiss();
        }
    }


    private void Alert(String title, String alert)
    {
        try {
            final PkDialog mDialog = new PkDialog(Splash_page.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    finish();
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            mDialog.setNegativeButton(getResources().getString(R.string.action_cancel_small), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    //finish();
                    Alert(getResources().getString(R.string.app_name), "There is newer version of this application available, click OK to upgrade now?");

                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(Splash_page.this);
    }

    private void launchNextScreen()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if (user_id.equals(""))
                {
                    Intent intent = new Intent(Splash_page.this, Slider_page.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else
                {
                    //load_profile_data();
                    Intent intent = new Intent(Splash_page.this, Main_homepage.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        }, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        launchNextScreen();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // finish();
                        //launchNextScreen();
                        AlertNew("Alert","To Continue, Please Enable the device Location.");
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }


    private void AlertNew(String title, String alert)
    {
        try {
            final PkDialog mDialog = new PkDialog(Splash_page.this);
            mDialog.setDialogTitle(title);
            mDialog.setDialogMessage(alert);
            mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    enableGpsService();

                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
