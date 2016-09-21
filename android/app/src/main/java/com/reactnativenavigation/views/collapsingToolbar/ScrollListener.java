package com.reactnativenavigation.views.collapsingToolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;
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
    private boolean isExpended;
    private boolean isCollapsed = true;
    private boolean canCollapse = true;
    protected boolean canExpend = false;
    private boolean isCollapsing = false;
    private boolean isDragging = false;
    private int delta;
    private int previousDelta = 0;
    private int finalCollapsedTranslation;
    private final int finalExpendedTranslation;

    public ScrollListener(final TopBar topBar, ContentView contentView) {
        this.topBar = topBar;
        this.contentView = contentView;
        collapsingToolBar = topBar.getCollapsingToolBar();
        finalExpendedTranslation = 0;
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                finalCollapsedTranslation = -(topBar.getHeight() - topBar.getCollapsedTopBarHeight());
            }
        });
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
            previousY = yTouchDown;
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
        Log.v(TAG, "handleTouch");
        if (scrollDirectionComputer == null) {
            scrollDirectionComputer = new ScrollDirection(scrollView);
        }

        delta = (int) (y - yTouchDown + previousDelta);
//        delta = (int) (y - yTouchDown);
        checkCollapseLimits();
        ScrollDirection.Direction direction = getScrollDirection(y);
        Log.v("Delta", "delta: " + delta);
        Log.w(TAG, "direction: " + direction);

        if (shouldTranslateTopBarAndScrollView(direction)) {
            setTopBarTranslationY();
            setContentViewTranslationY();
            previousY = y;
            isCollapsing = true;
            return true;
        } else {
            isCollapsing = false;
//            previousY = y;
            Log.e(TAG, "Not handling scroll");
            return false;
        }
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

    private boolean shouldTranslateTopBarAndScrollView(ScrollDirection.Direction direction) {
        Log.i("shouldTranslate", "isExpended: " + isExpended + " isCollapsed: " + isCollapsed + " direction: " + direction);
        return isDragging &&
               (isNotCollapsedOrExpended() || isExpendedAndScrollingUp(direction) || isCollapsedAndScrollingDown(direction));
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

    private void setTopBarTranslationY() {
        float translation = delta;
        if (translation < finalCollapsedTranslation) {
            translation = finalCollapsedTranslation;
        }
        if (translation > finalExpendedTranslation) {
            translation = finalExpendedTranslation;
        }
        topBar.setTranslationY(translation);
    }

    private void setContentViewTranslationY() {
        float translation = delta;
        if (translation < finalCollapsedTranslation) {
            translation = finalCollapsedTranslation;
        }
        if (translation > finalExpendedTranslation) {
            translation = finalExpendedTranslation;
        }
        contentView.setTranslationY(translation);
    }

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == getPreviousY()) {
            return ScrollDirection.Direction.None;
        }
        ScrollDirection.Direction ret = y < previousY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
        return ret;
    }

    private float getPreviousY() {
        return previousY == -1 ? yTouchDown : previousY;
    }

    @Override
    public boolean didInterceptTouchEvent(MotionEvent ev) {
        return isCollapsing && !isExpended && !isCollapsed;
    }
}
