package com.example.stockwatch;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StockLoaderRunnable {
    private static final String TAG = "StockLoaderRunnable";
    private MainActivity mainActivity;
    private static final String DATA_URL = "https://api.iextrading.com/1.0/ref-data/symbols";

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
                handleResults(null);
                httpURLConnection.disconnect();
                return;
            }
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line).append("\n");
            }
            Log.d(TAG, "RUN: " + stringBuilder.toString());
        }
        catch (Exception e){
            Log.e(TAG, "RUN: ", e);
            handleResults(null);
            return;
        }
        handleResults(stringBuilder.toString());
    }
    private void handleResults(String str){
        if (str == null){
            Log.d(TAG, "handleResults(: Failure in data download");
            mainActivity.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   mainActivity.downloadFailed();
               }
            });
            return;
        }
        final ArrayList<Stock> stockList = parseJSON(s);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stockList != null) {
                    Toast.makeText(mainActivity, "Loaded" + stockList.size() + " stock.", Toast.LENGTH_SHORT).show();
                    mainActivity.updateData(stockList);
                }
            }
        });
    }
    private ArrayList<Stock> parseJSON(String str) {
        ArrayList<Stock> stockArrayList = new ArrayList<>();
        try{
            JSONArray jobjMain = new JSONArray(s);
        }
        catch (Exception e){

        }
    }
        private ArrayList<Stock >
}
