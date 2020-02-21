package com.ridesharerental.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SyncAdapterType;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.ridesharerental.adapter.MainPageAdapter;
import com.ridesharerental.facebook.AsyncFacebookRunner;
import com.ridesharerental.facebook.DialogError;
import com.ridesharerental.facebook.Facebook;
import com.ridesharerental.facebook.FacebookError;
import com.ridesharerental.facebook.Util;
import com.ridesharerental.pushnotification.GCMInitializer;
import com.ridesharerental.retrofit.ApiClient;
import com.ridesharerental.retrofit.ApiInterface;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.ActivitySwitcher;
import com.ridesharerental.widgets.Common_Loader;
import com.ridesharerental.widgets.RippleView;
import com.ridesharerental.widgets.dialog.PkDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Slider_page extends AppCompatActivity implements RippleView.OnRippleCompleteListener, GoogleApiClient.OnConnectionFailedListener {




    ImageView img_twitter;

    SessionManager session;
    String str_get_user_email="",str_get_user_name="",str_get_user_id="",str_get_user_iamge;

    Common_Loader loader;
    String str_get_first_name = "", str_get_last_name = "", str_get_email = "", str_get_profile_image = "", str_common_id = "", str_fb_user_id = "",gmail_personPhotoUrl1="";
    String str_email = "";
    String GCM_Id="";
    RelativeLayout Rl_driver,Rl_claimant;
    ImageView down_arrow;
    TextView txt_string_with_html;
    LinearLayout claim_layout;
    ScrollView scroll_view;

    //------------------facebook---------------------
    private SharedPreferences mPrefs;
    private static String APP_ID = "131759277438692";
    private Facebook facebook = new Facebook(APP_ID);
    private AsyncFacebookRunner mAsyncRunner;


    //---------------google plus login--------
    Button btn_sign_with_google;
    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    public static final String PROFILE_USER_ID = "USER_ID";
    public static final String PROFILE_DISPLAY_NAME = "PROFILE_DISPLAY_NAME";
    public static final String PROFILE_USER_EMAIL = "USER_PROFILE_EMAIL";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    Context ctx;

    ImageView img_facebook,img_Google;

    private TextView txt_Login;
    private RippleView mySignRPLVW,Ripple_Login,Ripple_login_with_gmail;
    private AutoScrollViewPager myViewPager;
    private MainPageAdapter myAdapter;
    int[] myImageInt = {
            R.drawable.slider_1,
            R.drawable.slider_1,
            R.drawable.slider_1,
            R.drawable.slider_1,
            R.drawable.slider_1,
    };

    private CircleIndicator myViewPageIndicator;

    private GoogleApiClient client;

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MainPage Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class Receiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("com.app.device.back.button.pressed"))
            {
                finish();
            }
        }
    }
    private Receiver receive;

    String[] title;
    String[] text;
    String[] text1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_new_page);
        ctx=getApplicationContext();
        loader=new Common_Loader(Slider_page.this);
        session=new SessionManager(Slider_page.this);
        //------------------google plus------------
        gso = ((MyApplication) getApplication()).getGoogleSignInOptions();
        mGoogleApiClient = ((MyApplication) getApplication()).getGoogleApiClient(Slider_page.this, this);
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        txt_string_with_html = findViewById(R.id.txt_string_with_html);

