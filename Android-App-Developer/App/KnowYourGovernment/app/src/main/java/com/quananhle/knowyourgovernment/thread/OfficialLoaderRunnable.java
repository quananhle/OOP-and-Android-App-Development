package com.quananhle.knowyourgovernment.thread;

import android.net.Uri;
import android.util.Log;

import com.quananhle.knowyourgovernment.MainActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OfficialLoaderRunnable implements Runnable {
    private static final String TAG = "OfficialLoaderRunnable";
    private static final String REQUEST_METHOD = "GET";
    private MainActivity mainActivity;

    private static final String API_KEY = "AIzaSyDBDktFKTYIN3gfxkLWzdhkafxtRVM6W0w";
    private static final String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="
            + API_KEY + "&address=";
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";

    public OfficialLoaderRunnable(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void run(){
        String urlToUse = Uri.parse(DATA_URL).toString();
        Log.d(TAG, "run: " + urlToUse);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + httpURLConnection.getResponseCode());
                handleResults(null);
                return;
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line).append('\n');
            }
            Log.d(TAG, "run: " + stringBuilder.toString());
        }
        catch (Exception e){
            Log.e(TAG, "run: " + e);
            handleResults(null);
            return;
        }
        handleResults(stringBuilder.toString());
    }
    //====================== *** HELPER•METHODS *** ======================//
    public void handleResults(String str){
        if (str == null){
            Log.d(TAG, "handleResults: Failure in data downloading");
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.downloadFailed();
                }
            });
            return;
        }
    }
}
