package com.sheridan.capstone.sheridanmobile20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Anthony Lionti on 2017-08-25.
 * This class controls the actions for the nav drawer
 * Extend your activity to this class if you want your activity to include the nav drawer
 */

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String userinfo;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    public ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this is the fab code for the actions
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        //toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //authentication

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        TextView navUsername;
        TextView navName;

        navUsername = (TextView) findViewById(R.id.nav_email);
        navName = (TextView) findViewById(R.id.nav_name);

     /*   if (currentUser != null) {
            Log.i("test", "not null");

            navUsername.setText(currentUser.getDisplayName());
            navName.setText(currentUser.getDisplayName());
        } else {


            /*navUsername.setText(currentUser.getEmail());
            navName.setText(currentUser.getDisplayName());*/
      //  }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent anIntent = new Intent(getApplicationContext(), SettingsActivity.class);//change this to the class i want to load, map activity
            startActivity(anIntent);
            return true;
        } else if (id == R.id.action_login) {
            Intent anIntent = new Intent(getApplicationContext(), ChooserActivity.class);//change this to the class i want to load, map activity
            startActivity(anIntent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String url;
        CustomTabsIntent.Builder builder;
        Intent anIntent;
        CustomTabsIntent customTabsIntent;

        switch (id) {
            case R.id.nav_home:
                anIntent = new Intent(getApplicationContext(), MainActivity.class);//change this to the class i want to load, map activity
                startActivity(anIntent);
                break;
            case R.id.nav_slate:
                url = "https://slate.sheridancollege.ca";
                builder = new CustomTabsIntent.Builder();
                customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                break;
            case R.id.nav_email:
                url = "https://outlook.office365.com/owa/sheridancollege.ca/";
                builder = new CustomTabsIntent.Builder();
                customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                break;
            case R.id.nav_maps:
                anIntent = new Intent(getApplicationContext(), MapActivity.class);//change this to the class i want to load, map activity
                startActivity(anIntent);
                // drawer.closeDrawers();
                break;
            case R.id.nav_javacam:
                anIntent = new Intent(getApplicationContext(), JavaCamActivity.class);//change this to the class i want to load, map activity
                startActivity(anIntent);
                break;
            case R.id.nav_programs:
                url = "https://academics.sheridancollege.ca/programs/?s=";
                builder = new CustomTabsIntent.Builder();
                customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                break;
            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Share Sheridan Mobile 2.0 with your fellow classmates and faculty. Visit our official GitHub at https://github.com/alionti998/SheridanMobile2.0");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Sheridan Mobile 2.0 with your fellow classmates and faculty. Visit our official GitHub at https://github.com/alionti998/SheridanMobile2.0"));
                break;
            case R.id.nav_as:
                url = "https://access.sheridaninstitute.ca";
                builder = new CustomTabsIntent.Builder();
                customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                break;
            case R.id.nav_send:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
