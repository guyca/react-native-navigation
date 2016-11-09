package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class ScrollViewDelegate implements View.OnTouchListener {
    interface OnScrollListener {
        boolean onTouch(MotionEvent event);

        void onScrollViewAdded(ScrollView scrollView);
    }

    private ScrollView scrollView;
    private OnScrollListener listener;

    public ScrollViewDelegate(OnScrollListener scrollListener) {
        listener = scrollListener;
    }

    public boolean hasScrollView() {
        return scrollView != null;
    }

    public void onScrollViewAdded(ScrollView scrollView) {
        this.scrollView = scrollView;
        listener.onScrollViewAdded(this.scrollView);
    }

    public void onScrollViewRemoved() {
        this.scrollView = null;
    }

    public boolean didInterceptTouchEvent(MotionEvent ev) {
            return listener.onTouch(ev);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        scrollView.onTouchEvent(event);
        return this.listener.onTouch(event);
    }
}
