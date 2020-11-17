package com.quananhle.newsgateway.service;

import android.view.ViewGroup;

import com.quananhle.newsgateway.MainActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesViewHolder> {
    private static final String TAG = "HeadlinesAdapter";
    private ArrayList<Article>  articleArrayList;
    private MainActivity mainActivity;
    @NonNull
    @Override
    public HeadlinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HeadlinesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
