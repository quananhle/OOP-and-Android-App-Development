package com.quananhle.newsgateway.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class NewsService extends Service {
    private static final String TAG = "NewsService";
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private ServiceReceiver serviceReceiver;

    public void setNews(ArrayList<Article> articles){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
