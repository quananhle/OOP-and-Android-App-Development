package com.quananhle.knowyourgovernment.thread;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.MainActivity;
import com.quananhle.knowyourgovernment.helper.Officials;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OfficialLoaderRunnable implements Runnable {
    private static final String TAG = "OfficialLoaderRunnable";
    private static final String REQUEST_METHOD = "GET";
    private MainActivity mainActivity;

    private static final String API_KEY = "AIzaSyDBDktFKTYIN3gfxkLWzdhkafxtRVM6W0w";
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="
            + API_KEY + "&address=";
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";

    private String city;
    private String state;
    private String zip;

    public OfficialLoaderRunnable(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void run(){
        String urlToUse = Uri.parse(DATA_URL).toString();
        Log.d(TAG, "run: " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + httpURLConnection.getResponseCode());
                handleResults(null);
                return;
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line).append('\n');
            }
            Log.d(TAG, "run: " + stringBuilder.toString());
        }
        catch (Exception e){
            Log.e(TAG, "run: " + e);
            handleResults(null);
            return;
        }
        handleResults(stringBuilder.toString());
    }
    //====================== *** HELPER•METHODS *** ======================//
    public void handleResults(String str){
        if (str == null){
            Log.d(TAG, "handleResults: Failure in data downloading");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.downloadFailed();
                }
            });
            return;
        }
        final ArrayList<Officials> officialsArrayList = parseJSON(str);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (officialsArrayList != null){
                    Toast.makeText(mainActivity, "Loaded " + officialsArrayList.size()
                            + " officials.", Toast.LENGTH_SHORT).show();
                    mainActivity.updateList(officialsArrayList);
                }
            }
        });
    }
    private ArrayList<Officials> parseJSON(String str){
        Log.d(TAG, "parseJSON: starting parsing JSON");
        ArrayList<Officials> officialsArrayList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(str);
            /**
             * 1)The “normalizedInput” JSONObject contains the following:
             * "normalizedInput": {
             *      "line1": "",
             *      "city": "Chicago",
             *      "state": "IL",
             *      "zip": "60654"
             * },
             */
            JSONObject normalizedInput = object.getJSONObject("normalizedInput");
            city = normalizedInput.getString("city");
            state = normalizedInput.getString("state");
            zip = normalizedInput.getString("zip");
            /**
             * 2)The “offices” JSONArray contains the following:
             * "offices": [
             *  {
             *      "name": "President of the United States",
             *      "divisionId": "ocd-division/country:us",
             *      "levels": [
             *          "country"
             *      ],
             *      "roles": [
             *          "headOfState","headOfGovernment"
             *      ],
             *      "officialIndices": [
             *           0
             *      ]
             *  },
             *  {
             *      "name": "United States Senate",
             *      "divisionId": "ocd-division/country:us/state:il",
             *      "levels": [
             *          "country"
             *      ],
             *      "roles": [
             *          "legislatorUpperBody"
             *      ],
             *      "officialIndices": [
             *          2,
             *          3
             *      ]
             *  },
             * ],
             */
            JSONArray officesArray = object.getJSONArray("offices");
            for (int i=0; i<officesArray.length(); ++i){
                JSONObject jsonObject = officesArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String officialIndices = jsonObject.getString("officialIndices");
                String [] array = officialIndices.substring(1, officialIndices.length() - 1).split(",");
                int [] indices = new int[array.length];
                for (int j=0; j < array.length; ++j){
                    indices[j] = Integer.parseInt(array[j]);
                }
                for (int j=0; j < indices.length; ++j){
                    
                }
            }

            JSONArray officialsArray = object.getJSONArray("officials");
        }
    }
}
