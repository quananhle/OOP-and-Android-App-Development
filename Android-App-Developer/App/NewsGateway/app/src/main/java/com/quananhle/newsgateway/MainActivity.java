package com.quananhle.newsgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.quananhle.newsgateway.service.Article;
import com.quananhle.newsgateway.service.HeadlinesAdapter;
import com.quananhle.newsgateway.service.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private ArrayList<Article> articleArrayList  = new ArrayList<>();
    private ArrayList<Article> headlineArrayList = new ArrayList<>();
    private ArrayList<Source> sourceArrayList    = new ArrayList<>();
    HeadlinesAdapter headlinesAdapter;
    Map<String, ArrayList<Source>> sourceHashMap = new HashMap<>();
    


    private static final String ACTION_NEWS_STORY = "ANS";
    private static final String ACTION_MSG_TO_SERVICE = "AMTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }



    public void setSources(Map<String, ArrayList<Source>> hashMap){

    }
    public void updateHeadlines(ArrayList<Article> headlines){

    }


    public class NewsReceiver extends BroadcastReceiver {
        private static final String TAG = "NewsReceiver";
        @Override
        public void onReceive(Context context, Intent intent){
            switch (intent.getAction()){
                case ACTION_NEWS_STORY:
                    try {
                        Bundle bundle = intent.getExtras();
                        ArrayList<Article> articles = (ArrayList<Article>)
                                bundle.getSerializable("articleArrayList");
                        Log.d(TAG, "onReceive: " + TAG + " | Articles received by onReceive");
                        for (int i=0; i < articles.size(); ++i){
                            Log.d(TAG, "onReceive: title: " + articles.get(i).getTitle());
                        }
                        reDoFragment(articles);
                    }
                    catch (Exception e){
                        Log.d(TAG, "onReceive: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

}