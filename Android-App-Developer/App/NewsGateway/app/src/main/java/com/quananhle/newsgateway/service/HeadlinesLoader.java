package com.quananhle.newsgateway.service;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.quananhle.newsgateway.MainActivity;

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

public class HeadlinesLoader extends AsyncTask<Void, Void, ArrayList<Article>> {
//    @SuppressLint("StaticFieldLeak");
    private static final String TAG = "HeadlinesLoader";
    private MainActivity mainActivity;
    private ArrayList<Article> articleArrayList;
    private static final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private static final String DATA_URL = "http://newsapi.org/v2/top-headlines?country=us&apiKey=";

    public HeadlinesLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articleArrayList){

    }

    @Override
    protected ArrayList<Article> doInBackground(Void... voids) {
        String dataURL = DATA_URL + API_KEY;
        Log.d(TAG, "doInBackground: (HeadlinesLoader) URL is " + dataURL);
        String urlToUse = Uri.parse(dataURL).toString();
        Log.d(TAG, "doInBackground: (HeadlinesLoader) " + urlToUse);
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
            Log.d(TAG, "doInBackground: (HeadlinesLoader) " + stringBuilder.toString());
        } catch (MalformedURLException mURLe) {
            Log.e(TAG, "doInBackground: (HeadlinesLoader) MalformedURLException ", mURLe);
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            Log.e(TAG, "doInBackground: (HeadlinesLoader) ProtocolException ", pe);
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            Log.e(TAG, "doInBackground: (HeadlinesLoader) FileNotFoundException ", fnfe);
            fnfe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            Log.e(TAG, "doInBackground: (HeadlinesLoader) IOException ", ioe);
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: (HeadlinesLoader) Exception ", e);
            e.printStackTrace();
        }
        articleArrayList = parseJSON(stringBuilder.toString());
        Log.d(TAG, "doInBackground: (HeadlinesLoader) | articleArrayList: " + articleArrayList);
        return articleArrayList;
    }
    private ArrayList<Article> parseJSON(String str){
        Log.d(TAG, "parseJSON: (HeadlinesLoader) String is " + str);
        articleArrayList = new ArrayList<>();
        Article article = new Article();
        Log.d(TAG, "parseJSON: (HeadlinesLoader) starting parsing JSON");
        try {
            
        }
    }
}
