package com.ridesharerental.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;

import java.util.HashMap;

import static com.ridesharerental.app.R.id.webview;

/**
 * Created by user65 on 1/23/2018.
 */

public class Paypal extends Activity
{
    String user_id="";
    SessionManager sessionManager;
    WebView myWebView;

    RelativeLayout Rel_back;
    Common_Loader loader;
    String str_booking_no="";
    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_layout);
        loader=new Common_Loader(Paypal.this);
        sessionManager = new SessionManager(Paypal.this);

        HashMap<String, String> details = sessionManager.getUserDetails();
        user_id = details.get(sessionManager.KEY_USER_ID);
        System.out.println("------User ID-------->"+user_id);

        init();

        Rel_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
                Paypal.this.finish();
            }
        });

    }


    public void init()
    {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            str_booking_no=bundle.getString("booking_no");
        }
        Rel_back=(RelativeLayout)findViewById(R.id.chat_detail_backLAY);
        myWebView = (WebView) findViewById(webview);
        loader.show();
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        System.out.println("----Paypal Url--------->"+str_booking_no);;
        myWebView.loadUrl(str_booking_no);

    }


    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            loader.show();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            System.out.println("on finish");
            System.out.println("------finish url--------->"+url);
            loader.dismiss();
            if(url.contains("app/driver/payment_status/success"))
            {
                Intent payment_intent = new Intent(Paypal.this, Payment_Completed.class);
                startActivity(payment_intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                Paypal.this.finish();
            }
            else if(url.contains("app/driver/payment_status/failed"))
            {
                onBackPressed();
                Paypal.this.finish();
            }
        }
    }
}