//        String s1 = getResources().getString(R.string.string_with_html);
//        String s2 =  getResources().getString(R.string.string_with_html1);
//        SpannableString ss1=  new SpannableString(s1);
//        ss1.setSpan(new StyleSpan(Typeface.BOLD), 0, ss1.length(), 0);
//        txt_string_with_html.append(ss1);
//        txt_string_with_html.append(", ");
//        txt_string_with_html.append(s2);

        String sourceString = "<b>" + getResources().getString(R.string.string_with_html) + "</b> , " + getResources().getString(R.string.string_with_html1);
        txt_string_with_html.setText(Html.fromHtml(sourceString));

        title = new String[]{getResources().getString(R.string.book_screen), getResources().getString(R.string.payment_screen), getResources().getString(R.string.alert_screen)
                 , getResources().getString(R.string.track_screen), getResources().getString(R.string.chat_screen)};
        text = new String[]{getResources().getString(R.string.book_text), getResources().getString(R.string.payment_text), getResources().getString(R.string.alert_text)
                 , getResources().getString(R.string.track_text), getResources().getString(R.string.chat_text)};
        text1 = new String[]{getResources().getString(R.string.book_text1), getResources().getString(R.string.payment_text1), getResources().getString(R.string.alert_text1)
               , getResources().getString(R.string.track_text1), getResources().getString(R.string.chat_text1)};
       // check();
        initilize();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Slider_page.this, Login_page.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Slider_page.this.finish();

            }
        });

        img_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                /*String url = "https://twitter.com/RideShareCars";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

                String url = "https://twitter.com/RideShareCars";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                //sendShareTwit();
            }
        });

        img_facebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                /*String url = "https://www.facebook.com/ridesharerental/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

                String url = "https://www.facebook.com/ridesharerental/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

               // send_Facebook();
               // logoutFromFacebook();
               // loginToFacebook();
            }
        });

        img_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

               /* String url = "https://www.instagram.com/ridesharerental/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);*/

                String url = "https://www.instagram.com/ridesharerental/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

                //send_instagram();
               /* signOutFromGplus();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);*/
            }
        });


        Rl_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Slider_page.this, DriverClaimActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });


        Rl_claimant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(Slider_page.this, DriverClaimentActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(claim_layout.getVisibility() == View.VISIBLE){

                    down_arrow.setRotation(180);

                    claim_layout.setVisibility(View.GONE);
//                    scroll_view.scrollTo(0, 0);

                    scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll_view.fullScroll(View.FOCUS_UP);
//                            ObjectAnimator.ofInt(scroll_view, "scrollY",  scroll_view.getTop()).setDuration(500).start();
                        }
                    });




                }else {


                    down_arrow.setRotation(0);
//                    scroll_view.scrollTo(0, scroll_view.getBottom());
                    claim_layout.setVisibility(View.VISIBLE);
                    scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll_view.fullScroll(View.FOCUS_DOWN);
//                            ObjectAnimator.ofInt(scroll_view, "scrollY",  scroll_view.getBottom()).setDuration(500).start();
                        }
                    });



                }


            }
        });



    }

    public void initilize()
    {


        GCMInitializer initializer = new GCMInitializer(Slider_page.this, new GCMInitializer.CallBack() {
            @Override
            public void onRegisterComplete(String registrationId) {
                GCM_Id = registrationId;
                System.out.println("-----GCM id-------->" + GCM_Id);
            }

            @Override
            public void onError(String errorMsg) {
            }
        });
        initializer.init();

        img_facebook=(ImageView)findViewById(R.id.img_facebook);
        img_Google=(ImageView)findViewById(R.id.img_google);
        img_twitter=(ImageView)findViewById(R.id.img_twitter);

        txt_Login=(TextView)findViewById(R.id.txt_login_now);
        mySignRPLVW = (RippleView) findViewById(R.id.main_skip);
        Ripple_Login=(RippleView)findViewById(R.id.main_login);
        Ripple_login_with_gmail=(RippleView)findViewById(R.id.main_login_google_plus);
        Rl_driver = (RelativeLayout) findViewById(R.id.Rl_driver);
        Rl_claimant = (RelativeLayout) findViewById(R.id.Rl_claimant);
        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        claim_layout = (LinearLayout) findViewById(R.id.claim_layout);
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);

        myViewPager = (AutoScrollViewPager) findViewById(R.id.main_page_VWPGR);
        myViewPageIndicator = (CircleIndicator) findViewById(R.id.main_page_VWPGR_indicator);
       // loadData();

        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        down_arrow.startAnimation(pulse);

        clickListener();
        receive = new Receiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.app.device.back.button.pressed");
        registerReceiver(receive,intentFilter);
    }

    private void clickListener() {
        mySignRPLVW.setOnRippleCompleteListener(this);
        Ripple_Login.setOnRippleCompleteListener(this);
        Ripple_login_with_gmail.setOnRippleCompleteListener(this);

    }

    private void loadData() {
        try {
            myAdapter = new MainPageAdapter(getApplicationContext(), myImageInt,title,text,text1);
            myViewPager.setAdapter(myAdapter);
            myViewPageIndicator.setViewPager(myViewPager);
            myViewPager.startAutoScroll();
            myViewPager.setInterval(2900);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (receive != null) {
               unregisterReceiver(receive);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onComplete(RippleView rippleView)
    {
        switch (rippleView.getId()) {
            case R.id.main_skip:
                Intent intent = new Intent(Slider_page.this, Sign_up_page.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                Slider_page.this.finish();

               // animatedStartActivity();
                break;

            case R.id.main_login:
                Intent intent1 = new Intent(Slider_page.this, Login_page.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                Slider_page.this.finish();

                // animatedStartActivity();
                break;



            case R.id.main_login_google_plus:
                signOutFromGplus();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);

                // animatedStartActivity();
                break;

        }
    }



    private void animatedStartActivity()
    {
        // we only animateOut this activity here.
        // The new activity will animateIn from its onResume() - be sure to
        // implement it.
        final Intent intent = new Intent(Slider_page.this,
                Sign_up_page.class);
        // disable default animation for new intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        ActivitySwitcher.animationOut(findViewById(R.id.container),
                getWindowManager(),
                new ActivitySwitcher.AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished()
                    {
                        startActivity(intent);
                    }
                });
    }


    /*@Override
    protected void onResume() {
        // animateIn this activity
        ActivitySwitcher.animationIn(findViewById(R.id.container),
                getWindowManager());
        super.onResume();
    }*/


    public void check()
    {
    try
    {
       // JSONObject json = new JSONObject();
        JSONArray jsonArray_attribute=new JSONArray();


        JSONObject student1 = new JSONObject();
        try {
            student1.put("id", "594d0a3d99985be3363f95f1");
            student1.put("slug", "0p");
            student1.put("slug", "0p");
            student1.put("name ", "Size");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject student2 = new JSONObject();
        try {
            student2.put("id", "598c733dd73c1fdd50f268cc");
            student2.put("slug", "multi-color-01");
            student2.put("ColorCode", "green");
            student2.put("name", "Color");
            student2.put("value", "Multi Color");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        jsonArray_attribute.put(student1);
        jsonArray_attribute.put(student2);

        JSONObject studentsObj = new JSONObject();

        studentsObj.put("attributes=", jsonArray_attribute);
        studentsObj.put("category = ", jsonArray_attribute);





        System.out.println("------Display------->"+studentsObj.toString());



    }catch (Exception e)
    {
        e.printStackTrace();
    }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            facebook.authorizeCallback(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }


    }



    private void handleSignInResult(GoogleSignInResult result)
    {
        requestSyncForAccounts();
        getUsernameLong(ctx,result);
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        /*if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount userAccount = result.getSignInAccount();
            System.out.println("------User Account------>"+userAccount);
            if(userAccount.getPhotoUrl()!=null && !userAccount.getPhotoUrl().equals(""))
            {
                gmail_personPhotoUrl1 = userAccount.getPhotoUrl().toString();
                System.out.println("------gmail profile image url-------->"+gmail_personPhotoUrl1);
            }
            String userId = userAccount.getId();
            String displayedUsername="";
            if(userAccount.getDisplayName()!=null && !userAccount.getDisplayName().equals(""))
            {
                displayedUsername = userAccount.getDisplayName();
            }
            else
            {
                displayedUsername = userAccount.getGivenName();
            }
            str_email = userAccount.getEmail();

            if(str_email!=null && !str_email.equals(""))
            {

                if(displayedUsername!=null && !displayedUsername.equals(""))
                {
                    try {
                        String[] parts = displayedUsername.split(" ");
                        str_get_first_name = parts[0];
                        str_get_last_name = parts[1];
                    }catch (Exception e)
                    {
                    }
                }
                System.out.println("-----User Id---->"+userId);
                System.out.println("----User Name----->"+displayedUsername);
                System.out.println("-----User Email---->"+str_email);
                facebook_login("google");
                // String userProfilePhoto = userAccount.getPhotoUrl().toString();
            }

        }*/
    }


    private void requestSyncForAccounts() {
        SyncAdapterType[] syncAdapters = ContentResolver.getSyncAdapterTypes();
        Bundle extras = new Bundle();
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        Account[] accounts = AccountManager.get(Slider_page.this).getAccounts();
        for (Account account : accounts) {
            for (int j = 0; j < syncAdapters.length; j++) {
                SyncAdapterType sa = syncAdapters[j];
                if (ContentResolver.getSyncAutomatically(account, sa.authority)) {
                    ContentResolver.requestSync(account, sa.authority, extras);
                }
            }
        }
    }




    public  String getUsernameLong(Context context,GoogleSignInResult result)
    {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();
        for (Account account : accounts) {
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
            Log.i("DGEN ACCOUNT","CALENDAR LIST ACCOUNT/"+account.name);
        }
        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null)
        {
            String email = possibleEmails.get(0);
            if (result.isSuccess())
            {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount userAccount = result.getSignInAccount();
                System.out.println("------User Account------>"+userAccount);
                if(userAccount.getPhotoUrl()!=null && !userAccount.getPhotoUrl().equals(""))
                {
                     gmail_personPhotoUrl1 = userAccount.getPhotoUrl().toString();
                    System.out.println("------gmail profile image url-------->"+gmail_personPhotoUrl1);
                }

                String userId = userAccount.getId();
                String displayedUsername="";
                if(userAccount.getDisplayName()!=null && !userAccount.getDisplayName().equals(""))
                {
                    displayedUsername = userAccount.getDisplayName();
                }

                 str_email = userAccount.getEmail();

                if(str_email!=null && !str_email.equals(""))
                {
                    if(userAccount.getDisplayName()!=null && !userAccount.getDisplayName().equals(""))
                    {
                        try {
                            displayedUsername = userAccount.getDisplayName();
                            String[] parts = displayedUsername.split(" ");
                           // str_get_first_name = parts[0];
                           // str_get_last_name = parts[1];
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    str_get_first_name=displayedUsername;
                    System.out.println("-----User Id---->"+userId);
                    System.out.println("----User Name----->"+displayedUsername);
                    System.out.println("-----User Email---->"+str_email);
                    save_data("google");
                }

            }
            return email;

        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }




    private void signOutFromGplus() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);

                    }
                });
    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            // btnSignIn.setVisibility(View.GONE);
			/*
			 * btnSignOut.setVisibility(View.VISIBLE);
			 * btnRevokeAccess.setVisibility(View.VISIBLE);
			 * llProfileLayout.setVisibility(View.VISIBLE);
			 */
        } else {
            // btnSignIn.setVisibility(View.VISIBLE);
			/*
			 * btnSignOut.setVisibility(View.GONE);
			 * btnRevokeAccess.setVisibility(View.GONE);
			 * llProfileLayout.setVisibility(View.GONE);
			 */
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)) {
            return true;
        }
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0))
        {
            alartExit();
            return true;
        }
        return false;
    }
    private void alartExit()
    {
        AlertDialog dialog = new AlertDialog.Builder(Slider_page.this)
                .setMessage(getString(R.string.Are_you_Exit))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Sofia Pro Regular.ttf");
        textView.setTypeface(face);
        nbutton.setTypeface(face);
        pbutton.setTypeface(face);
    }
    //------------------------login facebook method---------------
    public void loginToFacebook()
    {
        mPrefs = Slider_page.this.getSharedPreferences("CASPreferences", Context.MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);

        if (access_token != null) {
            facebook.setAccessToken(access_token);
        }

        if (expires != 0) {
            facebook.setAccessExpires(expires);
        }

        if (!facebook.isSessionValid()) {
            facebook.authorize(this,
                    new String[]{"email", "publish_actions"},
                    new Facebook.DialogListener() {
                        @Override
                        public void onCancel() {
                            // Function to handle cancel event
                        }
                        @Override
                        public void onComplete(Bundle values) {
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("access_token",
                                    facebook.getAccessToken());
                            editor.putLong("access_expires",
                                    facebook.getAccessExpires());
                            editor.commit();
                            String accessToken = facebook.getAccessToken();
                            System.out.println("----done----------->" + facebook.getAccessToken());
                            //getProfileInformation(accessToken);
                            String facebook_info = "https://graph.facebook.com/me?fields=id,name,picture,email&access_token=" + accessToken;
                            System.out.println("------facebbok info----->" + facebook_info);
                            get_face_book_ProfileInformation(accessToken);
                        }

                        @Override
                        public void onError(DialogError error) {
                            // Function to handle error
                        }

                        @Override
                        public void onFacebookError(FacebookError fberror) {
                            // Function to handle Facebook errors
                        }
                    });
        }
        else {
            logoutFromFacebook();
        }
    }
    public void get_face_book_ProfileInformation(String fb_access_token)
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String key = "EAAX0GWIE39sBANAnVZBt2sFkLaXb2ZAUhVZAJf4l1ahJG7zLvPl1OpfzZCEum601QHvdXcSiS175xyePZBj9qs18WDS1lJXFkgCXDntUtRHZAU04RXF6om7lGHZAsrJZBo10qPt6fbYlxoEMk05zqhjIyCk97fSlKmuDekSCeYcHkRxJktQXK9ZCZB";
        Call<ResponseBody> call = apiService.fb_login(fb_access_token);
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
                        System.out.println("--------------------ggggggg----------------" + Str_response);
                        JSONObject jsono = new JSONObject(Str_response);
                        JSONObject jobject_profile_pic = jsono.getJSONObject("picture");
                        JSONObject jobject_profile_data = jobject_profile_pic.getJSONObject("data");

                        //str_get_profile_image = jobject_profile_data.getString("url");
                        str_get_first_name = jsono.getString("name");
                        // str_get_last_name=jsono.getString("name");
                        if(jsono.has("email"))
                        {
                            str_email = jsono.getString("email");
                        }

                        str_fb_user_id = jsono.getString("id");
                        if(str_get_first_name!=null)
                        {
                            String[] parts = str_get_first_name.split(" ");
                            str_get_first_name = parts[0];
                            str_get_last_name = parts[1];
                            System.out.println("-------First Name------>"+str_get_first_name);
                            System.out.println("-------Last Name------>"+str_get_last_name);
                        }
                        if(jsono.has("email"))
                        {
                            save_data("facebook");
                        }
                        else
                        {
                            Alert("Alert","No Email");
                        }
                        // System.out.println("-------Profile Image----->" + str_get_profile_image);
                        System.out.println("----First name-------->" + str_get_first_name);
                        System.out.println("-----Email------->" + str_get_email);
                        System.out.println("------Facebook user ID------>" + str_fb_user_id);
                        loader.dismiss();
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
                Alert("Server Error", "Server Error");
            }
        });
    }
    public void logoutFromFacebook()
    {
        Util.clearCookies(Slider_page.this);
        SharedPreferences.Editor editor = getSharedPreferences("CASPreferences", MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    //------------------------
    public void save_data(final String login_type)
    {
        loader.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> map = new HashMap<>();

        map.put("firstname", str_get_first_name);
        map.put("lastname",str_get_last_name);
        map.put("email", str_email);
        map.put("gcm_key", GCM_Id);


        if(login_type!=null && !login_type.equals(""))
        {
            if(login_type.equals("facebook"))
            {
                map.put("login_type", "facebook");
                map.put("facebookId", str_fb_user_id);
            }
            else if(login_type.equals("google"))
            {
                map.put("login_type", "google");
                map.put("googleImage", gmail_personPhotoUrl1);
                System.out.println("--------while gmail login-post----->"+gmail_personPhotoUrl1);
            }
        }

        Set keys = map.keySet();

        for (Iterator i = keys.iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            String value = (String) map.get(key);

            System.out.println(""+key+":"+value);
        }

        Call<ResponseBody> call = apiService.user_register(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
                        String str_status=response_obj.getString("status");
                        String str_message=response_obj.getString("msg");
                        if(str_status.equals("1"))
                        {
                            JSONObject common_Obj=object.getJSONObject("commonArr");

                            String str_profile_image = common_Obj.getString("profile_pic");
                            session.set_profile(str_profile_image);

                            String str_msg_coutn=common_Obj.getString("unread_message_count");
                            session.set_msg_count(str_msg_coutn);

                            //Alert_Success("Success!!!",str_message);
                            JSONObject json_Common_Array=object.getJSONObject("commonArr");
                            str_get_user_email=json_Common_Array.getString("email");
                            //  str_get_user_name=json_Common_Array.getString("commonId");
                            str_get_user_id=json_Common_Array.getString("commonId");
                            str_get_user_iamge=json_Common_Array.getString("profile_pic");
                            session.set_user_details(str_get_user_id,str_get_user_email,"",str_get_user_iamge,"",json_Common_Array.has("signature_image")? json_Common_Array.getString("signature_image"):"");
                            Intent intent = new Intent(Slider_page.this, Main_homepage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();

                        }
                        else
                        {
                            Alert("Failure",str_message);
                        }
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
    }



    public void Alert_Success(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(Slider_page.this);
        mDialog.setDialogTitle(str_title);
        mDialog.setDialogMessage(str_message);

        mDialog.setPositiveButton(getResources().getString(R.string.ok), new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDialog.dismiss();
                Intent intent = new Intent(Slider_page.this, Main_homepage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();

            }
        });

        mDialog.show();
    }

    public void Alert(final String str_title,final  String str_message)
    {
        final PkDialog mDialog = new PkDialog(Slider_page.this);
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




    private void send_Facebook()
    {
        // String urlToShare = "https://stackoverflow.com/questions/7545254";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = Slider_page.this.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana"))
            {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
            else
            {
                //  Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
            }
        }
// As fallback, launch sharer.php in a browser
        if (!facebookAppFound)
        {
            String sharerUrl = "https://www.facebook.com/ridesharerental/";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        else
        {
            // Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
        }
        if(facebookAppFound==true)
        {
            startActivity(intent);
        }
        else
        {
            Toast.makeText(Slider_page.this,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
        }


    }



    private void sendShareTwit()
    {
        String msg = "";
        Uri uri = Uri
                .parse("android.resource://com.code2care.example.sharetextandimagetwitter/drawable/mona");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
        boolean facebookAppFound = false;

        List<ResolveInfo> matches = Slider_page.this.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter.android"))
            {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
            else
            {
                //  Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
            }
        }
        if (!facebookAppFound)
        {
            String sharerUrl = "https://twitter.com/RideShareCars";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        else
        {
            // Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
        }

        if (facebookAppFound==true)
        {
            startActivity(intent);
        }
        else {
            Toast.makeText(Slider_page.this,"Install twitter application in your mobile",Toast.LENGTH_SHORT).show();
        }
        // intent.putExtra(Intent.EXTRA_STREAM, uri);
        // intent.setType("image/jpeg");
        // intent.setPackage("com.twitter.android");
    }



    private void send_instagram()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"");
        boolean facebookAppFound = false;

        List<ResolveInfo> matches = Slider_page.this.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches)
        {
            System.out.println("-------Print package------->"+info.activityInfo.packageName);
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.instagram.android"))
            {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
            else
            {
                //  Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
            }
        }
        if (!facebookAppFound)
        {
            String sharerUrl = "https://www.instagram.com/ridesharerental/";
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
        }
        else
        {
            // Toast.makeText(context,"Install facebook application in your mobile",Toast.LENGTH_SHORT).show();
        }

        if (facebookAppFound==true)
        {
            startActivity(intent);
        }
        else {
            Toast.makeText(Slider_page.this,"Install instagram application in your mobile",Toast.LENGTH_SHORT).show();
        }
        // intent.putExtra(Intent.EXTRA_STREAM, uri);
        // intent.setType("image/jpeg");
        // intent.setPackage("com.twitter.android");
    }

}
