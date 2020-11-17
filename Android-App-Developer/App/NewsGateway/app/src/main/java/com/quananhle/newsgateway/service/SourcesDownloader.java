package com.quananhle.newsgateway.service;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.quananhle.newsgateway.MainActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class SourcesDownloader extends AsyncTask<Void, Void, ArrayList<Source>> {
    private static final String TAG = "SourcesDownloader";
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;
    private Map<String, ArrayList<Source>> hashMap = new TreeMap<>();
    public SourcesDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected ArrayList<Source> doInBackground(Void... voids) {
        return null;
    }
}
