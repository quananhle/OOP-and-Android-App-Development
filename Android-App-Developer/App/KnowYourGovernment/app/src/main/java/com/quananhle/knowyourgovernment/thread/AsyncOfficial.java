package com.quananhle.knowyourgovernment.thread;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.MainActivity;
import com.quananhle.knowyourgovernment.helper.Officials;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

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

public class AsyncOfficial extends AsyncTask<String, Void, String> {
    private MainActivity mainActivity;
    private static final String TAG = "AsyncOfficialLoader";
    private static final String API_KEY = "AIzaSyDBDktFKTYIN3gfxkLWzdhkafxtRVM6W0w";
    private static String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="
            + API_KEY + "&address=";
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    private static final String UNKNOWN_PARTY = "Unknown";

    private String city;
    private String state;
    private String zip;

    public AsyncOfficial(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute(){
        Log.d(TAG, "onPreExecute: in pre execute");
    }
    @Override
    protected void onPostExecute(String str){
        Log.d(TAG, "onPostExecute: in post execute");
        if (str == null){
            Toast.makeText(mainActivity, "Civic Info Service API is unavailable", Toast.LENGTH_LONG).show();
            mainActivity.setOfficialsList(null);
            return;
        }
        else if (str.isEmpty()){
            Toast.makeText(mainActivity, "Location not found", Toast.LENGTH_SHORT).show();
            mainActivity.setOfficialsList(null);
            return;
        }
        ArrayList<Officials> officialsArrayList = parseJSON(str);
        Object[] objects = new Object[2];
        objects[0] = city + ", " + state + " " + zip;
        objects[1] = officialsArrayList;
        mainActivity.setOfficialsList(objects);
        return;
    }
    @Override
    protected String doInBackground(String... strings) {
        String dataURL = DATA_URL + strings[0];
        Log.d(TAG, "doInBackground: URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
        Log.d(TAG, "doInBackground: " + urlToUse);
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
        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
            Log.e(TAG, "doBackground: File not found " + fnfe);
            return null;
        } catch (IOException ioe){
            ioe.printStackTrace();
            Log.e(TAG, "doBackground: IOExecption " + ioe);
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "doInBackground: Exception", e);
            return null;
        }
        Log.d(TAG, "doInBackground: returning");
        return stringBuilder.toString();
    }

    private ArrayList<Officials> parseJSON(String s){
        Log.d(TAG, "parseJSON: started JSON");
        SocialMedia socialMedia = new SocialMedia();
        ArrayList<Officials> officialList = new ArrayList<>();
        try{
            JSONObject wholeThing = new JSONObject(s);
            JSONObject normalizedInput = wholeThing.getJSONObject("normalizedInput");
            JSONArray offices = wholeThing.getJSONArray("offices");
            JSONArray officials = wholeThing.getJSONArray("officials");

            city = normalizedInput.getString("city");
            state = normalizedInput.getString("state");
            zip = normalizedInput.getString("zip");

            for(int i = 0;i < offices.length(); i++){
                JSONObject obj = offices.getJSONObject(i);
                String officeName = obj.getString("name");
                String officialIndices = obj.getString("officialIndices");

                String temp = officialIndices.substring(1,officialIndices.length()-1);
                String [] temp2 = temp.split(",");
                int [] indices = new int [temp2.length];
                for(int j = 0; j < temp2.length; j++){
                    indices[j] = Integer.parseInt(temp2[j]);
                }

                for(int j = 0; j < indices.length; j++ ){
                    JSONObject innerObj = officials.getJSONObject(indices[j]);
                    String name = innerObj.getString("name");

                    String address = "";
                    if(! innerObj.has("address")){
                        address = DEFAULT_DISPLAY;
                    }
                    else {
                        JSONArray addressArray = innerObj.getJSONArray("address");
                        JSONObject addressObject = addressArray.getJSONObject(0);

                        if (addressObject.has("line1")) {
                            address += addressObject.getString("line1") + "\n";
                        }
                        if (addressObject.has("line2")) {
                            address += addressObject.getString("line2") + "\n";
                        }
                        if (addressObject.has("city")) {
                            address += addressObject.getString("city") + " ";
                        }
                        if (addressObject.has("state")) {
                            address += addressObject.getString("state") + ", ";
                        }
                        if (addressObject.has("zip")) {
                            address += addressObject.getString("zip");
                        }
                    }
                    String party = (innerObj.has("party") ? innerObj.getString("party") : UNKNOWN_PARTY );
                    String phones = ( innerObj.has("phones") ? innerObj.getJSONArray("phones").getString(0) : DEFAULT_DISPLAY );
                    String urls = ( innerObj.has("urls") ? innerObj.getJSONArray("urls").getString(0) : DEFAULT_DISPLAY );
                    String emails = (innerObj.has("emails") ? innerObj.getJSONArray("emails").getString(0) : DEFAULT_DISPLAY );
                    String photoURL = (innerObj.has("photoUrl") ? innerObj.getString("photoUrl") : DEFAULT_DISPLAY);

                    JSONArray channels = ( innerObj.has("channels") ? innerObj.getJSONArray("channels") : null );
                    String facebook = ""; String twitter = ""; String youtube = "";

                    if(channels != null){
                        for(int k = 0; k < channels.length(); k++ ){
                            String type = channels.getJSONObject(k).getString("type");
                            switch (type){
                                case "Facebook":
                                    facebook = channels.getJSONObject(k).getString("id");
                                    break;
                                case "Twitter":
                                    twitter = channels.getJSONObject(k).getString("id");
                                    break;
                                case "YouTube":
                                    youtube = channels.getJSONObject(k).getString("id");
                                    break;
                                default:
                                    break;
                            }
                            socialMedia = new SocialMedia(facebook, twitter, youtube);
                        }
                    }
                    else{ // is null
                        facebook = DEFAULT_DISPLAY;
                        twitter = DEFAULT_DISPLAY; youtube = DEFAULT_DISPLAY;
                    }
                    Officials o = new Officials(name, officeName, party,
                            address, phones, urls, emails, photoURL,
                            socialMedia);
                    officialList.add(o);
                }
            }
            return officialList;
        }catch(Exception e){
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
