package com.example.Quan_Le.popular_movies_android_app.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class MoviesUtils {

    private boolean connected;
    private static MoviesUtils instance;

    public static MoviesUtils getInstance() {
        if (null != instance) {
            return instance;
        } else {
            instance = new MoviesUtils();
        }
        return instance;
    }

    public boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
}