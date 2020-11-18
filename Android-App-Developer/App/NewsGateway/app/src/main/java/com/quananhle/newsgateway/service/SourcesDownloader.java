package com.quananhle.newsgateway.service;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.quananhle.newsgateway.MainActivity;

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

public class SourcesDownloader extends AsyncTask<Void, Void, ArrayList<Source>> {
    private static final String TAG = "SourcesDownloader";
    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL = "https://newsapi.org/v2/sources?language=en&country=us&apiKey=";
    private static final String URL_GET_CATEGORY = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private static final String URL_CATEGORY_END = "&apiKey=";
    private MainActivity mainActivity;
    private ArrayList<Source> sourceArrayList = new ArrayList<>();
    private Map<String, ArrayList<Source>> hashMap = new TreeMap<>();
    String id = "", name = "", category = "";
    public SourcesDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected void onPreExecute(){
        Log.d(TAG, "onPreExecute: (SourcesDownloader) ");
    }
    @Override
    protected void onPostExecute(ArrayList<Source> sources) {
        Log.d(TAG, "onPostExecute: (SourcesDownloader) | Total sources: " + sources.size());
        ArrayList<Source> arrayList = new ArrayList<>();
        ArrayList<Source> list;
        hashMap.put("all", sources);
        for (Source source : sources){
            arrayList.clear();
            Source s = new Source();
            s.setId(source.getId());
            s.setCompany(source.getCompany());
            s.setCategory(source.getCategory());
            if (!hashMap.containsKey(source.getCategory().toLowerCase().trim())){
                hashMap.put(source.getCategory(), new ArrayList<Source>());
                list = hashMap.get(source.getCategory());
                list.add(s);
                hashMap.put(source.getCategory(), list);
            }
            else {
                list = hashMap.get(source.getCategory());
                list.add(s);
                hashMap.put(source.getCategory(), list);
            }
        }
        try {
            super.onPostExecute(sources);
            Log.d(TAG, "onPostExecute: (SourcesDownloader) | (hashMap) " + hashMap);
            mainActivity.setSources(hashMap);
        }
        catch (Exception e){
            Log.d(TAG, "onPostExecute: (SourcesDownloader) " + e.getMessage());
            e.printStackTrace();
        }

    }
    @Override
    protected ArrayList<Source> doInBackground(Void... voids) {
        String dataURL = DATA_URL + API_KEY;
        Log.d(TAG, "doInBackground: (SourcesDownloader) URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
        Log.d(TAG, "doInBackground: (SourcesDownloader) " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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
            return null;
        } catch (IOException ioe) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) IOException ", ioe);
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: (SourcesDownloader) Exception ", e);
            e.printStackTrace();
        }
        sourceArrayList = parseJSON(stringBuilder.toString());
        Log.d(TAG, "doInBackground: (sourceArrayList) " + sourceArrayList);
        return sourceArrayList;
    }
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
                source.setCategory(getCategory(jsonObject));
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
