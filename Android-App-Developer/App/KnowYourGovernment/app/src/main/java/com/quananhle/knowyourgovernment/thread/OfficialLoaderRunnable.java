package com.quananhle.knowyourgovernment.thread;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.MainActivity;
import com.quananhle.knowyourgovernment.helper.Officials;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
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
        Officials official = new Officials();
        ArrayList<Officials> officialsArrayList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(str);
            /**
             * 1) The “normalizedInput” JSONObject contains the following:
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
            String location = city + ", " + state + " " + zip;
            mainActivity.setLocationView(location);
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
            for (int i=0; i < officesArray.length(); ++i){
                JSONObject jsonObject = officesArray.getJSONObject(i);
                String officeName = jsonObject.getString("name");
                String officialIndices = jsonObject.getString("officialIndices");
                String [] array = officialIndices.substring(1, officialIndices.length() - 1).split(",");
                int [] indices = new int[array.length];
                //access the indices of officialIndices and store it in indices
                for (int j=0; j < array.length; ++j){
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
                for (int j=0; j < indices.length; ++j){
                    JSONObject jsonOfficialsObject = officialsArray.getJSONObject(indices[j]);
                    String officialName = jsonOfficialsObject.getString("name");
                    String address = "";
                    SocialMedia socialMedia = new SocialMedia();
                    if (!jsonOfficialsObject.has("address")){
                        address = DEFAULT_DISPLAY;
                    }
                    JSONObject jsonAddressObject = jsonOfficialsObject.getJSONArray("address").getJSONObject(0);
                    if (jsonAddressObject.has("line1")) address += jsonAddressObject.getString("line1") +'\n';
                    if (jsonAddressObject.has("line2")) address += jsonAddressObject.getString("line2") +'\n';
                    if (jsonAddressObject.has("city"))  address += jsonAddressObject.getString("city")  +", ";
                    if (jsonAddressObject.has("state")) address += jsonAddressObject.getString("state") +' ';
                    if (jsonAddressObject.has("zip"))   address += jsonAddressObject.getString("zip");

                    String party = (!jsonOfficialsObject.has("party")
                            ? UNKNOWN_PARTY : jsonOfficialsObject.getString("party"));
                    String phones = (!jsonOfficialsObject.has("phones")
                            ? DEFAULT_DISPLAY : jsonOfficialsObject.getString("phones"));
                    String urls = (!jsonOfficialsObject.has("phones")
                            ? DEFAULT_DISPLAY : jsonOfficialsObject.getString("urls"));
                    String emails = (!jsonOfficialsObject.has("emails")
                            ? DEFAULT_DISPLAY : jsonOfficialsObject.getString("emails"));
                    String photoURL = (!jsonOfficialsObject.has("photoURL")
                            ? DEFAULT_DISPLAY : jsonOfficialsObject.getString("photoURL"));

                    JSONArray jsonArrayChannels = (!jsonOfficialsObject.has("channels")
                            ? null : jsonOfficialsObject.getJSONArray("channels"));
                    String googleAccount = "", facebookAccount = "", twitterAccount = "", youtubeAccount = "";
                    if (jsonArrayChannels != null){
                        for (int k=0; k < jsonArrayChannels.length(); ++k) {
                            JSONObject jsonChannelObject = jsonArrayChannels.getJSONObject(k);
                            String type = jsonChannelObject.getString("type");
                            String id = jsonChannelObject.getString("id");
                            facebookAccount = type.equals("Facebook") ? id : "";
                            twitterAccount = type.equals("Twitter")   ? id : "";
                            youtubeAccount = type.equals("Youtube")   ? id : "";
                        }
                        socialMedia = new SocialMedia(facebookAccount, twitterAccount, youtubeAccount);
                        official.setSocialMedia(socialMedia);
                    }
                    else {
                        facebookAccount = DEFAULT_DISPLAY;
                        twitterAccount  = DEFAULT_DISPLAY;
                        youtubeAccount  = DEFAULT_DISPLAY;
                    }
                    official = new Officials(officeName, officialName, party, address, phones, urls, emails, photoURL, socialMedia);
                    officialsArrayList.add(official);
                }
            }
            return officialsArrayList;
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: Exception " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
