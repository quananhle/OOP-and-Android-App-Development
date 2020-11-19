package com.quananhle.newsgateway.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.quananhle.newsgateway.MainActivity;
import java.util.ArrayList;
import androidx.annotation.Nullable;

public class NewsService extends Service {
    private static final String TAG = "NewsService";
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private ServiceReceiver serviceReceiver;
    private boolean isRunning = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        IntentFilter intentFilter = new IntentFilter(MainActivity.ACTION_MSG_TO_SERVICE);
        serviceReceiver = new ServiceReceiver();
        registerReceiver(serviceReceiver, intentFilter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    while (articleArrayList.isEmpty()){
                        try {
                            Thread.sleep(250);
                        }
                        catch (InterruptedException ie){
                            ie.printStackTrace();
                            Log.d(TAG, "NewsService: " + TAG + " | InterruptedException: " + ie);
                        }
                    }
                    Intent intent = new Intent();
                    intent.setAction(MainActivity.ACTION_NEWS_STORY);
                    intent.putExtra(MainActivity.ARTICLE_LIST, articleArrayList);
                    sendBroadcast(intent);
                    articleArrayList.clear();
                }
                Log.i(TAG, "NewsService: accomplished");
            }
        }).start();
        return Service.START_STICKY;
    }

    public void setArticles(ArrayList<Article> articles){
        articleArrayList.clear();
        for (int i=0; i < articles.size(); ++i){
            articleArrayList.add(articles.get(i));
        }
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(serviceReceiver);
        isRunning = false;
        super.onDestroy();
    }

    class ServiceReceiver extends BroadcastReceiver {
        private static final String TAG = "ServiceReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + TAG + ": intent's action is " + intent.getAction());
            switch (intent.getAction()){
                case MainActivity.ACTION_MSG_TO_SERVICE:
                    Source source = intent.hasExtra(MainActivity.SOURCE)
                            ? (Source) intent.getSerializableExtra(MainActivity.SOURCE) : null;
                    assert source != null;
                    new ArticlesDownloader(NewsService.this).execute(source.getId());
                    break;
            }
        }
    }
}
