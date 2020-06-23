package com.example.Quan_Le.popular_movies_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.Quan_Le.popular_movies_android_app.Helper.*;
import com.example.Quan_Le.popular_movies_android_app.Models.*;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    public RecyclerView rv;
    @BindView(R.id.loading_indicator)
    public ProgressBar pb;
    private static Retrofit retrofit;
    private static String API_KEY;
    public List<Movie> movies;
    private int currentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.most_watched);
        API_KEY = getResources().getString(R.string.API_KEY);
        ButterKnife.bind(this);
        pb.setVisibility(View.VISIBLE);
        getPopularMovies();
    }
    private void getPopularMovies() {
        if (MoviesUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = API.getRetrofitInstance();
            }
            MoviesInterface movieService = retrofit.create(MoviesInterface.class);
            Call<MovieResponse> call = movieService.getMostWatched(API_KEY, getResources().getString(R.string.LANGUAGE), currentPage);
            Log.i("Most-Watched Movies API", movieService.getMostWatched(API_KEY, getResources().getString(R.string.LANGUAGE), 1).request().url().toString());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        pb.setVisibility(View.INVISIBLE);
                        movies = response.body().getResults();
                        if (movies != null) {
                            generateMovieList(movies);
                            Log.d(TAG, "Number of Most-Watched Movies: " + movies.size());
                        }
                    }
                }
                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Error! Try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void getTopRatedMovies() {
        if (MoviesUtils.getInstance().isNetworkAvailable(this)) {
            if (retrofit == null) {
                retrofit = API.getRetrofitInstance();
            }
            MoviesInterface movieService = retrofit.create(MoviesInterface.class);
            Call<MovieResponse> call = movieService.getTopRated(API_KEY, getResources().getString(R.string.LANGUAGE), currentPage);
            Log.i("Top-Rated Movies API", movieService.getTopRated(API_KEY, getResources().getString(R.string.LANGUAGE), 1).request().url().toString());
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful()) {
                        pb.setVisibility(View.INVISIBLE);
                        movies = response.body().getResults();
                        if (movies != null) {
                            generateMovieList(movies);
                            Log.d(TAG, "Number of Top-Rated Movies: " + movies.size());
                        }
                    }
                }
                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    pb.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Error! Try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void generateMovieList(final List<Movie> results) {
        MoviesAdapter adapter = new MoviesAdapter(this, results, new MoviesAdapter.MovieItemClickListener() {
            @Override
            public void onMovieItemClick(int clickedItemIndex) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("Movie", results.get(clickedItemIndex));
                startActivity(intent);
            }
        });

        gridView(adapter);
    }
    private void gridView(MoviesAdapter adapter) {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.top_rated:
                getTopRatedMovies();
                setTitle(R.string.top_rated);
                break;
            case R.id.most_watched:
                getPopularMovies();
                setTitle(R.string.most_watched);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
