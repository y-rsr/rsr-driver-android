package com.ridesharerental.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.ridesharerental.retrofit.IConstant_WebService;
import com.ridesharerental.services.Iconstant.SessionManager;
import com.ridesharerental.widgets.Common_Loader;

import java.util.HashMap;

import static com.ridesharerental.app.R.id.webview;

/**
 * Created by user65 on 1/23/2018.
 */

public class Invoive extends Activity
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
        setContentView(R.layout.invoice_page);
        loader=new Common_Loader(Invoive.this);
        sessionManager = new SessionManager(Invoive.this);

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
                Invoive.this.finish();
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
        //myWebView.loadUrl(IConstant_WebService.baseurl+"app/driver/invoice/RSC1501768?commonId="+user_id);
        System.out.println("------Invoice Url-------->"+IConstant_WebService.baseurl+"app/driver/invoice/"+str_booking_no+"?commonId="+user_id);

        loader.show();
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(IConstant_WebService.baseurl+"app/driver/invoice/"+str_booking_no+"?commonId="+user_id);
       // myWebView.loadUrl(IConstant_WebService.baseurl+"app/driver/invoice/RSC1501768?commonId="+user_id);



    }


    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);

           /* if (!pd.isShowing())
            {
                pd.show();
            }*/
            loader.show();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            System.out.println("on finish");
            /*if (pd.isShowing())
            {
                pd.dismiss();
            }*/
            loader.dismiss();
        }
    }
}
