package com.sheridan.capstone.sheridanmobile20.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.sheridan.capstone.sheridanmobile20.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by Anthony Lionti on 2017-12-03.
 */

public class LocationFragment extends DialogFragment {

    JSONArray jsonArray;
    RequestTask rt;
    String campus, room, roomType;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            Toast.makeText(getContext(), "Unable to get host address.", Toast.LENGTH_SHORT).show();
            ipAddressString = null;
        }
        Log.i("LOCAL IP ADDRESS", ipAddressString);
        Toast.makeText(getContext(), "LOCAL IP ADDRESS " + ipAddressString, Toast.LENGTH_SHORT).show();

        rt = new RequestTask();
        try {
            jsonArray = rt.execute("http://mobileapp.sheridancollege.ca/api/v1/CampusMaps/GetLocation?ipAddress=" + ipAddressString).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(jsonArray.toString())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void returnCampusInfo() { //this may need to be changed, when the api starts working again
        JSONObject jsonObj = new JSONObject();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.i("test", "obj: " + jsonObj);

            try {
                // String buildingCode = jsonObj.getString("buildingCode");
                campus = jsonObj.getString("CampusCode");
                room = jsonObj.getString("RoomNumber");
                roomType = jsonObj.getString("RoomTypeCode");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

//get json from url, must be done in separate class
class RequestTask extends AsyncTask<String, Void, JSONArray> {
    JSONArray jsonArray;

    @Override
    protected JSONArray doInBackground(String... urlString) {
        String jsonString;

        HttpURLConnection urlConnection;
        URL url;
        BufferedReader br;
        StringBuilder sb = null;
        try {
            url = new URL(urlString[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            br = new BufferedReader(new InputStreamReader(url.openStream()));
            sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonString = sb.toString();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray message) {

    }

}


