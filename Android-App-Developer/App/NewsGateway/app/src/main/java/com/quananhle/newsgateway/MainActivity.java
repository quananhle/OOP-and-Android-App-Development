package com.quananhle.newsgateway;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.quananhle.newsgateway.service.Article;
import com.quananhle.newsgateway.service.HeadlinesAdapter;
import com.quananhle.newsgateway.service.HeadlinesLoader;
import com.quananhle.newsgateway.service.Source;
import com.quananhle.newsgateway.service.SourcesDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private static final String ARTICLE_LIST = "AL";
    private static final String SOURCE = "Source";
    private static final String ACTION_NEWS_STORY = "ANS";
    private static final String ACTION_MSG_TO_SERVICE = "AMTS";
    private ArrayList<Article> articleArrayList  = new ArrayList<>();
    private ArrayList<Article> headlineArrayList = new ArrayList<>();
    private ArrayList<Source> sourceArrayList    = new ArrayList<>();

    RecyclerView recyclerView;
    HeadlinesAdapter headlinesAdapter;
    Map<String, ArrayList<Source>> sourceHashMap = new HashMap<>();
    NewsReceiver newsReceiver;
    Menu menu;
    ListView drawyerList;
    DrawerLayout drawyerLayout;
    ImageButton home;
    ActionBarDrawerToggle drawerToggle;
    List<Fragment> fragments;
    MyPageAdapter myPageAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ViewPager viewPager;
    TextView networkOffTitle, networkOffMessage, topHeadLines;
    Button retry;

    final int WARNING_ICON = 1;
    final int ERROR_ICON = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponents();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }



    //====================== *** CLASS•INSIDE•MAIN•ACTIVITY *** ======================//
    // Page Adapter
    private class MyPageAdapter extends FragmentPagerAdapter {

    }
    // News Receiver 
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

    //====================== *** HELPER•METHODS *** ======================//

    //=====* onCreate *====//
    private void setupComponents(){
        recyclerView       = findViewById(R.id.recycler_view);
        drawyerLayout      = findViewById(R.id.drawer_layout);
        drawyerList        = findViewById(R.id.drawer_list);
        topHeadLines       = findViewById(R.id.topHeadlines);
        home               = findViewById(R.id.home);
        retry              = findViewById(R.id.try_again);
        networkOffTitle    = findViewById(R.id.network_off_title);
        networkOffMessage  = findViewById(R.id.network_off_message);
        swipeRefreshLayout = findViewById(R.id.swiper);

        fragments        = new ArrayList<>();
        sourceArrayList  = new ArrayList<>();
        articleArrayList = new ArrayList<>();
        newsReceiver     = new NewsReceiver();

        myPageAdapter    = new MyPageAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(myPageAdapter);
    }
    private void setupCategories(View view){
        if (!isConnected()){
            networkOffTitle.setVisibility  (View.VISIBLE);
            networkOffMessage.setVisibility(View.VISIBLE);
            retry.setVisibility            (View.VISIBLE);
            home.setVisibility             (View.GONE);
            topHeadLines.setVisibility     (View.GONE);
        }
        else {
            new SourcesDownloader(this).execute();
            new HeadlinesLoader(this).execute();
            networkOffTitle.setVisibility  (View.GONE);
            networkOffMessage.setVisibility(View.GONE);
            retry.setVisibility            (View.GONE);
            home.setVisibility             (View.VISIBLE);
            topHeadLines.setVisibility     (View.VISIBLE);
        }
    }

    public void setSources(Map<String, ArrayList<Source>> hashMap){

    }
    public void updateHeadlines(ArrayList<Article> headlines){

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