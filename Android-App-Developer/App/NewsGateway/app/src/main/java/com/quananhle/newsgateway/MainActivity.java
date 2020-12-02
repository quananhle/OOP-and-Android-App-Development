package com.quananhle.newsgateway;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.quananhle.newsgateway.service.Article;
import com.quananhle.newsgateway.service.ArticleFragment;
import com.quananhle.newsgateway.service.HeadlinesAdapter;
import com.quananhle.newsgateway.service.HeadlinesLoader;
import com.quananhle.newsgateway.service.NewsService;
import com.quananhle.newsgateway.service.Source;
import com.quananhle.newsgateway.service.SourcesDownloader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    public static final String ARTICLE_LIST = "AL";
    public static final String SOURCE = "Source";
    public static final String ACTION_NEWS_STORY = "ANS";
    public static final String ACTION_MSG_TO_SERVICE = "AMTS";
    private ArrayList<Article> articleArrayList  = new ArrayList<>();
    private ArrayList<Article> headlineArrayList = new ArrayList<>();
    private ArrayList<Source> sourceArrayList    = new ArrayList<>();

    RecyclerView recyclerView;
    HeadlinesAdapter headlinesAdapter;
    Map<String, ArrayList<Source>> sourceHashMap = new HashMap<>();
    NewsReceiver newsReceiver;
    Menu menu;
    ListView drawerList;
    DrawerLayout drawerLayout;
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
    final int NO_NETWORK   = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupComponents();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                drawerLayout.closeDrawer(drawerList);
                recyclerView.setAdapter(headlinesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                new HeadlinesLoader(MainActivity.this).execute();
                Toast.makeText(MainActivity.this, "Most Recent Headlines loaded", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        headlinesAdapter = new HeadlinesAdapter(headlineArrayList,this);
        recyclerView.setAdapter(headlinesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent serviceIntent = new Intent(this, NewsService.class);
        startService(serviceIntent);

        drawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Source temp = sourceArrayList.get(position);
                        Intent intent = new Intent(MainActivity.ACTION_MSG_TO_SERVICE);
                        intent.putExtra(SOURCE, (Serializable) temp);
                        sendBroadcast(intent);
                        setTitle(temp.getCompany());
                        viewPager.setVisibility(View.VISIBLE);
                        viewPager.setBackgroundResource(R.color.midnightBlack);
                        Snackbar.make(view,temp.getCompany() + " Selected", Snackbar.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(drawerList);
                        swipeRefreshLayout.setEnabled(false);

                    }
                }
        );

        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new HeadlinesLoader(MainActivity.this).execute();
                topHeadLines.setVisibility(View.VISIBLE);
                setTitle(R.string.app_name);
                viewPager.setVisibility(View.GONE);
                recyclerView.scrollToPosition(0);
                swipeRefreshLayout.setEnabled(true);
                sourceArrayList.clear();
                sourceArrayList.addAll(sourceHashMap.get("all"));
                ((ArrayAdapter) drawerList.getAdapter()).notifyDataSetChanged();
            }
        });

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        );

        if(isConnected())
        {
            new SourcesDownloader(this).execute();
            new HeadlinesLoader(this).execute();
            networkOffTitle.setVisibility(View.GONE);
            networkOffMessage.setVisibility(View.GONE);
            retry.setVisibility(View.GONE);
            home.setVisibility(View.VISIBLE);
            topHeadLines.setVisibility(View.VISIBLE);

        }
        else
        {
            networkOffTitle.setVisibility(View.VISIBLE);
            networkOffMessage.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
            home.setVisibility(View.GONE);
            topHeadLines.setVisibility(View.GONE);
        }

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter1);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        Intent intent = new Intent(this, NewsService.class);
        stopService(intent);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        setTitle(item.getTitle().toString().trim());
        sourceArrayList.clear();
        ArrayList<Source> sources = sourceHashMap.get(item.getTitle().toString().toLowerCase());
        if (sources != null){
            sourceArrayList.addAll(sources);
        }
        ((ArrayAdapter) drawerList.getAdapter()).notifyDataSetChanged();
        Toast.makeText(this, "Total Sources Loaded: " + sources.size(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        Article article = headlineArrayList.get(position);
        if (article.getUrl() != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
            startActivity(intent);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d(TAG, "onCreateView: Share news ");
        int position = recyclerView.getChildAdapterPosition(view);
        Article article = headlineArrayList.get(position);
        if (article.getUrl() != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareMessage = getString(R.string.share_message) + "\n" + Uri.parse(article.getUrl());
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(intent, "Share via "));
        }
        return false;
    }

    //====================== *** CLASS•INSIDE•MAIN•ACTIVITY *** ======================//
    // Page Adapter
    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;
        public MyPageAdapter(FragmentManager fragmentManager){
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
        @Override
        public int getItemPosition(@NonNull Object object){
            return POSITION_NONE;
        }
        @NonNull
        @Override
        public Fragment getItem(int position){
            return fragments.get(position);
        }
        @Override
        public int getCount(){
            return fragments.size();
        }
        @Override
        public long getItemId(int position){
            return baseId + position;
        }
        public void notifyChangeInPosition(int n){
            baseId += getCount() + n;
        }
    }
    // News Receiver
    public class NewsReceiver extends BroadcastReceiver {
        private static final String TAG = "NewsReceiver";
        @Override
        public void onReceive(Context context, Intent intent){
            articleArrayList.clear();
            articleArrayList = intent.hasExtra(ARTICLE_LIST)
                    ? (ArrayList<Article>) intent.getSerializableExtra(ARTICLE_LIST) : new ArrayList<Article>();
            reDoFragment();
        }
    }

    //====================== *** HELPER•METHODS *** ======================//

    //=====* onCreate *====//
    private void setupComponents(){
        recyclerView       = findViewById(R.id.recycler_view);
        drawerLayout       = findViewById(R.id.drawer_layout);
        drawerList         = findViewById(R.id.drawer_list);
        topHeadLines       = findViewById(R.id.headline_button);
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
            Log.d(TAG, "onCreateView: network off");
            showMessage(NO_NETWORK, "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
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
    private void setSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                drawerLayout.closeDrawer(drawerList);
                recyclerView.setAdapter(headlinesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                Toast.makeText(MainActivity.this, "MOST RECENT NEWS UPDATED", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private void setDrawerToggle(){
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer);
    }
    private void setDrawerList(){
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Source source = sourceArrayList.get(position);
                Intent intent = new Intent(MainActivity.ACTION_MSG_TO_SERVICE);
                intent.putExtra(SOURCE, (Serializable) source);
                sendBroadcast(intent);
                setTitle(source.getCompany());
                viewPager.setVisibility(View.VISIBLE);
                viewPager.setBackgroundResource(R.color.darkSlateGray);
                Snackbar.make(view, source.getCompany() + " SELECTED", Snackbar.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(drawerList);
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }
    private void setHomeButton(){
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HeadlinesLoader(MainActivity.this).execute();
                topHeadLines.setVisibility(View.VISIBLE);
                setTitle(R.string.app_name);
                viewPager.setVisibility(View.GONE);
                recyclerView.scrollToPosition(0);
                swipeRefreshLayout.setEnabled(true);
                sourceArrayList.clear();
                sourceArrayList.addAll(sourceHashMap.get("ALL"));
                ((ArrayAdapter) drawerList.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    //=====* NewsReceiver.class *====//
    private void reDoFragment(){
        for (int i=0; i < myPageAdapter.getCount(); ++i){
            myPageAdapter.notifyChangeInPosition(i);
        }
        fragments.clear();
        for (int i=0; i < articleArrayList.size(); ++i){
            fragments.add(ArticleFragment.newInstance
                    (articleArrayList.get(i), 1+i, articleArrayList.size()));
        }
        myPageAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
    }

    //=====* SourcesDownloader.class *====//
    public void setSources(Map<String, ArrayList<Source>> hashMap){
        try {
            menu.clear();
            sourceHashMap = hashMap;
            for (String category : hashMap.keySet()){
                String formatter = "";
                String[] strings = category.split("_");
                for (String string : strings){
                    String s = string.toLowerCase();
                    int size = s.length();
                    formatter = String.format("%s%s%s", formatter,
                            s.substring(0, 1).toUpperCase(),
                            s.substring(1, size));
                }
                menu.add(formatter);
            }
            sourceArrayList.addAll(Objects.requireNonNull(sourceHashMap.get("all")));
            drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, sourceArrayList));
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
        }
        catch (Exception e){
            Log.d(TAG, "setSources: Exception" + e);
            new SourcesDownloader(this).execute();
        }
    }

    //=====* SourcesLoaderRunnable.class *====//
    public void updateData(ArrayList<Source> listIn) {
        for (Source source : listIn) {
            if (!sourceHashMap.containsKey(source.ge())) {
                sourceHashMap.put(article.getTitle(), new ArrayList<Source>());
            }
            ArrayList<Source> clist = countryData.get(c.getSubRegion());
            if (clist != null) {
                clist.add(c);
            }
        }

        countryData.put("All", listIn);

        ArrayList<String> tempList = new ArrayList<>(countryData.keySet());
        Collections.sort(tempList);
        for (String s : tempList)
            opt_menu.add(s);


        countryList.addAll(listIn);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, countryList);
        mDrawerList.setAdapter(arrayAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }
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
        } else if (icon == NO_NETWORK) {
            dialog.setIcon(R.drawable.network_off);
        } else {
            dialog.setIcon(null);
        }
        dialog.show();
    }
}