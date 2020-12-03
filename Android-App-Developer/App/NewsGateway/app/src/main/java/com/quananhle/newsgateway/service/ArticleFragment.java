package com.quananhle.newsgateway.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.quananhle.newsgateway.R;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ArticleFragment extends Fragment {
    private static final String TAG = "ArticleFragment";
    public ArticleFragment() { }

    public static ArticleFragment newInstance(Article article, int index, int max) {
        ArticleFragment f = new ArticleFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("ARTICLE_DATA", article);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        f.setArguments(bdl);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment_layout = inflater.inflate(R.layout.article_fragment, container, false);
        Bundle args = getArguments();
        if (args != null) {
            final Article article = (Article) args.getSerializable("ARTICLE_DATA");
            if (article == null) {
                return null;
            }
            int index = args.getInt("INDEX");
            int total = args.getInt("TOTAL_COUNT");

            if(article.getUrl() != null){
                shareNews(fragment_layout,article.getUrl());
            }

            if(!isNull(article.getTitle())) {
                TextView title = fragment_layout.findViewById(R.id.title);
                title.setText(article.getTitle());
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(article.getUrl() != null)
                            articleClicked(article.getUrl());
                    }
                });
            }
            else {
                TextView title = fragment_layout.findViewById(R.id.title);
                title.setText(R.string.network_off_title);
                title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        if(article.getUrl() != null)
                            articleClicked(article.getUrl());
                    }
                });
            }
            if(!isNull(article.getAuthor())) {
                TextView author = fragment_layout.findViewById(R.id.author);
                author.setText(article.getAuthor());
            }
            if(!isNull(article.getPublishingDate())) {
                TextView publishingDate = fragment_layout.findViewById(R.id.published_date);
                publishingDate.setText(article.getPublishingDate());
            }
            if(!isNull(article.getDescription())) {
                TextView description = fragment_layout.findViewById(R.id.description);
                description.setText(article.getDescription());
                description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(article.getUrl() != null)
                            articleClicked(article.getUrl());
                    }
                });
            }

            if(isNull(article.getImageUrl())) {
                ImageView photo = fragment_layout.findViewById(R.id.image);
                photo.setBackgroundResource(R.drawable.placeholder);
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(article.getUrl() != null)
                            articleClicked(article.getUrl());
                    }
                });
            }
            else {
                ImageView picture = fragment_layout.findViewById(R.id.image);
                Glide.with(this)
                        .load(article.getImageUrl())
                        .placeholder(R.drawable.brokenimage)
                        .error(R.drawable.error)
                        .into(picture);
                picture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(article.getUrl() != null)
                            articleClicked(article.getUrl());
                    }
                });
            }
            TextView pageNum = fragment_layout.findViewById(R.id.page_number);
            pageNum.setText(String.format(Locale.US, "%d of %d", index, total));
            return fragment_layout;
        }
        else {
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
                String shareMessage = getString(R.string.share_message) + "\n" + URL;
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
    private boolean isNull(String data) {
        if(data == null || data.equals("null")){
            return true;
        }
        else {
            return false;
        }
    }
}
