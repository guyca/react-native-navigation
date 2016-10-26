package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private DeltaCalculator deltaCalculator;
    private OnScrollListener scrollListener;

    public interface  OnScrollListener {
        void onScroll(float delta);
    }

    public ScrollListener(DeltaCalculator deltaCalculator, OnScrollListener scrollListener) {
        this.deltaCalculator = deltaCalculator;
        this.scrollListener = scrollListener;
    }

    @Override
    public void onScrollViewAdded(ScrollView scrollView) {
        deltaCalculator.setScrollView(scrollView);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return handleTouch(event);
    }

    private boolean handleTouch(MotionEvent event) {
        Float delta = deltaCalculator.calculate(event);
        if (delta != null) {
            scrollListener.onScroll(delta);
            return true;
        }
        return false;
    }
}
