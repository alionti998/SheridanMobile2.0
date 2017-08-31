package com.sheridan.capstone.sheridanmobile20;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

//Anthony Test commit 1
//content_main will be the home screen. it will have a news/alerts section provided by Android WebView/HTML

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    WebView webview;
    String resourceURL = "http://liontia.dev.fast.sheridanc.on.ca/Capstone/";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        webview = (WebView) findViewById(R.id.webViewHome);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("test", "overide= " + url);
                if (!url.equals(resourceURL)) { // Could be cleverer and use a regex
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
                    return true;
                }
                return false;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String   failingUrl) {

            }
        });

        webview.loadUrl(resourceURL);//use html file and resources from my webspace
        //webview.loadUrl("file:///android_asset/news/index.html");//use html file and resources from local Android file system (assets)

        //setContentView(mWebview );

    }

    @Override
    public void onRefresh() {
        Log.i("test", "refreshing");
        webview.loadUrl(resourceURL);//use html file and resources from my webspace
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "News has been updated", Toast.LENGTH_SHORT).show();
    }
}
