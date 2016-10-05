package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.views.CollapsingTopBar;
import com.reactnativenavigation.views.ContentView;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private CollapsingTopBar topBar;
    private ContentView contentView;
    private CollapseDeltaCalculator boundsCalculator;

    public ScrollListener(final CollapsingTopBar topBar, ContentView contentView) {
        this.contentView = contentView;
        this.topBar = topBar;
        boundsCalculator = new CollapseDeltaCalculator(topBar);
    }

    @Override
    public void onScrollViewAdded(ScrollView scrollView) {
        boundsCalculator.setScrollView(scrollView);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return handleTouch(event);
    }

    private boolean handleTouch(MotionEvent event) {
        Float delta = boundsCalculator.calculateDelta(event);
        if (delta != null) {
            translateViews(delta);
            return true;
        }
        return false;
    }

    private void translateViews(float delta) {
        topBar.collapseBy(delta);
        contentView.collapseBy(delta);
    }
}
