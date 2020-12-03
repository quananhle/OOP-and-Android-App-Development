package com.quananhle.newsgateway.loader;

import android.net.Uri;
import android.util.Log;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.service.Source;

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
import java.util.Map;
import java.util.TreeMap;

public class SourcesLoaderRunnable implements Runnable{
    private static final String TAG = "SourcesLoaderRunnable";
    private static final String REQUEST_METHOD = "GET";
    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL = "https://newsapi.org/v2/sources?language=en&country=us&category=&apiKey=";
    private MainActivity mainActivity;
    private Map<String, ArrayList<Source>> hashMap = new TreeMap<>();

    String id = "", name = "", category = "";

    public SourcesLoaderRunnable(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

//    private void processResults(String str) {
//        final ArrayList<Source> sources = parseJSON(str);
//        if (sources != null)  {
//            mainActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mainActivity.updateSource(sources);
//                }
//            });
//        }
//    }

    @Override
    public void run(){
        ArrayList<Source> sources;
        String dataURL = DATA_URL + API_KEY;
        Log.d(TAG, "doInBackground: (SourcesDownloader) URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
        Log.d(TAG, "doInBackground: (SourcesDownloader) " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.addRequestProperty("User-Agent","");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line).append('\n');
            Log.d(TAG, "doInBackground: (SourcesDownloader) " + stringBuilder.toString());
        } catch (MalformedURLException mURLe) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) MalformedURLException ", mURLe);
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) ProtocolException ", pe);
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) FileNotFoundException ", fnfe);
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) IOException ", ioe);
            ioe.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) Exception ", e);
            e.printStackTrace();
        }
//        processResults(stringBuilder.toString());
    }

    //====================== *** HELPER•METHODS *** ======================//
    private ArrayList<Source> parseJSON(String str){
        Log.d(TAG, "parseJSON: (SourcesDownloader) String is " + str);
        ArrayList<Source>  sourceList = new ArrayList<>();
        Source source = new Source();
        Log.d(TAG, "parseJSON: (SourcesDownloader) starting parsing JSON");
        /*
        {
        "status": "ok",
        "sources": [
        {
            "id": "al-jazeera-english",
            "name": "Al Jazeera English",
            "description": "News, analysis from the Middle East and worldwide, multimedia and interactives, opinions, documentaries, podcasts, long reads and broadcast schedule.",
            "url": "http://www.aljazeera.com",
            "category": "general",
            "language": "en",
            "country": "us",
            "urlsToLogos": {
                "small": "",
                "medium": "",
                "large": ""
            },
            "sortBysAvailable": [
                "top"
            ]
        },
        {
            "id": "ars-technica",
            "name": "Ars Technica",
            "description": "The PC enthusiast's resource. Power users and the tools they love, without computing religion.",
            "url": "http://arstechnica.com",
            "category": "technology",
            "language": "en",
            "country": "us",
            "urlsToLogos": {
                "small": "",
                "medium": "",
                "large": ""
            },
            "sortBysAvailable": [
                "top"
            ]
        },
        {
            "id": "associated-press",
            "name": "Associated Press",
            "description": "The AP delivers in-depth coverage on the international, politics, lifestyle, business, and entertainment news.",
            "url": "https://apnews.com/",
            "category": "general",
            "language": "en",
            "country": "us",
            "urlsToLogos": {
                "small": "",
                "medium": "",
                "large": ""
            },
            "sortBysAvailable": [
                "top"
            ]
        },
         */
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray sources = (JSONArray) jsonObject.get("sources");
            Log.d(TAG, "parseJSON: (SourcesDownloader) total sources available: " + sources.length());
            for (int i=0; i < sources.length(); ++i){
                source = new Source();
                JSONObject jsonObj = (JSONObject) sources.getJSONObject(i);
                source.setId(getID(jsonObj));
                source.setCompany(getName(jsonObj));
                source.setCategory(getCategory(jsonObj));
                sourceList.add(source);
            }
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (SourcesDownloader) | Exception " + e);
        }
        return sourceList;
    }
    //====================== *** HELPER•METHODS *** ======================//
    private String getID(JSONObject object){
        try {
            id = object.getString("id");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (SourcesDownloader) | (getID) " + e);
        }
        return id;
    }
    private String getName(JSONObject object){
        try {
            name = object.getString("name");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (SourcesDownloader) | (getName) " + e);
        }
        return name;
    }
    private String getCategory(JSONObject object){
        try {
            category = object.getString("category");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (SourcesDownloader) | (getCategory) " + e);
        }
        return category;
    }
}



