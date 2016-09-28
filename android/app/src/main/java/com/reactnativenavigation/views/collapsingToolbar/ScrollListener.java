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
    protected boolean canExpend = false;
    private boolean isCollapsing = false;
    private boolean isDragging = false;
    private int delta;
    private int previousDelta = 0;
    private int finalCollapsedTranslation;
    private final int finalExpendedTranslation;
    private ScrollDirection scrollDirectionComputer;

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
        return handleTouch(scrollView, event.getRawY());
    }

    private void updateInitialTouchY(MotionEvent event) {
//        Log.v(TAG, "updateInitialTouchY: " + getHumanReadableMotionEventName(event));
        if (MotionEvent.ACTION_DOWN == event.getActionMasked()) {
            saveInitialTouchYIfNeeded(event);
        } else if (MotionEvent.ACTION_UP == event.getActionMasked()) {
            clearInitialTouchY();
        }
    }

    private String getHumanReadableMotionEventName(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "ACTION_POINTER_DOWN";
            case MotionEvent.ACTION_POINTER_UP:
                return "ACTION_POINTER_UP";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            default:
                throw new RuntimeException("Unhandled event type " + event.getActionMasked());
        }
    }

    private void saveInitialTouchYIfNeeded(MotionEvent event) {
//        if (yTouchDown < 0) {
            yTouchDown = event.getRawY();
            previousY = yTouchDown;
            isDragging = true;
            Log.i(TAG, "Saving initial touch: " + yTouchDown);
//        }
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

        if (shouldTranslateTopBarAndScrollView(y)) {
            calculateDelta(y);
            setTranslation();
            previousY = y;
            isCollapsing = true;
        } else {
            isCollapsing = false;
            Log.e(TAG, "Not handling scroll");
        }
        return isCollapsing;
    }

    private void calculateDelta(float y) {
        delta = (int) (y - yTouchDown + previousDelta);
        Log.v("Delta", "delta: " + delta);
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

    private boolean shouldTranslateTopBarAndScrollView(float y) {
        checkCollapseLimits();
        ScrollDirection.Direction direction = getScrollDirection(y);
        Log.i("shouldTranslate", "isExpended: " + isExpended + " isCollapsed: " + isCollapsed + " direction: " + direction);
        return isDragging &&
               (isNotCollapsedOrExpended() ||
                isExpendedAndScrollingUp(direction) ||
                isCollapsedAndScrollingDown(direction));
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

    private void setTranslation() {
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

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == (previousY == -1 ? yTouchDown : previousY)) {
            return ScrollDirection.Direction.None;
        }
        ScrollDirection.Direction ret = y < previousY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
        Log.w(TAG, "direction: " + ret);
        return ret;
    }

    @Override
    public boolean didInterceptTouchEvent(MotionEvent ev) {
        return isCollapsing && !isExpended && !isCollapsed;
    }
}
