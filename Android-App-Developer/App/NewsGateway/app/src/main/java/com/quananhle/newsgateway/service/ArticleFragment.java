package com.quananhle.newsgateway.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.newsgateway.MainActivity;
import com.quananhle.newsgateway.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ArticleFragment extends Fragment implements
        View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "ArticleFragment";
    private static MainActivity mainActivity;
    private static ArrayList<String> fragmentUrls;
    private TextView articleTitle;
    private TextView articleDate;
    private TextView articleAuthor;
    private ImageView articleImage;
    private TextView articleDescription;
    private TextView articlePageCount;
    private RecyclerView recyclerView;

    final int WARNING_ICON = 1;
    final int ERROR_ICON   = 2;
    final int NO_NETWORK   = 3;

    //default constructor
    public ArticleFragment() {
    }
    private static final ArticleFragment newInstance(MainActivity mainActivity, String title,
                                                     String date, String author, String image,
                                                     String description, int index, int total, String url){
        mainActivity = mainActivity;
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle bundle = new Bundle(5);
        bundle.putString("title", title);
        bundle.putString("date", date);
        bundle.putString("author", author);
        bundle.putString("image", image);
        bundle.putString("description", description);
        bundle.putInt("i", index);
        bundle.putInt("n", total);
        articleFragment.setArguments(bundle);
        return articleFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        articleTitle       = view.findViewById(R.id.title);
        articleDate        = view.findViewById(R.id.published_date);
        articleAuthor      = view.findViewById(R.id.author);
        articleImage       = view.findViewById(R.id.image);
        articleDescription = view.findViewById(R.id.description);
        articlePageCount   = view.findViewById(R.id.page_number);
        recyclerView       = view.findViewById(R.id.recycler_view);

        articleTitle.setOnClickListener(this);
        articleImage.setOnClickListener(this);
        articleDescription.setOnClickListener(this);

        String title = (getArguments().getString("title") == null
                ? "" : getArguments().getString("title"));
        String date  = getArguments().getString("date");
        if (date == null || date.equals("null")){
            articleDate.setVisibility(View.GONE);
        }
        String author = (getArguments().getString("author"));
        if (author == null || author.equals("null")){
            articleAuthor.setVisibility(View.GONE);
        }
        String image = getArguments().getString("image");
        String description = getArguments().getString("description");
        if (description == null || description.equals("null") || description.isEmpty()){
            description = getString(R.string.no_description);
        }
        Log.d(TAG, "onCreateView: description " + description);
        int index = getArguments().getInt("i");
        int total   = getArguments().getInt("n");
        Log.d(TAG, "onCreateView: ");
        articleTitle.setText(title);
        articleDate.setText(date);
        articleAuthor.setText(author);
        articleDescription.setText(description);
        articlePageCount.setText(String.format(Locale.US, "%d of %d", index, total));

        articleDescription.setMovementMethod(new ScrollingMovementMethod());
        articleImage.setImageResource(R.drawable.placeholder);

        if (isConnected()){
            if (image == null){
                Log.d(TAG, "onCreateView: null image url");
                articleImage.setVisibility(View.GONE);
            }
            else {
                final String imageUrl = image;
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
        }
        else {
            Log.d(TAG, "onCreateView: network off");
            showMessage(NO_NETWORK, "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
        }
        return view;
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title: case R.id.image: case R.id.description:
                Log.d(TAG, "onClick: title image description");
                View v = (View) view.getParent();
                break;
            default:
                Log.d(TAG, "onClick: somewhere else");
        }
    }
    @Override
    public boolean onLongClick(View view){
        int pos = rec
        return false;
    }

    //====================== *** HELPER•METHODS *** ======================//
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
