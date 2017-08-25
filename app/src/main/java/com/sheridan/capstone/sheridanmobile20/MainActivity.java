package com.sheridan.capstone.sheridanmobile20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

//Anthony Test commit 1

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //this was causing duplicate fragments being displayed, disable it for now and see if it causes problems
       FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
       getLayoutInflater().inflate(R.layout.content_main, contentFrameLayout);
    }

}
