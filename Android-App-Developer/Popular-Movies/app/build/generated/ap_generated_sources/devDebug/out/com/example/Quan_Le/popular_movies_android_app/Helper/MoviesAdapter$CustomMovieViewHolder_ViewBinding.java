// Generated code from Butter Knife. Do not modify!
package com.example.Quan_Le.popular_movies_android_app.Helper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.Quan_Le.popular_movies_android_app.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MoviesAdapter$CustomMovieViewHolder_ViewBinding implements Unbinder {
  private MoviesAdapter.CustomMovieViewHolder target;

  @UiThread
  public MoviesAdapter$CustomMovieViewHolder_ViewBinding(MoviesAdapter.CustomMovieViewHolder target,
      View source) {
    this.target = target;

    target.title = Utils.findRequiredViewAsType(source, R.id.movie_title, "field 'title'", TextView.class);
    target.poster = Utils.findRequiredViewAsType(source, R.id.movie_poster, "field 'poster'", ImageView.class);
    target.release = Utils.findRequiredViewAsType(source, R.id.movie_release_date, "field 'release'", TextView.class);
    target.movieRatings = Utils.findRequiredViewAsType(source, R.id.movie_ratings, "field 'movieRatings'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MoviesAdapter.CustomMovieViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.title = null;
    target.poster = null;
    target.release = null;
    target.movieRatings = null;
  }
}
