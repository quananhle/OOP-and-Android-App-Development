package com.quananhle.newsgateway.service;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesViewHolder> {

//    private static final String TAG = "HeadLineAdapter";
//    private List<Article> headlinesList;
//    private MainActivity mainActivity;
//
//    public HeadlinesAdapter(List<Article> headlinesList, MainActivity mainActivity) {
//        this.headlinesList = headlinesList;
//        this.mainActivity = mainActivity;
//    }
//
//    @NonNull
//    @Override
//    public HeadlinesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
//    {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline,parent,false);
//        itemView.setOnClickListener(mainActivity);
//        return new HeadlinesViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull HeadlinesViewHolder holder, int position)
//    {
//        final Article temp = headlinesList.get(position);
//        holder.title.setText(temp.getTitle());
//        holder.publishedAt.setText(temp.getPublishingDate());
//        final ImageView picture = holder.image;
//        ImageButton share = holder.share;
//
//        if (temp.getImageUrl() != null || !temp.getImageUrl().equals("null")){
//            Log.d(TAG, "onBindViewHolder: (image url) " + temp.getImageUrl());
//            final String imageUrl = temp.getImageUrl().trim();
//            Picasso picasso = new Picasso.Builder(mainActivity).listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                    final String securedUrl = imageUrl.replace("http:", "https:");
//                    picasso.get().load(securedUrl)
//                            .error(R.drawable.brokenimage)
//                            .placeholder(R.drawable.placeholder)
//                            .into(picture);
//                }
//            }).build();
//            picasso.get().load(imageUrl).error(R.drawable.brokenimage)
//                    .placeholder(R.drawable.placeholder)
//                    .into(picture);
//        }
//        else {
//            Log.d(TAG, "onBindViewHolder: (null image url)");
//            picture.setImageResource(R.drawable.placeholder);
//        }
//
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                if(!isNull(temp.getUrl()))
//                {
//                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    String shareBody = "Checkout this new story from News Gateway:\n" + temp.getUrl();
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "New Gateway");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                    mainActivity.startActivity(sharingIntent);
//                }
//
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount()
//    {
//        return headlinesList.size();
//    }
//
//    private boolean isNull(String data)
//    {
//        return data == null || data.equals("null");
//    }

    private static final String TAG = "HeadlinesAdapter";
    private ArrayList<Article>  articleArrayList;
    private MainActivity mainActivity;
    private ImageView photo;
    private ImageButton share;

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
        holder.date.setText(article.getPublishingDate());
        photo = holder.image;
        share = holder.share;
        if (article.getImageUrl() != null || !article.getImageUrl().equals("null")){
            Log.d(TAG, "onBindViewHolder: (image url) " + article.getImageUrl());
            final String imageUrl = article.getImageUrl().trim();
            Picasso picasso = new Picasso.Builder(mainActivity).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String securedUrl = imageUrl.replace("http:", "https:");
                    picasso.get().load(securedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(photo);
                }
            }).build();
            picasso.get().load(imageUrl).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        }
        else {
            Log.d(TAG, "onBindViewHolder: (null image url)");
            photo.setImageResource(R.drawable.placeholder);
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (article.getUrl() != null || !article.getUrl().equals("null")){
                    Toast.makeText(mainActivity, "SHARE THE NEWS", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onBindViewHolder: (share) " + article.getUrl());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String shareMessage = (R.string.share_message) + article.getUrl();
                    intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    mainActivity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }
}
