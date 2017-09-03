package com.sheridan.capstone.sheridanmobile20;

/**
 * Main screen
 */

import android.content.Intent;
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

import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    WebView webview;
    String resourceURL = "https://sheridanmobile20.firebaseapp.com/";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml for the drawer
        getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);//the xml with the content for this activity

        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//the floating button (add per activity)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "infosheridan@sheridaninstitute.ca" });
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Type in your enquiry]");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "[Please add details along with your full name, student ID#, program and any other information so that we can help you better. " +
                        "Replies may take up to 2 business days or longer during the start of term]");
                getApplicationContext().startActivity(Intent.createChooser(emailIntent, "Email Sheridan"));
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
        //webview.loadUrl("file:///android_asset/news/index.html");//use html file and resources from local Android file system (assets)

        //setContentView(mWebview );

    }

    @Override
    public void onRefresh() { //if pull down to refresh
        Log.i("test", "refreshing");
        webview.loadUrl(resourceURL);//use html file and resources from my webspace
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "Home has been updated", Toast.LENGTH_SHORT).show();
    }
}
