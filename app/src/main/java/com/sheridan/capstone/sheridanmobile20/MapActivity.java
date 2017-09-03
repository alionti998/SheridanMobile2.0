package com.sheridan.capstone.sheridanmobile20;

/**
 * This class is for campus maps
 * gets information from json from http://xlm.sheridancollege.ca which is where map data is stored
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapActivity extends BaseActivity {
    JSONArray jsonArray;
    ArrayAdapter<String> adapter, adapter1, adapter2;
    RequestTask rt;
    Spinner spinnerCampus, spinnerBuilding, spinnerFloor;
    List<String> campusNameList, campusCodeList, buildingCodeList, buildingNameList, floorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_map, contentFrameLayout);

        spinnerCampus = (Spinner)findViewById(R.id.spinnerCampus);
        spinnerBuilding = (Spinner)findViewById(R.id.spinnerBuilding);
        spinnerFloor = (Spinner)findViewById(R.id.spinnerFloor);

        campusCodeList =  new ArrayList<>();
        campusNameList =  new ArrayList<>();
        buildingCodeList =  new ArrayList<>();
        buildingNameList =  new ArrayList<>();

        rt = new RequestTask();
        try {
            jsonArray = rt.execute("http://xlm.sheridancollege.ca:8080/xlmagic/locationServices/getCampusList").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        returnCampusInfo();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campusNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampus.setAdapter(adapter);

        Log.i("test", "finished main");

        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//campus changed
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                rt = new RequestTask();
                try {
                    jsonArray = rt.execute("http://xlm.sheridancollege.ca:8080/xlmagic/locationServices/getBuildingList?campusCode=" + campusCodeList.get(i)).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                returnMapBasicInfo();
                //adapter1.notifyDataSetChanged();
                //Log.i("test", "list= " + buildingList.toString());

                adapter1 = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_spinner_item, buildingNameList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBuilding.setAdapter(adapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                floorList = returnFloorNumber(buildingCodeList.get(i));

                adapter2 = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_spinner_item, floorList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFloor.setAdapter(adapter2);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//floor changed
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new DisplayURLImage((ImageView) findViewById(R.id.imageView))
                        .execute(returnFloorMapUrl(buildingCodeList.get(spinnerBuilding.getSelectedItemPosition()), spinnerFloor.getSelectedItem().toString()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void returnCampusInfo() {
        JSONObject jsonObj = new JSONObject();
        campusNameList = new ArrayList<>();
        campusCodeList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.i("test", "obj: " + jsonObj);

            try {
                // String buildingCode = jsonObj.getString("buildingCode");
                campusNameList.add(jsonObj.getString("campusName"));
                campusCodeList.add(jsonObj.getString("campusCode"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void returnMapBasicInfo() {
        JSONObject jsonObj = new JSONObject();
        buildingCodeList = new ArrayList<>();
        buildingNameList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.i("test", "obj: " + jsonObj);

            try {
                // String buildingCode = jsonObj.getString("buildingCode");
                buildingCodeList.add(jsonObj.getString("buildingCode"));
                buildingNameList.add(jsonObj.getString("buildingName"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public List returnFloorNumber (String buildingCode) {
        JSONObject jsonObj, jsonObj2;
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObj = jsonArray.getJSONObject(i);

                JSONArray jsonArray2 = jsonObj.optJSONArray("floors");
                for (int j = 0; j < jsonArray2.length(); j++) {

                    jsonObj2 = jsonArray2.getJSONObject(j);

                    if (jsonObj2.getString("buildingCode").equals(buildingCode)) {
                        list.add(jsonObj2.getString("floorCode"));
                    }
                  //  Log.i("test", "obj: " + jsonObj2);
                    //  Log.i("test", "data: " + list);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String returnFloorMapUrl (String buildingCode, String floorCode) {
        JSONObject jsonObj, jsonObj2;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObj = jsonArray.getJSONObject(i);

                    JSONArray jsonArray2 = jsonObj.optJSONArray("floors");
                    for (int j = 0; j < jsonArray2.length(); j++) {

                        jsonObj2 = jsonArray2.getJSONObject(j);

                        if (jsonObj2.getString("buildingCode").equals(buildingCode) && jsonObj2.getString("floorCode").equals(floorCode)) {
                            Log.i("test", "floor url= " + jsonObj2.getString("floorMapUrl"));
                            return jsonObj2.getString("floorMapUrl");
                        }
                      //  Log.i("test", "obj: " + jsonObj2);
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
