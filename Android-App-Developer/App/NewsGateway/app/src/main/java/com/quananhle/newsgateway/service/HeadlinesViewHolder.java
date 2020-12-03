package com.quananhle.newsgateway.service;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.newsgateway.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeadlinesViewHolder extends RecyclerView.ViewHolder {
    public TextView title, date;
    public ImageButton share;
    public ImageView image;

    public HeadlinesViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        date  = itemView.findViewById(R.id.published_date);
        share = itemView.findViewById(R.id.share);
        image = itemView.findViewById(R.id.image);
    }
//ImageButton share;
//    TextView title, publishedAt;
//    ImageView image;
//    HeadlinesViewHolder(@NonNull View itemView)
//    {
//        super(itemView);
//        title = itemView.findViewById(R.id.title);
//        publishedAt = itemView.findViewById(R.id.published_date);
//        image = itemView.findViewById(R.id.image);
//        share = itemView.findViewById(R.id.share);
//    }
}
