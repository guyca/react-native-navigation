package com.reactnativenavigation.views.collapsingToolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.TopBar;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private static final String TAG = "ScrollListener";
    private TopBar topBar;
    private ContentView contentView;
    CollapsingToolBar collapsingToolBar;
    private float yTouchDown = -1;
    private float previousY = -1;
    private ScrollView scrollView;
    private boolean hasReachedMinimum;
    private boolean hasReachedMaximum = true;
    private boolean isCollapsing = true;
    private boolean isDragging = false;
    private int delta;
    private int previousDelta = 0;

    public ScrollListener(TopBar topBar, ContentView contentView) {
        this.topBar = topBar;
        this.contentView = contentView;
        collapsingToolBar = topBar.getCollapsingToolBar();
    }

    private ScrollDirection scrollDirectionComputer;

    @Override
    public boolean onTouch(MotionEvent event) {
        updateInitialTouchY(event);
        return handleTouch(scrollView, event.getRawY());
    }

    @Override
    public void onScrollViewAdded(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    private void updateInitialTouchY(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
            saveInitialTouchYIfNeeded(event);
        } else if (MotionEvent.ACTION_UP == event.getActionMasked()) {
            clearInitialTouchY();
        }
    }

    private void saveInitialTouchYIfNeeded(MotionEvent event) {
        if (yTouchDown < 0) {
            yTouchDown = event.getRawY();
            isDragging = true;
            Log.i(TAG, "Saving initial touch: " + yTouchDown);
        }
    }

    private void clearInitialTouchY() {
        Log.d(TAG, "Clearing initial touch");
        yTouchDown = -1;
        previousDelta = delta;
        delta = 0;
        previousY = -1;
        isDragging = false;
    }

    private boolean handleTouch(ScrollView scrollView, float y) {
        if (scrollDirectionComputer == null) {
            scrollDirectionComputer = new ScrollDirection(scrollView);
        }

        delta = (int) (y - yTouchDown + previousDelta);
        ScrollDirection.Direction direction = getScrollDirection(y);
        Log.v("Delta", "delta: " + delta);

        int currentTopBarTranslation = delta;
        int minTranslation = -topBar.getTitleBar().getHeight();
        int maxTranslation = 0;
        Log.w(TAG, "direction: " + direction);
        hasReachedMinimum = calculateHasReachedMinimum(currentTopBarTranslation, minTranslation, direction);
        hasReachedMaximum = calculateHasReachedMaximum(currentTopBarTranslation, maxTranslation, direction);


        if (canCollapse(direction)) {
            setTopBarTranslationY();
            setContentViewTranslationY();
            previousY = y;
            return true;
        } else {
            Log.e(TAG, "Not handling scroll");
            return true;
        }
    }

    private boolean canCollapse(ScrollDirection.Direction direction) {
        return isDragging &&
               (((hasReachedMinimum && direction == ScrollDirection.Direction.Down) ||
               (hasReachedMaximum && direction == ScrollDirection.Direction.Up)) || (!hasReachedMaximum && !hasReachedMinimum));
    }

    private boolean calculateHasReachedMaximum(int currentTopBarTranslation, int maxTranslation, ScrollDirection.Direction direction) {
        return currentTopBarTranslation >= maxTranslation;
    }

    private boolean calculateHasReachedMinimum(int currentTopBarTranslation, int minTranslation, ScrollDirection.Direction direction) {
        return currentTopBarTranslation <= minTranslation;
    }

    private void setTopBarTranslationY() {
        topBar.setTranslationY(delta);
    }

    private void setContentViewTranslationY() {
        contentView.setTranslationY(delta);
    }

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == getPreviousY()) {
            return ScrollDirection.Direction.None;
        }
        return y < previousY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
    }

    private float getPreviousY() {
        return previousY == -1 ? yTouchDown : previousY;
    }

    @Override
    public boolean didInterceptTouchEvent(MotionEvent ev) {
        return isCollapsing && !hasReachedMinimum && !hasReachedMaximum;
    }
}
