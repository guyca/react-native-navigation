package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private CollapseCalculator collapseCalculator;
    private OnScrollListener scrollListener;

    public interface  OnScrollListener {
        void onScroll(float amount);
    }

    public ScrollListener(CollapsingView collapsingView, OnScrollListener scrollListener) {
        this.collapseCalculator = new CollapseCalculator(collapsingView);
        this.scrollListener = scrollListener;
    }

    @Override
    public void onScrollViewAdded(ScrollView scrollView) {
        collapseCalculator.setScrollView(scrollView);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return handleTouch(event);
    }

    private boolean handleTouch(MotionEvent event) {
        Float scrollAmount = collapseCalculator.calculate(event);
        if (scrollAmount != null) {
            scrollListener.onScroll(scrollAmount);
            return true;
        }
        return false;
    }
}
