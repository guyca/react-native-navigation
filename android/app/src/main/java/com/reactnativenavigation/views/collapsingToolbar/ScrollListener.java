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
    public boolean onTouch(ContentView contentView, MotionEvent event) {
        updateInitialTouchY(event);
        return handleTouch(scrollView, contentView, event);
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

    private boolean handleTouch(ScrollView scrollView, ContentView contentView, MotionEvent event) {
//        Log.i("collapse", "handleTouch");
        if (scrollDirectionComputer == null) {
            scrollDirectionComputer = new ScrollDirection(scrollView);
        }

        int currentTopBarTranslation = (int) topBar.getTranslationY();

        int minTranslation = -topBar.getTitleBar().getHeight();
        int maxTranslation = 0;

//        ScrollDirection.Direction direction = scrollDirectionComputer.getScrollDirection();
        ScrollDirection.Direction direction = getScrollDirection(event);

        hasReachedMinimum =
                direction == ScrollDirection.Direction.Up && currentTopBarTranslation <= minTranslation;
        hasReachedMaximum =
                direction == ScrollDirection.Direction.Down && currentTopBarTranslation >= maxTranslation;

        delta = (int) (event.getRawY() - yTouchDown + previousDelta);
        Log.v("Delta", "delta: " + delta);

        if (isDragging) {
            setTopBarTranslationY();
            setContentViewTranslationY();
            return true;
        } else {
            return false;
        }
    }

    private void setTopBarTranslationY() {
        topBar.setTranslationY(delta);
    }

    private void setContentViewTranslationY() {
        contentView.setTranslationY(delta);
    }

    private ScrollDirection.Direction  getScrollDirection(MotionEvent event) {
        if (event.getY() == yTouchDown) {
            return ScrollDirection.Direction.None;
        }
        return event.getY() < yTouchDown ? ScrollDirection.Direction.Down : ScrollDirection.Direction.Up;
    }

    private boolean hasReachedCollapseBounds(ScrollDirection.Direction direction, boolean reachedMinimum, boolean reachedMaximum) {
        if (reachedMinimum && delta != 0) {
            Log.i("collapse", "hasReachedMinimum");
            return true;
        }
        if (reachedMaximum && delta != 0) {
            Log.w("collapse", "hasReachedMaximum");
            return true;
        }
        if (direction == ScrollDirection.Direction.None) {
            Log.v("collapse", "direction == ScrollDirection.Direction.None");
            return true;
        }
        return false;
//        return direction == ScrollDirection.Direction.None || reachedMinimum || reachedMaximum;
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
}
