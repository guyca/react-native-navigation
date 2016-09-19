package com.reactnativenavigation.views.collapsingToolbar;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

public class ScrollViewDelegate implements ViewTreeObserver.OnScrollChangedListener {

    public interface OnScrollListener {
        void onScroll(ScrollView scrollView);
    }

    private ScrollView scrollView;
    private OnScrollListener listener;

    public ScrollViewDelegate() {
    }

    public void onViewAdded(View child) {
        if (child instanceof ScrollView) {
            detach();
            scrollView = (ScrollView) child;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        ScrollViewDelegate.this.onScrollChanged();
                    }
                });
            } else {
                scrollView.getViewTreeObserver().addOnScrollChangedListener(this);
            }
        }
    }

    private void detach() {
        if (scrollView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                scrollView.setOnScrollChangeListener(null);
            } else {
                scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
            }
            scrollView = null;
        }
    }

    public void setListener(OnScrollListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrollChanged() {
        if (this.listener != null) {
            this.listener.onScroll(scrollView);
        }
    }
}
