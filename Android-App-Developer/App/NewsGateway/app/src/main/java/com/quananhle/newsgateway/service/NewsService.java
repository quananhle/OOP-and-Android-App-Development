package com.quananhle.newsgateway.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import androidx.annotation.Nullable;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.R;
import com.quananhle.newsgateway.loader.ArticlesLoaderRunnable;


public class NewsService extends Service {
    private static final String TAG = "NewsService";
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private ServiceReceiver serviceReceiver;
    private boolean isRunning = true;

    final int WARNING_ICON = 1;
    final int ERROR_ICON = 2;

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
//                    new ArticlesDownloader(NewsService.this).execute(source.getId());
                    doRunnable(source.getId());
                    break;
            }
        }
    }


    //====================== *** HELPER•METHODS *** ======================//

    //=====* ArticlesLoaderRunnable *====//
    public void doRunnable(String source){
        if (isConnected()){
            if (source.isEmpty()){
                Toast.makeText(this, "Location is missing", Toast.LENGTH_SHORT).show();
                return;
            }
            //Load the data
            ArticlesLoaderRunnable officialLoaderRunnable = new ArticlesLoaderRunnable(this, source);
            new Thread(officialLoaderRunnable).start();
        }
    }

    //=====* ArticleLoaderRunnable.class *====//
    public void downloadFailed() {
        articleArrayList.clear();
    }

    //=====* Logistic methods *====//
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager == null) {
            return false;
        } else if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void showMessage(int icon, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (icon == WARNING_ICON) {
            dialog.setIcon(R.drawable.warning);
        } else if (icon == ERROR_ICON) {
            dialog.setIcon(R.drawable.error);
        } else {
            dialog.setIcon(null);
        }
        dialog.show();
    }
}