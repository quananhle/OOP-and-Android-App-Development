package com.quananhle.knowyourgovernment.thread;

import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.MainActivity;
import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.Official;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

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
    private static String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="
            + API_KEY + "&address=";
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    private static final String UNKNOWN_PARTY = "Unknown";

    private String city;
    private String state;
    private String zip;

    public OfficialLoaderRunnable(MainActivity mainActivity, String zipCode){
        this.mainActivity = mainActivity;
        this.zip = zipCode;
    }
    @Override
    public void run(){
        String dataURL = DATA_URL + zip;
        String urlToUse = Uri.parse(dataURL).toString();
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
        final ArrayList<Official> officialArrayList = parseJSON(str);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (officialArrayList != null){
                    Toast.makeText(mainActivity, "Loaded " + officialArrayList.size()
                            + " officials.", Toast.LENGTH_SHORT).show();
                    mainActivity.updateList(officialArrayList);
                }
            }
        });
    }
    private ArrayList<Official> parseJSON(String str) {
        Log.d(TAG, "parseJSON: starting parsing JSON");
        ArrayList<SocialMedia> socialMedia = new ArrayList<>();
        ArrayList<Official> officialArrayList = new ArrayList<>();
        try {
            /**
             * 1) The “normalizedInput” JSONObject contains the following:
             * "normalizedInput": {
             *      "line1": "",
             *      "city": "Chicago",
             *      "state": "IL",
             *      "zip": "60654"
             * },
             */
            JSONObject object = new JSONObject(str);
            JSONObject normalizedInput = object.getJSONObject("normalizedInput");
            city = normalizedInput.getString("city");
            state = normalizedInput.getString("state");
            zip = normalizedInput.getString("zip");

            /**
             * 2) The “offices” JSONArray contains the following:
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
            for (int i = 0; i < officesArray.length(); ++i) {
                JSONObject jsonObject = officesArray.getJSONObject(i);
                String officeName = jsonObject.getString("name");
                String officialIndices = jsonObject.getString("officialIndices");
                String[] array = officialIndices.substring(1, officialIndices.length() - 1).split(",");
                int[] indices = new int[array.length];
                //access the indices of officialIndices and store it in indices
                for (int j = 0; j < array.length; ++j) {
                    indices[j] = Integer.parseInt(array[j]);
                }
                /**
                 * 3) The “officials” JSONArray contains the following:
                 * "officials": [
                 *  {
                 *  "name": "Donald J. Trump",
                 *  "address": [
                 *  {
                 *      "line1": "The White House",
                 *      "line2": "1600 Pennsylvania Avenue NW",
                 *      "city": "Washington",
                 *      "state": "DC",
                 *      "zip": "20500"
                 *  }
                 *],
                 * "party": "Republican",
                 * "phones": [
                 *      "(202) 456-1111"
                 * ],
                 * "urls": [
                 *      "http://www.whitehouse.gov/"
                 * ],
                 * "emails": [
                 *      "email@address.com"
                 * ]
                 * "photoUrl": "https://www.whitehouse.gov/sites/whitehouse.gov/files/images/45/PE%20Color.jpg",
                 * "channels": [
                 *  {
                 *      "type": "GooglePlus",
                 *      "id": "+whitehouse"
                 *  },
                 *  {
                 *      "type": "Facebook",
                 *      "id": "whitehouse"
                 *  },
                 *  {
                 *      "type": "Twitter",
                 *      "id": "whitehouse"
                 *  },
                 *  {
                 *      "type": "YouTube",
                 *      "id": "whitehouse"
                 *  }
                 * ]
                 * },
                 *]
                 */
                JSONArray officialsArray = object.getJSONArray("officials");
                //access the elements of officials
                for (int j = 0; j < indices.length; ++j) {
                    JSONObject jsonOfficialsObject = officialsArray.getJSONObject(indices[j]);
                    String officialName = jsonOfficialsObject.getString("name");
                    String address = "";
                    if (!jsonOfficialsObject.has("address")) {
                        address = DEFAULT_DISPLAY;
                    } else {
                        JSONObject jsonAddressObject = jsonOfficialsObject
                                .getJSONArray("address").getJSONObject(0);
                        if (jsonAddressObject.has("line1")) address += jsonAddressObject
                                .getString("line1") + '\n';
                        if (jsonAddressObject.has("line2")) address += jsonAddressObject
                                .getString("line2") + '\n';
                        if (jsonAddressObject.has("city")) address += jsonAddressObject
                                .getString("city") + ", ";
                        if (jsonAddressObject.has("state")) address += jsonAddressObject
                                .getString("state") + ' ';
                        if (jsonAddressObject.has("zip")) address += jsonAddressObject
                                .getString("zip");
                    }

                    String party = (jsonOfficialsObject.has("party")
                            ? jsonOfficialsObject.getString("party") : UNKNOWN_PARTY);
                    String photoURL = (jsonOfficialsObject.has("photoUrl")
                            ? jsonOfficialsObject.getString("photoUrl") : DEFAULT_DISPLAY);
                    String phones = (jsonOfficialsObject.has("phones")
                            ? jsonOfficialsObject.getJSONArray("phones").getString(0) : DEFAULT_DISPLAY);
                    String emails = (jsonOfficialsObject.has("emails")
                            ? jsonOfficialsObject.getJSONArray("emails").getString(0) : DEFAULT_DISPLAY);
                    String urls = (jsonOfficialsObject.has("urls")
                            ? jsonOfficialsObject.getJSONArray("urls").getString(0) : DEFAULT_DISPLAY);

                    Official official = new Official(officeName, officialName, party, address, phones, urls,
                            emails, photoURL, socialMedia);
                    official.setSocialMedia(getSocialMedia(jsonOfficialsObject));
                    officialArrayList.add(official);
                }
            }
            return officialArrayList;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: Exception " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private ArrayList<SocialMedia> getSocialMedia(JSONObject jsonObject){
        ArrayList<SocialMedia> socialMedia = new ArrayList<>();
        SocialMedia tmp;
        try {
            JSONArray channels = (JSONArray) jsonObject.get("channels");
            for(int i = 0; i < channels.length(); i++) {
                JSONObject channel = (JSONObject) channels.get(i);
                tmp = new SocialMedia(channel.getString("type"), channel.getString("id"));
                socialMedia.add(tmp);
            }
        }
        catch (Exception e) {
            Log.d(TAG, "EXCEPTION: Get Social Media: " + e);
        }
        return socialMedia;
    }
}
