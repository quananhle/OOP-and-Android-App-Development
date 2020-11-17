package com.quananhle.newsgateway.service;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ArticleFragment extends Fragment {
    private static final String TAG = "ArticleFragment";
    private static MainActivity mainActivity;
    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleAuthor;
    private ImageView articleImage;
    private TextView articleDescription;
    private TextView articlePageCount;

    final int WARNING_ICON = 1;
    final int ERROR_ICON   = 2;
    final int NO_NETWORK   = 3;

    //default constructor
    public ArticleFragment() {
    }
    private static final ArticleFragment newInstance(Article article, int index, int total){
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("ARTICLE", article);
        bundle.putSerializable("INDEX", index);
        bundle.putSerializable("TOTAL", total);
        articleFragment.setArguments(bundle);
        return articleFragment;
    }
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        articleTitle       = view.findViewById(R.id.title);
        articleDate        = view.findViewById(R.id.published_date);
        articleAuthor      = view.findViewById(R.id.author);
        articleImage       = view.findViewById(R.id.image);
        articleDescription = view.findViewById(R.id.description);
        articlePageCount   = view.findViewById(R.id.page_number);

        if (!getArguments().isEmpty() || getArguments() != null) {
            final Article article = (Article) getArguments().getSerializable("ARTICLE") == null
                    ? null : (Article) getArguments().getSerializable("ARTICLE");
            Log.d(TAG, "onCreateView: (Article) ");

            if (article.getUrl() != null) {
                shareNews(view, article.getUrl());
            }
            if (article.getTitle() != null || !article.getTitle().equals("null")) {
                articleTitle.setText(article.getTitle());
                Log.d(TAG, "onCreateView: (Title) " + articleTitle.toString().trim());
                articleTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "OPEN ARTICLE", Toast.LENGTH_SHORT).show();
                            articleClicked(article.getUrl());
                        }
                    }
                });
                articleTitle.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "SHARE THE NEWS", Toast.LENGTH_SHORT).show();
                            shareNews(v, article.getUrl());
                        }
                        return false;
                    }
                });
            } else {
                articleTitle.setText(R.string.null_title);
                Log.d(TAG, "onCreateView: (Title) " + R.string.null_title);
                articleTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "OPEN ARTICLE", Toast.LENGTH_SHORT).show();
                            articleClicked(article.getUrl());
                        }
                    }
                });
                articleTitle.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "SHARE THE NEWS", Toast.LENGTH_SHORT).show();
                            shareNews(v, article.getUrl());
                        }
                        return false;
                    }
                });
            }
            if (article.getAuthor() != null || !article.getAuthor().equals("null")) {
                articleAuthor.setText(article.getAuthor());
                Log.d(TAG, "onCreateView: (Author) " + articleAuthor.toString().trim());
            }
            if (article.getPublishingDate() != null || !article.getPublishingDate().equals("null")) {
                articleDate.setText(article.getPublishingDate());
                Log.d(TAG, "onCreateView: (Publishing Date) " + articleDate.toString().trim());
            }
            if (article.getDescription() != null || !article.getDescription().equals("null")
                                                 || !article.getDescription().isEmpty()) {
                articleDescription.setText(article.getDescription());
                Log.d(TAG, "onCreateView: (Description) ");
                articleDescription.setMovementMethod(new ScrollingMovementMethod());
                articleDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "OPEN ARTICLE", Toast.LENGTH_SHORT).show();
                            articleClicked(article.getUrl());
                        }
                    }
                });
                articleDescription.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "SHARE THE NEWS", Toast.LENGTH_SHORT).show();
                            shareNews(v, article.getUrl());
                        }
                        return false;
                    }
                });
            }
            else {
                articleDescription.setText(getString(R.string.no_description));
            }
            if (article.getImageUrl() != null || article.getImageUrl().equals("null")) {
                articleImage.setImageResource(R.drawable.placeholder);
                if (isConnected()) {
                    final String imageUrl = article.getImageUrl().trim();
                    Picasso picasso = new Picasso.Builder(mainActivity).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            final String securedUrl = imageUrl.replace("http:", "https:");
                            picasso.get().load(securedUrl)
                                    .error(R.drawable.brokenimage)
                                    .placeholder(R.drawable.placeholder)
                                    .into(articleImage);
                        }
                    }).build();
                    picasso.get().load(imageUrl).error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(articleImage);
                }
                else {
                    Log.d(TAG, "onCreateView: network off");
                    showMessage(NO_NETWORK, "NO NETWORK CONNECTION",
                            "Data cannot be accessed/loaded without an Internet connection");
                }
                articleImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (article.getUrl() != null || !article.getUrl().equals("null")){
                            Toast.makeText(mainActivity, "OPEN ARTICLE", Toast.LENGTH_SHORT).show();
                            articleClicked(article.getUrl());
                        }
                    }
                });
                articleDescription.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (article.getUrl() != null || !article.equals("null")) {
                            Toast.makeText(mainActivity, "SHARE THE NEWS", Toast.LENGTH_SHORT).show();
                            shareNews(v, article.getUrl());
                        }
                        return false;
                    }
                });
            }
            else {
                Log.d(TAG, "onCreateView: null image url");
                articleImage.setVisibility(View.GONE);
            }
            int index = getArguments().getInt("INDEX");
            int total   = getArguments().getInt("TOTAL");
            articlePageCount.setText(String.format(Locale.US, "%d of %d", index, total));
            return view;
        }
        else {
            Log.d(TAG, "onCreateView: null");
            return null;
        }
    }
    //====================== *** HELPER•METHODS *** ======================//
    private void shareNews(View view, final String URL){
        Log.d(TAG, "onCreateView: Share news ");
        ImageButton imageButton = view.findViewById(R.id.share);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareMessage = getString(R.string.share_message) + URL;
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(intent, "Share via "));
            }
        });
    }
    private void articleClicked(final String URL){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
        startActivity(intent);
    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        AlertDialog dialog = new AlertDialog.Builder(mainActivity).create();
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
