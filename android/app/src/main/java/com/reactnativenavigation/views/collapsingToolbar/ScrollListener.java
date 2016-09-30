package com.reactnativenavigation.views.collapsingToolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.CollapsingTopBar;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.TopBar;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private static final String TAG = "ScrollListener";
    private TopBar topBar;
    private ContentView contentView;
    private float yTouchDown = -1;
    private float previousY = -1;
    private ScrollView scrollView;
    private boolean isExpended;
    private boolean isCollapsed = true;
    private boolean canCollapse = true;
    private boolean canExpend = false;
    private float delta;
    private float previousTouchDelta = 0;
    private int finalCollapsedTranslation;
    private final int finalExpendedTranslation;
    private int scrollAmountOnInitialTouch;

    public ScrollListener(final TopBar topBar, ContentView contentView) {
        this.topBar = topBar;
        this.contentView = contentView;
        finalExpendedTranslation = 0;
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                finalCollapsedTranslation = -topBar.getHeight();
                if (topBar instanceof CollapsingTopBar) {
                    finalCollapsedTranslation +=
                            ((CollapsingTopBar) topBar).getCollapsingToolBar().getCollapsedTopBarHeight();
                }
            }
        });
    }

    @Override
    public void onScrollViewAdded(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        updateInitialTouchY(event);
        return handleTouch(event);
    }

    private void updateInitialTouchY(MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
            saveInitialTouchY(event);
        } else if (MotionEvent.ACTION_UP == event.getActionMasked()) {
            clearInitialTouchY();
        }
    }

    private void saveInitialTouchY(MotionEvent event) {
        yTouchDown = event.getRawY();
        previousY = yTouchDown;
        scrollAmountOnInitialTouch = scrollView.getScrollY();
        Log.i("saveInitialTouchY", "Saving initial touch: " + yTouchDown + " scroll: " + scrollView.getScrollY());
    }

    private void clearInitialTouchY() {
        Log.d(TAG, "Clearing initial touch");
        yTouchDown = -1;
        previousTouchDelta = delta;
        delta = 0;
        previousY = -1;
    }

    private boolean handleTouch(MotionEvent event) {
        final float y = event.getRawY();
        if (shouldTranslateTopBarAndScrollView(y, event)) {
            translateTopBarAndScrollView(y);
            return true;
        } else {
            if (!isMoveEvent(event)) {
                Log.v(TAG, "Ignoring touch event");
                return false;
            }

//            if (isCollapsing) {
//                MotionEvent evDown = MotionEvent.obtain(event);
//                evDown.setAction(MotionEvent.ACTION_DOWN);
//                scrollView.onTouchEvent(evDown);
//            }

            Log.e(TAG, "Not handling scroll");
            return false;
        }
    }

    private void translateTopBarAndScrollView(float y) {
        delta = y - yTouchDown + previousTouchDelta - scrollAmountOnInitialTouch;
        translateViews();
        previousY = y;
    }

    private void translateViews() {
        float translation = delta;
        if (translation < finalCollapsedTranslation) {
            translation = finalCollapsedTranslation;
        }
        if (translation > finalExpendedTranslation) {
            translation = finalExpendedTranslation;
        }
        topBar.collapseBy(translation);
        contentView.setTranslationY(translation);
    }

    private void checkCollapseLimits() {
        int currentTopBarTranslation = (int) topBar.getTranslationY();
        isExpended = isExpended(currentTopBarTranslation, finalExpendedTranslation);
        isCollapsed = isCollapsed(currentTopBarTranslation, finalCollapsedTranslation);
        canCollapse = calculateCanCollapse(currentTopBarTranslation, finalExpendedTranslation, finalCollapsedTranslation);
        canExpend = calculateCanExpend(currentTopBarTranslation, finalExpendedTranslation, finalCollapsedTranslation);
        Log.v("checkCollapseLimits", "canExpend: " + canExpend + " canCollapse: " + canCollapse);
    }

    private boolean calculateCanCollapse(int currentTopBarTranslation, int finalExpendedTranslation, int finalCollapsedTranslation) {
        return currentTopBarTranslation > finalCollapsedTranslation && currentTopBarTranslation <= finalExpendedTranslation;
    }

    private boolean calculateCanExpend(int currentTopBarTranslation, int finalExpendedTranslation, int finalCollapsedTranslation) {
        return currentTopBarTranslation >= finalCollapsedTranslation && currentTopBarTranslation < finalExpendedTranslation;
    }

    private boolean shouldTranslateTopBarAndScrollView(float y, MotionEvent event) {
        checkCollapseLimits();
        ScrollDirection.Direction direction = getScrollDirection(y);
        Log.i("shouldTranslate", "isExpended: " + isExpended + " isCollapsed: " + isCollapsed + " direction: " + direction);
        return isMoveEvent(event) &&
               scrollView.getScrollY() == 0 &&
               (isNotCollapsedOrExpended() ||
                isExpendedAndScrollingUp(direction) ||
                isCollapsedAndScrollingDown(direction));
    }

    private boolean isMoveEvent(MotionEvent event) {
        return event.getActionMasked() == MotionEvent.ACTION_MOVE;
    }

    private boolean isCollapsedAndScrollingDown(ScrollDirection.Direction direction) {
        return isCollapsed && direction == ScrollDirection.Direction.Down;
    }

    private boolean isExpendedAndScrollingUp(ScrollDirection.Direction direction) {
        return isExpended && direction == ScrollDirection.Direction.Up;
    }

    private boolean isNotCollapsedOrExpended() {
        Log.d("NotCollapsedOrExpended", "canExpend: " + canExpend + " canCollapse: " + canCollapse);
        return canExpend && canCollapse;
    }

    private boolean isCollapsed(int currentTopBarTranslation, int finalCollapsedTranslation) {
        return currentTopBarTranslation == finalCollapsedTranslation;
    }

    private boolean isExpended(int currentTopBarTranslation, int finalExpendedTranslation) {
        return currentTopBarTranslation == finalExpendedTranslation;
    }

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == (previousY == -1 ? yTouchDown : previousY)) {
            return ScrollDirection.Direction.None;
        }
        ScrollDirection.Direction ret = y < previousY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
        Log.w(TAG, "direction: " + ret);
        return ret;
    }
}
