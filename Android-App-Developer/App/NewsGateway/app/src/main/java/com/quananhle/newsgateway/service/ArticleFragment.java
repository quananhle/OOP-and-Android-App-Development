package com.quananhle.newsgateway.service;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.newsgateway.MainActivity;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class ArticleFragment extends Fragment implements
        View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "ArticleFragment";
    private static MainActivity mainActivity;
    private static ArrayList<String> fragmentUrls;
    private TextView title;
    private TextView date;
    private TextView author;
    private ImageView articleImage;
    private TextView description;
    private TextView articleCount;
    //default constructor
    public ArticleFragment() {
    }
    private static final ArticleFragment newInstance(MainActivity mainActivity, String title,
                                                     String date, String author, String image,
                                                     String description, int index, int max, String url){
        mainActivity = mainActivity;
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bundle = new Bundle(5);
        bundle.putString("title", title);
        bundle.putString("date", date);
        bundle.putString("author", author);
        bundle.putString("image", image);
        bundle.putString("description", description);
        bundle.putInt("i", index);
        bundle.putInt("n", max);
        articleFragment.setArguments(bundle);
        return articleFragment;
    }
    @Override
    public View onCreateView(){
        
    }

}
