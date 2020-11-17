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
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private ArrayList<Source> sourceArrayList;
    private Map<String, ArrayList<Source>> hashMap = new TreeMap<>();
    public SourcesDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected ArrayList<Source> doInBackground(Void... voids) {
        String dataURL = DATA_URL + API_KEY;
        String urlToUse = Uri.parse(dataURL).toString();
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
        Log.d(TAG, "parseJSON: starting parsing JSON");
        ArrayList<Source>  sourceArrayList = new ArrayList<>();
        Source source = new Source();
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONArray jsonArray = sourceArrayList.get("sources");
            Log.d(TAG, "parseJSON: total sources available: " + jsonArray.length());
            for (int i=0; i < jsonArray.length(); ++i){
                source = new Source();
                JSONObject sourceObject = (JSONObject) jsonArray.get(i);
                source.setId(ge);
            }
        }
    }
}
