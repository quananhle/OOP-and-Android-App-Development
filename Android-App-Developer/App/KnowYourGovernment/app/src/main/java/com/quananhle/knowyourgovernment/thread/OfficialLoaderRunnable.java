package com.quananhle.knowyourgovernment.thread;

import android.net.Uri;

import com.quananhle.knowyourgovernment.MainActivity;

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
        Uri dataUri = Uri.parse(DATA_URL);
    }
}
