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
    private boolean hasReachedMaximum;
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
        return handleTouch(scrollView, event);
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
        isDragging = false;
    }

    private boolean handleTouch(ScrollView scrollView, MotionEvent event) {
        if (scrollDirectionComputer == null) {
            scrollDirectionComputer = new ScrollDirection(scrollView);
        }

        delta = (int) (event.getRawY() - yTouchDown + previousDelta);
        ScrollDirection.Direction direction = getScrollDirection(event);
        Log.v("Delta", "delta: " + delta);

        int currentTopBarTranslation = delta;
        int minTranslation = -topBar.getTitleBar().getHeight();
        int maxTranslation = 0;
        Log.w(TAG, "direction: " + direction);
        hasReachedMinimum = calculateHasReachedMinimum(currentTopBarTranslation, minTranslation, direction);
        hasReachedMaximum = calculateHasReachedMaximum(currentTopBarTranslation, maxTranslation, direction);

        previousY = event.getRawY();

        if (isDragging && !hasReachedMinimum && !hasReachedMaximum && direction != ScrollDirection.Direction.None) {
            setTopBarTranslationY();
            setContentViewTranslationY();
            return true;
        } else {
            Log.e(TAG, "Not handling scroll");
            return true;
        }
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

    private ScrollDirection.Direction  getScrollDirection(MotionEvent event) {
        if (event.getRawY() == (previousY == -1 ? yTouchDown : previousY)) {
            return ScrollDirection.Direction.None;
        }
        return event.getRawY() < previousY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
    }

    @Override
    public boolean didInterceptTouchEvent(MotionEvent ev) {
        Log.i("didIntercept", "isCollapsing: " + isCollapsing + " hasReachedMinimum: " + hasReachedMinimum);
        if (isCollapsing && !hasReachedMinimum) {
            Log.d("GUYGUY", "true");
            return true;
        }
        Log.w("GUYGUY", "false");
        return false;
    }

    @Override
    public boolean hasReachedMinimum() {
        return hasReachedMinimum;
    }
}
