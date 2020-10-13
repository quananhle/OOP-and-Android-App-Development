package com.example.stockwatch;

import android.net.Uri;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class StockLoaderRunnable {
    private static final String TAG = "StockLoaderRunnable";
    private MainActivity mainActivity;
    private static final String DATA_URL = "";

    StockLoaderRunnable (MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void run(){
        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "RUN: " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d(TAG, "RUN: HTTP ResponseCode NOT OK: " + httpURLConnection.getResponseCode());
            }
        }
    }
}
