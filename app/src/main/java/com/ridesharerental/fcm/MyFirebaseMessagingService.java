package com.ridesharerental.fcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ridesharerental.app.Main_homepage;
import com.ridesharerental.app.R;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by user145 on 8/4/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private String myPropertyId = "", myBookingId = "", myReceiverId = "", myReceiverName = "",myBookingMsgSTR = "";
    SessionManager mySession;


    @Override
    public void onCreate() {
        mySession = new SessionManager(MyFirebaseMessagingService.this);
        super.onCreate();
        Log.v(TAG, "Service Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Service Destroyed...");
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        System.out.println("--------FCM Received-------" + remoteMessage);
//        Toast.makeText(MyFirebaseMessagingService.this,"Receive Message==>"+remoteMessage,Toast.LENGTH_SHORT).show();
        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
               // JSONArray array = new JSONArray(params);
               // Log.e("JSON_OBJECT NEW_Array", array.toString());

                Log.e("JSON_OBJECT NEW", object.toString());
                //JSONObject json = new JSONObject(remoteMessage.toString());
                if(object!=null)
                {
                    handleDataMessage(object);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }


    private void handleDataMessage(final JSONObject json)
    {
        Log.e(TAG, "push json: " + json.toString());


        try {

           // JSONObject object1 = json.getJSONObject("data");
           // Log.e("FCM Notification", object1.toString(1));
            //showBookingNotification(json.getString("m"));
            System.out.println("---------Key----->"+json.getString("k"));
            System.out.println("---------Message----->"+json.getString("m"));


            if(json.getString("m")!=null && !json.getString("m").equals(""))
            {
                showNotification(json.getString("m"),json.getString("k"));

            }



          /*  new Thread()
            {
                @Override
                public void run()
                {
                    try {
                        if(json.getString("m")!=null && !json.getString("m").equals(""))
                        {
                            System.out.println("---------Handler------------->");
                            Alert_sucess("RideShare Rental",json.getString("m"),json.getString("k"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.start();*/

            /*if (object1.has("property")) {

                myPropertyId = object1.getString("property");
                myBookingId = object1.getString("booking_id");

                if (mySession.getUserDetails().getUserId().equalsIgnoreCase(object1.getString("host")))
                {
                    myReceiverId = object1.getString("user");
                } else {
                    myReceiverId = object1.getString("host");
                }

                myReceiverName = object1.getString("receiver_name");

                if (mySession.isNotiticationAllowed() && !ChatUpcomingFragment.isInChatPage) {
                    showNotification(object1.getString("message"));
                } else {
                    Log.e("Push notification", "not enable in settings");
                }
            } else {
                myBookingMsgSTR = object1.getString("message");

                if (mySession.isNotiticationAllowed()){
                    showBookingNotification(object1.getString("message"));
                }
            }*/


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }


    }

    private void showBookingNotification(String aMsg)
    {
        Intent notificationIntent = null;
        try {

            notificationIntent = new Intent(getApplicationContext(), Main_homepage.class);
            notificationIntent.putExtra("KEY", "bookhotel");
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Resources res = this.getResources();
            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext());
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.app_icon))
                    .setTicker(aMsg)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setLights(0xffff0000, 100, 2000)
                    .setPriority(Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText(aMsg)
                    .setContentIntent(contentIntent);
            Notification n = builder.getNotification();
            n.defaults |= Notification.DEFAULT_ALL;
            nm.notify(0, n);


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String aMsg,String key)
    {

       /* NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContent(contentView);*/


        Intent notificationIntent = null;
        try {

            notificationIntent = new Intent(getApplicationContext(), Main_homepage.class);
            notificationIntent.putExtra("calling_type", key);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Resources res = this.getResources();

            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext());
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.app_icon))
                    .setTicker(aMsg)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setLights(0xffff0000, 100, 2000)
                    .setPriority(Notification.DEFAULT_SOUND)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentText(aMsg)
                    .setContentIntent(contentIntent);

           /* RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
            contentView.setImageViewResource(R.id.image, R.mipmap.app_icon);
            contentView.setTextViewText(R.id.title, "Rideshare Rental");
            contentView.setTextViewText(R.id.message, aMsg);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContent(contentView);*/


            Notification n = builder.getNotification();
            n.defaults |= Notification.DEFAULT_ALL;
            nm.notify(0, n);


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }




    public void Alert_sucess(final String str_title,final  String str_message,final String key_1)
    {
        final PkDialog mDialog = new PkDialog(getApplicationContext());
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);


        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                Intent notificationIntent = null;
                try {

                    notificationIntent = new Intent(getApplicationContext(), Main_homepage.class);
                    notificationIntent.putExtra("calling_type", key_1);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    Resources res = getApplicationContext().getResources();
                    android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(getApplicationContext());
                    builder.setContentIntent(contentIntent)
                            .setSmallIcon(R.mipmap.app_icon)
                            .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.app_icon))
                            .setTicker(str_message)
                            .setWhen(System.currentTimeMillis())
                            .setAutoCancel(true)
                            .setContentTitle(getResources().getString(R.string.app_name))
                            .setLights(0xffff0000, 100, 2000)
                            .setPriority(Notification.DEFAULT_SOUND)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setContentText(str_message)
                            .setContentIntent(contentIntent);
                    Notification n = builder.getNotification();
                    n.defaults |= Notification.DEFAULT_ALL;
                    nm.notify(0, n);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        mDialog.show();
    }

}