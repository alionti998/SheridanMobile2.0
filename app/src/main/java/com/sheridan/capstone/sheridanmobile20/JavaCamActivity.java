package com.sheridan.capstone.sheridanmobile20;

/**
 * Main screen
 */

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

public class JavaCamActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    WebView webview;
    String resourceURL = "http://liontia.dev.fast.sheridanc.on.ca/Capstone/JAVACAM.html";
    //String resourceURL = "file:///android_asset/news/index.html";// for Android assets html, javascript does not work with local resources
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml for the drawer
        getLayoutInflater().inflate(R.layout.activity_java_cam, contentFrameLayout);//the xml with the content for this activity

        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//the floating button (add per activity)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);//swipe down refresh
        mSwipeRefreshLayout.setOnRefreshListener(this);//listener for swipe down

        webview = (WebView) findViewById(R.id.webViewHome);//web view to display url in activity
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

    }

    @Override
    public void onRefresh() { //if pull down to refresh
        Log.i("test", "refreshing");
        webview.loadUrl(resourceURL);//use html file and resources from my webspace
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "JAVACAM has been updated", Toast.LENGTH_SHORT).show();
    }
}
