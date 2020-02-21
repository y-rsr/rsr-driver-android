package com.ridesharerental.services.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ridesharerental.app.MyApplication;


/**
 * Created by user65 on 3/14/2018.
 */

public class ConnectivityReceiver
        extends BroadcastReceiver
{

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null)
        {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);

            /*if(isConnected())
            {
               Toast.makeText(context,"-----------true----------------->",Toast.LENGTH_SHORT).show();
                snack_bar("","",context);
                //Snackbar snackbar = Snackbar.make(MyApplication.appactivity.findViewById(android.R.id.content), "Success", Snackbar.LENGTH_INDEFINITE);
               // snackbar.show();
            }
            else
            {
               Toast.makeText(context,"-----------false----------------->",Toast.LENGTH_SHORT).show();
               // Snackbar snackbar = Snackbar.make(MyApplication.appactivity.findViewById(android.R.id.content), "Check your internet connection", Snackbar.LENGTH_INDEFINITE);
              //  snackbar.show();
            }*/
        }

    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}