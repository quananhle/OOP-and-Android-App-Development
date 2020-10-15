// Generated code from Butter Knife. Do not modify!
package com.example.Quan_Le.popular_movies_android_app;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.rv = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'rv'", RecyclerView.class);
    target.pb = Utils.findRequiredViewAsType(source, R.id.loading_indicator, "field 'pb'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rv = null;
    target.pb = null;
  }
}
