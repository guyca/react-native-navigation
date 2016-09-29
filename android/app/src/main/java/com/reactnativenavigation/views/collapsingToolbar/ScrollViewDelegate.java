package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class ScrollViewDelegate implements View.OnTouchListener {
    private ScrollView scrollView;

    interface OnScrollListener {
        boolean onTouch(MotionEvent event);

        void onScrollViewAdded(ScrollView scrollView);
    }

    private OnScrollListener listener;
    private Boolean didInterceptLastTouchEvent = null;

    public void onViewAdded(ScrollView scrollView) {
        this.scrollView = scrollView;
        this.scrollView.setScrollbarFadingEnabled(false);
        listener.onScrollViewAdded(this.scrollView);
    }

    public void setListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public boolean didInterceptTouchEvent(MotionEvent ev) {
            return listener.onTouch(ev);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!didInterceptLastTouchEvent) {
            scrollView.onTouchEvent(event);
        }
        return this.listener.onTouch(event);
    }
}
