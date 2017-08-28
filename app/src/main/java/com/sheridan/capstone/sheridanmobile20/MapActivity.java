package com.sheridan.capstone.sheridanmobile20;

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
    List<String> campusList, buildingList, floorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_map, contentFrameLayout);

        spinnerCampus = (Spinner)findViewById(R.id.spinnerCampus);
        spinnerBuilding = (Spinner)findViewById(R.id.spinnerBuilding);
        spinnerFloor = (Spinner)findViewById(R.id.spinnerFloor);

        campusList =  new ArrayList<String>();
        buildingList =  new ArrayList<String>();

        campusList.add("TRA");
        campusList.add("DAV");
        campusList.add("HMC");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, campusList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampus.setAdapter(adapter);

        //buildingList.add("tmp");
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buildingList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuilding.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, floorList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Log.i("test", "finished main");

        spinnerCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//campus changed
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                rt = new RequestTask();
                try {
                    jsonArray = rt.execute("http://xlm.sheridancollege.ca:8080/xlmagic/locationServices/getBuildingList?campusCode=" + spinnerCampus.getSelectedItem().toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                buildingList = returnMapBasicInfo("buildingCode");
                //adapter1.notifyDataSetChanged();
                Log.i("test", "list= " + buildingList.toString());
                adapter1 = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_spinner_item, buildingList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBuilding.setAdapter(adapter1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinnerBuilding.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//building changed
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                floorList = returnFloorNumber(spinnerBuilding.getSelectedItem().toString());
                //adapter1.notifyDataSetChanged();
               // Log.i("test", "list= " + buildingList.toString());
                adapter2 = new ArrayAdapter<String>(MapActivity.this, android.R.layout.simple_spinner_item, floorList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFloor.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinnerFloor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//floor changed
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new DisplayMapImage((ImageView) findViewById(R.id.imageView))
                        .execute(returnFloorMapUrl(spinnerBuilding.getSelectedItem().toString(), spinnerFloor.getSelectedItem().toString()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    public List returnMapBasicInfo(String data) {
        JSONObject jsonObj = new JSONObject();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                jsonObj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.i("test", "obj: " + jsonObj);

            try {
                // String buildingCode = jsonObj.getString("buildingCode");
                list.add(jsonObj.getString(data));
                //Log.i("test", "code= " + jsonObj.getString(data));
                //Log.i("test", "data: " + list);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List returnFloorNumber (String buildingCode) {
        JSONObject jsonObj = new JSONObject();
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObj = jsonArray.getJSONObject(i);

                JSONObject jsonObj2 = new JSONObject();

                JSONArray jsonArray2 = jsonObj.optJSONArray("floors");
                for (int j = 0; j < jsonArray2.length(); j++) {

                    jsonObj2 = jsonArray2.getJSONObject(j);

                    if (jsonObj2.getString("buildingCode").equals(buildingCode)) {
                        list.add(String.valueOf(j + 1));
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
        JSONObject jsonObj = new JSONObject();
        //List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                jsonObj = jsonArray.getJSONObject(i);

                    JSONObject jsonObj2 = new JSONObject();

                    JSONArray jsonArray2 = jsonObj.optJSONArray("floors");
                    for (int j = 0; j < jsonArray2.length(); j++) {

                        jsonObj2 = jsonArray2.getJSONObject(j);

                        if (jsonObj2.getString("buildingCode").equals(buildingCode) && jsonObj2.getString("floorCode").equals(floorCode)) {
                            Log.i("test", "floor url= " + jsonObj2.getString("floorMapUrl"));
                            return jsonObj2.getString("floorMapUrl");
                        }
                      //  Log.i("test", "obj: " + jsonObj2);

                      //  Log.i("test", "data: " + list);
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class RequestTask extends AsyncTask<String, Void, JSONArray> {
    JSONArray jsonArray;
    @Override
    protected JSONArray doInBackground(String... urlString) {
        String jsonString;

        // Log.i("test", "test");

        HttpURLConnection urlConnection = null;
        URL url = null;
        BufferedReader br = null;
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
