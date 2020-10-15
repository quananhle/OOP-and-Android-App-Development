// Generated code from Butter Knife. Do not modify!
package com.example.Quan_Le.popular_movies_android_app;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DetailsActivity_ViewBinding implements Unbinder {
  private DetailsActivity target;

  @UiThread
  public DetailsActivity_ViewBinding(DetailsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DetailsActivity_ViewBinding(DetailsActivity target, View source) {
    this.target = target;

    target.moviePoster = Utils.findRequiredViewAsType(source, R.id.details_poster, "field 'moviePoster'", ImageView.class);
    target.movieTitle = Utils.findRequiredViewAsType(source, R.id.details_title, "field 'movieTitle'", TextView.class);
    target.movieLanguage = Utils.findRequiredViewAsType(source, R.id.details_language, "field 'movieLanguage'", TextView.class);
    target.moviePlot = Utils.findRequiredViewAsType(source, R.id.details_about, "field 'moviePlot'", TextView.class);
    target.movieReleaseDate = Utils.findRequiredViewAsType(source, R.id.details_release_date, "field 'movieReleaseDate'", TextView.class);
    target.movieRatings = Utils.findRequiredViewAsType(source, R.id.details_users_rating, "field 'movieRatings'", TextView.class);
    target.moviePopularity = Utils.findRequiredViewAsType(source, R.id.details_popularity, "field 'moviePopularity'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DetailsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.moviePoster = null;
    target.movieTitle = null;
    target.movieLanguage = null;
    target.moviePlot = null;
    target.movieReleaseDate = null;
    target.movieRatings = null;
    target.moviePopularity = null;
  }
}
