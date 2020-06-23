package com.example.Quan_Le.popular_movies_android_app.Helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Quan_Le.popular_movies_android_app.Models.Movie;
import com.example.Quan_Le.popular_movies_android_app.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.CustomMovieViewHolder> {

    private List<Movie> dataList;
    private Context context;
    final private MovieItemClickListener mOnMovieItemClickListener;

    public MoviesAdapter(Context context, List<Movie> dataList, MovieItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.mOnMovieItemClickListener = listener;
    }
    public interface MovieItemClickListener {
        void onMovieItemClick(int clickedItemIndex);
    }

    @Override
    public CustomMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mainscreen, parent, false);
        return new CustomMovieViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CustomMovieViewHolder holder, int position) {
        holder.bindMovie(dataList.get(position));
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CustomMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        @BindView(R.id.movie_title)
        TextView title;
        @BindView(R.id.movie_poster)
        ImageView poster;
        @BindView(R.id.movie_release_date)
        TextView release;
        @BindView(R.id.movie_ratings)
        TextView movieRatings;

        CustomMovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        void bindMovie(Movie movie) {
            StringBuilder releaseText = new StringBuilder().append("Release Date: ");
            releaseText.append(movie.getRelease());
            title.setText(movie.getOriginalTitle());
            release.setText(releaseText);
            movieRatings.setText(new StringBuilder("Rating: ").append(movie.getRatings()));
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(context.getResources().getString(R.string.IMAGE_BASE_URL) + movie.getPoster())
                    .placeholder((R.drawable.gradient_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(poster);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnMovieItemClickListener.onMovieItemClick(clickedPosition);
        }
    }
}
