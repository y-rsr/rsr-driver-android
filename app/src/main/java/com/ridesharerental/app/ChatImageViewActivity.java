package com.ridesharerental.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class ChatImageViewActivity extends AppCompatActivity {

    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_image_view);

        webview = (WebView) findViewById(R.id.webview);

        getURL();

    }

    private void getURL() {

        if(getIntent()!= null)
        {
            webview.getSettings().setJavaScriptEnabled(true);
            //String pdf = "url_to_your_pdf" ;
            webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + getIntent().getStringExtra("url"));
        }

    }
}
