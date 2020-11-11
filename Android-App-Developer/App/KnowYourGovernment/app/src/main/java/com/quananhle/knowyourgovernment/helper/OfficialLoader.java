package com.quananhle.knowyourgovernment.helper;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.MainActivity;
import com.quananhle.knowyourgovernment.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class OfficialLoader extends AsyncTask<String, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private static final String TAG = "OfficialLoader";
    private static final String API_KEY = "AIzaSyDBDktFKTYIN3gfxkLWzdhkafxtRVM6W0w";
    private static String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="
            + API_KEY + "&address=";
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    private static final String UNKNOWN_PARTY = "Unknown";

    private String city;
    private String state;
    private String zip;

    public OfficialLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: in pre execute");
    }

    @Override
    protected void onPostExecute(String str) {
//        Log.d(TAG, "onPostExecute: in post execute");
        if (str == null) {
            Toast.makeText(mainActivity, "Civic Info Service API is unavailable", Toast.LENGTH_LONG).show();
            mainActivity.setOfficialList(null);
            return;
        } else if (str.isEmpty()) {
            Toast.makeText(mainActivity, "Location not found", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialList(null);
            return;
        }
        ArrayList<Official> officialArrayList = parseJSON(str);
        Object[] objects = new Object[2];
        objects[0] = city + ", " + state + " " + zip;
        objects[1] = officialArrayList;
        mainActivity.setOfficialList(objects);
    }

    @Override
    protected String doInBackground(String... strings) {
        String dataURL = DATA_URL + strings[0];
//        Log.d(TAG, "doInBackground: URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
//        Log.d(TAG, "doInBackground: " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            Log.d(TAG, "doInBackground: " + stringBuilder.toString());
        } catch (MalformedURLException mURLe) {
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
//            Log.e(TAG, "doBackground: File not found " + fnfe);
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
//            Log.e(TAG, "doBackground: IOExecption " + ioe);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e(TAG, "doInBackground: Exception", e);
            return null;
        }
//        Log.d(TAG, "doInBackground: returning");
        return stringBuilder.toString();
    }

    private ArrayList<Official> parseJSON(String str) {
        Log.d(TAG, "parseJSON: starting parsing JSON");
        SocialMedia socialMedia = new SocialMedia();
        ArrayList<Official> officialArrayList = new ArrayList<>();
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

//                for (int j = 0; j < indices.length; j++) {
//                    JSONObject innerObj = officialsArray.getJSONObject(indices[j]);
//                    String name = innerObj.getString("name");
//
//                    String address = "";
//                    if (!innerObj.has("address")) {
//                        address = DEFAULT_DISPLAY;
//                    } else {
//                        JSONArray addressArray = innerObj.getJSONArray("address");
//                        JSONObject addressObject = addressArray.getJSONObject(0);
//
//                        if (addressObject.has("line1")) {
//                            address += addressObject.getString("line1") + "\n";
//                        }
//                        if (addressObject.has("line2")) {
//                            address += addressObject.getString("line2") + "\n";
//                        }
//                        if (addressObject.has("city")) {
//                            address += addressObject.getString("city") + " ";
//                        }
//                        if (addressObject.has("state")) {
//                            address += addressObject.getString("state") + ", ";
//                        }
//                        if (addressObject.has("zip")) {
//                            address += addressObject.getString("zip");
//                        }
//                    }
//                    String party = (innerObj.has("party") ? innerObj.getString("party") : UNKNOWN_PARTY);
//                    String phones = (innerObj.has("phones") ? innerObj.getJSONArray("phones").getString(0) : DEFAULT_DISPLAY);
//                    String urls = (innerObj.has("urls") ? innerObj.getJSONArray("urls").getString(0) : DEFAULT_DISPLAY);
//                    String emails = (innerObj.has("emails") ? innerObj.getJSONArray("emails").getString(0) : DEFAULT_DISPLAY);
//                    String photoURL = (innerObj.has("photoUrl") ? innerObj.getString("photoUrl") : DEFAULT_DISPLAY);
//
//                    JSONArray channels = (innerObj.has("channels") ? innerObj.getJSONArray("channels") : null);
//                    String facebook = "";
//                    String twitter = "";
//                    String youtube = "";
//
//                    if (channels != null) {
//                        for (int k = 0; k < channels.length(); k++) {
//                            String type = channels.getJSONObject(k).getString("type");
//                            switch (type) {
//                                case "Facebook":
//                                    facebook = channels.getJSONObject(k).getString("id");
//                                    break;
//                                case "Twitter":
//                                    twitter = channels.getJSONObject(k).getString("id");
//                                    break;
//                                case "YouTube":
//                                    youtube = channels.getJSONObject(k).getString("id");
//                                    break;
//                                default:
//                                    break;
//                            }
//                            socialMedia = new SocialMedia(facebook, twitter, youtube);
//                        }
//                    } else { // is null
//                        facebook = DEFAULT_DISPLAY;
//                        twitter = DEFAULT_DISPLAY;
//                        youtube = DEFAULT_DISPLAY;
//                    }
//                    Official o = new Official(name, officeName, party,
//                            address, phones, urls, emails, photoURL,
//                            socialMedia);
//                    officialArrayList.add(o);
//                }


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

                    JSONArray jsonArrayChannels = (!jsonOfficialsObject.has("channels")
                            ? null : jsonOfficialsObject.getJSONArray("channels"));
                    String facebookAccount = "", twitterAccount = "", youtubeAccount = "";
                    if (jsonArrayChannels != null) {
                        for (int k = 0; k < jsonArrayChannels.length(); ++k) {
                            JSONObject jsonChannelObject = jsonArrayChannels.getJSONObject(k);
                            String type = jsonChannelObject.getString("type");
                            String id = jsonChannelObject.getString("id");
                            facebookAccount = type.equals("Facebook") ? id : DEFAULT_DISPLAY;
                            twitterAccount = type.equals("Twitter") ? id : DEFAULT_DISPLAY;
                            youtubeAccount = type.equals("YouTube") ? id : DEFAULT_DISPLAY;
                        }
                        socialMedia = new SocialMedia(facebookAccount, twitterAccount, youtubeAccount);
                    } else {
                        facebookAccount = DEFAULT_DISPLAY;
                        twitterAccount = DEFAULT_DISPLAY;
                        youtubeAccount = DEFAULT_DISPLAY;
                    }
                    Official official = new Official(officeName, officialName, party, address, phones, urls,
                            emails, photoURL, socialMedia);
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
}
