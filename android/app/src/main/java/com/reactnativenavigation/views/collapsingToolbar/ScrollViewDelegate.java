package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.reactnativenavigation.views.ContentView;

public class ScrollViewDelegate implements View.OnTouchListener {
    public interface OnScrollListener {
        boolean onTouch(ContentView contentView, MotionEvent event);

        void onScrollViewAdded(ScrollView scrollView);

        boolean didInterceptTouchEvent(MotionEvent ev);
    }

    private ScrollView scrollView;
    private OnScrollListener listener;

    public ScrollViewDelegate() {
    }

    public void onViewAdded(View child) {
        if (child instanceof ScrollView) {
            scrollView = (ScrollView) child;
            listener.onScrollViewAdded(scrollView);
        }
    }

    public void setListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public boolean didInterceptTouchEvent(MotionEvent ev) {
        return listener.didInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View contentView, MotionEvent event) {
        return this.listener.onTouch((ContentView) contentView, event);
    }
}
