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
import java.util.Map;
import java.util.TreeMap;

public class SourcesDownloader extends AsyncTask<Void, Void, ArrayList<Source>> {
    private static final String TAG = "SourcesDownloader";
    private final String API_KEY = "d86d5dc5ffaa4f0fa9036ad5c35fb4a1";
    private String DATA_URL = "https://newsapi.org/v2/sources?language=en&country=us&category=&apiKey=";
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private Map<String, ArrayList<Source>> hashMap = new TreeMap<>();
    public SourcesDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected ArrayList<Source> doInBackground(Void... voids) {
        ArrayList<Source> sourceArrayList;
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
            mURLe.printStackTrace();
        } catch (ProtocolException pe) {
            pe.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sourceArrayList;

    }
}
