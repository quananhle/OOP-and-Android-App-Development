package com.quananhle.newsgateway.service;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.quananhle.newsgateway.MainActivity;

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
    protected ArrayList<Article> doInBackground(Void... voids) {

    }
}
