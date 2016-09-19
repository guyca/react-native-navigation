package com.reactnativenavigation.views.collapsingToolbar;

import android.widget.ScrollView;

import com.reactnativenavigation.views.TopBar;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private TopBar topBar;

    public ScrollListener(TopBar topBar) {
        this.topBar = topBar;
    }

    private ScrollDirection scrollDirectionComputer;

    @Override
    public void onScroll(ScrollView scrollView) {
        if (scrollDirectionComputer == null) {
            scrollDirectionComputer = new ScrollDirection(scrollView);
        }

        int currentTopBarTranslation = (int) topBar.getTranslationY();
        int delta = scrollDirectionComputer.getScrollDelta();

        int minTranslation = -topBar.getTitleBar().getHeight();
        int maxTranslation = 0;

        ScrollDirection.Direction direction = scrollDirectionComputer.getScrollDirection();

        boolean reachedMinimum = direction == ScrollDirection.Direction.Up && currentTopBarTranslation <= minTranslation;
        boolean reachedMaximum = direction == ScrollDirection.Direction.Down && currentTopBarTranslation >= maxTranslation;

        if (direction == ScrollDirection.Direction.None || reachedMinimum || reachedMaximum) {
            if (reachedMinimum) {
                topBar.animate().translationY(minTranslation);
            }
            if (reachedMaximum) {
                topBar.animate().translationY(maxTranslation);
            }
        } else {

            int target = currentTopBarTranslation - delta;
            int bound = Math.min(Math.max(minTranslation, target), maxTranslation);

            topBar.setTranslationY(bound);
        }
    }
}
