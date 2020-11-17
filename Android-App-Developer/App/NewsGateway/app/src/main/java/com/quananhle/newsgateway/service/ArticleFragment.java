package com.quananhle.newsgateway.service;

import android.view.View;

import com.quananhle.newsgateway.MainActivity;

import androidx.fragment.app.Fragment;

public class ArticleFragment extends Fragment implements
        View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "ArticleFragment";
    //default constructor
    public ArticleFragment() {
    }
    private static final ArticleFragment newInstance(MainActivity mainActivity, String title,
                                                     String date, String author, String image,
                                                     String description, int index, int max, String url){
        
    }

}
