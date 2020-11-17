package com.quananhle.newsgateway.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesViewHolder> {
    private static final String TAG = "HeadlinesAdapter";
    private ArrayList<Article>  articleArrayList;
    private MainActivity mainActivity;

    public HeadlinesAdapter(ArrayList<Article> articleArrayList, MainActivity mainActivity) {
        this.articleArrayList = articleArrayList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public HeadlinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline, parent, false);
        itemView.setOnClickListener(mainActivity);
        return new HeadlinesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HeadlinesViewHolder holder, int position) {
        final Article article = articleArrayList.get(position);
        holder.title.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
