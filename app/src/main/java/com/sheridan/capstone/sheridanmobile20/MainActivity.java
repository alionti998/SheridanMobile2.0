package com.sheridan.capstone.sheridanmobile20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

//Anthony Test commit 1
//content_main will be the home screen. it will have a news/alerts section provided by Android WebView/HTML

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
       FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
       getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);

        WebView webview = (WebView) findViewById(R.id.webViewHome);
        webview .setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String   failingUrl) {

            }
        });

        //webview.loadUrl("http://liontia.dev.fast.sheridanc.on.ca/Capstone");//use html file and resources from my webspace
        webview.loadUrl("file:///android_asset/news/index.html");//use html file and resources from local Android file system (assets)

        //setContentView(mWebview );
    }

}
