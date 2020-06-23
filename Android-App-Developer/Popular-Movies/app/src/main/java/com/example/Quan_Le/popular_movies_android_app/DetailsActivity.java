package com.example.Quan_Le.popular_movies_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Quan_Le.popular_movies_android_app.Models.Movie;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_poster)
    ImageView moviePoster;
    @BindView(R.id.details_title)
    TextView movieTitle;
    @BindView(R.id.details_language)
    TextView movieLanguage;
    @BindView(R.id.details_about)
    TextView moviePlot;
    @BindView(R.id.details_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.details_users_rating)
    TextView movieRatings;
    @BindView(R.id.details_popularity)
    TextView moviePopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        bindSelectedMovieData();
    }

    private void bindSelectedMovieData() {
        Intent intent = getIntent();
        Movie selectedMovie = intent.getParcelableExtra("Movie");

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        builder.build().load(this.getResources().getString(R.string.IMAGE_BASE_URL) + selectedMovie.getBackdrop())
                .placeholder((R.drawable.gradient_background))
                .error(R.drawable.ic_launcher_background)
                .into(moviePoster);

        movieTitle.setText(selectedMovie.getTitle());
        movieLanguage.setText(new StringBuilder("Language: ").append(selectedMovie.getLanguage()));
        movieReleaseDate.setText(new StringBuilder("Release Date: ").append(selectedMovie.getRelease()));
        movieRatings.setText(new StringBuilder("Rating: ").append(selectedMovie.getRatings()));
        moviePlot.setText(selectedMovie.getOverview());
    }
}