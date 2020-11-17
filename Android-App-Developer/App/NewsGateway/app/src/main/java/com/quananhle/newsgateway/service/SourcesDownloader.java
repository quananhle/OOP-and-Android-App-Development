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
    @SuppressLint("StaticFieldLeak")
    private static final String TAG = "SourcesDownloader";
    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL = "https://newsapi.org/v2/sources?language=en&country=us&apiKey=";
    private static final String URL_GET_CATEGORY = "https://newsapi.org/v2/sources?language=en&country=us&category=";
    private static final String URL_CATEGORY_END = "&apiKey=";
    private MainActivity mainActivity;
    private ArrayList<Source> sourceArrayList;
    private Map<String, ArrayList<Source>> hashMap = new TreeMap<>();
    String id, name, category;
    public SourcesDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected void onPreExecute(){
        Log.d(TAG, "onPreExecute: ");
    }
    @Override
    protected void onPostExecute(ArrayList<Source> sourceArrayList) {
        Log.d(TAG, "onPostExecute: (sourceArrayList) " + sourceArrayList);
        ArrayList<Source> arrayList = new ArrayList<>();
        ArrayList<Source> list;
        hashMap.put("all", sourceArrayList);
        for (Source source : sourceArrayList){
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
            super.onPostExecute(sourceArrayList);
            Log.d(TAG, "onPostExecute: (hashMap) " + hashMap);
            mainActivity.setSources(hashMap);
        }
        catch (Exception e){
            Log.d(TAG, "onPostExecute: " + e.getMessage());
            e.printStackTrace();
        }

    }
    @Override
    protected ArrayList<Source> doInBackground(Void... voids) {
        String dataURL = DATA_URL + API_KEY;
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
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line).append('\n');
            Log.d(TAG, "doInBackground: " + stringBuilder.toString());
        } catch (MalformedURLException mURLe) {
            Log.e(TAG, "doInBackground: MalformedURLException ", mURLe);
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            Log.e(TAG, "doInBackground: ProtocolException ", pe);
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "doInBackground: FileNotFoundException ", fnfe);
            fnfe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            Log.e(TAG, "doInBackground: IOException ", ioe);
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: Exception ", e);
            e.printStackTrace();
        }
        sourceArrayList = parseJSON(stringBuilder.toString());
        return sourceArrayList;
    }
    private ArrayList<Source> parseJSON(String str){
        Log.d(TAG, "parseJSON: String is " + str);
        ArrayList<Source>  sourceArrayList = new ArrayList<>();
        Source source = new Source();
        Log.d(TAG, "parseJSON: starting parsing JSON");
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray sources = (JSONArray) jsonObject.get("sources");
            Log.d(TAG, "parseJSON: total sources available: " + sources.length());
            for (int i=0; i < sources.length(); ++i){
                source = new Source();
                JSONObject jsonObj = (JSONObject) sources.getJSONObject(i);
                source.setId(getID(jsonObj));
                source.setCompany(getName(jsonObj));
                source.setCategory(getCategory(jsonObject));
                sourceArrayList.add(source);
            }
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: Exception " + e);
        }
        return sourceArrayList;
    }
    private String getID(JSONObject object){
        try {
            id = object.getString("id");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (getID) " + e);
        }
        return id;
    }
    private String getName(JSONObject object){
        try {
            name = object.getString("name");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (getName) " + e);
        }
        return name;
    }
    private String getCategory(JSONObject object){
        try {
            category = object.getString("category");
        }
        catch (Exception e){
            Log.d(TAG, "parseJSON: (getCategory) " + e);
        }
        return category;
    }
}
