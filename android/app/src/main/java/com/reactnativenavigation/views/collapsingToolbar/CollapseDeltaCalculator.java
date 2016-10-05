package com.reactnativenavigation.views.collapsingToolbar;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.CollapsingTopBar;

import static com.facebook.react.common.ReactConstants.TAG;

public class CollapseDeltaCalculator {
    private float delta;
    private static int finalCollapsedTranslation;
    private MotionEvent previousTouchEvent;
    private float touchDownY = -1;
    private float initialMoveY = -1;
    private float previousCollapseY = -1;
    private ScrollView scrollView;
    private boolean isExpended;
    private boolean isCollapsed = true;
    private boolean canCollapse = true;
    private boolean canExpend = false;
    private CollapsingTopBar topBar;

    public static float correctTranslationValue(float translation) {
        final float expendedTranslation = 0;
        if (translation < finalCollapsedTranslation) {
            translation = finalCollapsedTranslation;
        }
        if (translation > expendedTranslation) {
            translation = expendedTranslation;
        }
        return translation;
    }

    CollapseDeltaCalculator(final CollapsingTopBar topBar) {
        this.topBar = topBar;
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                finalCollapsedTranslation = -topBar.getHeight();
                finalCollapsedTranslation += topBar.getCollapsingToolBar().getCollapsedTopBarHeight();
            }
        });
    }

    void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Nullable
    Float calculateDelta(MotionEvent event) {
        updateInitialTouchY(event);
        if (!isMoveEvent(event)) {
            Log.v(TAG, "Ignoring touch event");
            return null;
        }

        if (shouldTranslateTopBarAndScrollView(event)) {
            final float delta = translateTopBarAndScrollView(event.getRawY());
            previousTouchEvent = MotionEvent.obtain(event);
            return delta;
        } else {
            Log.e(TAG, "Not handling scroll");
            previousCollapseY = -1;
            previousTouchEvent = MotionEvent.obtain(event);
            return null;
        }
    }

    private boolean shouldTranslateTopBarAndScrollView(MotionEvent event) {
        checkCollapseLimits();
        ScrollDirection.Direction direction = getScrollDirection(event.getRawY());
        Log.i("shouldTranslate", "isExpended: " + isExpended +
                                 " isCollapsed: " + isCollapsed +
                                 " direction: " + direction +
                                 " canCollapse: " + canCollapse +
                                 " canExpend: " + canExpend);
        return isMoveEvent(event) &&
               (isNotCollapsedOrExpended() ||
                (canCollapse && isExpendedAndScrollingUp(direction)) ||
                (canExpend && isCollapsedAndScrollingDown(direction)));
    }

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == (previousCollapseY == -1 ? touchDownY : previousCollapseY)) {
            return ScrollDirection.Direction.None;
        }

        if (previousTouchEvent == null) {
            return ScrollDirection.Direction.None;
        }
        float rawY = previousTouchEvent.getRawY();
        Log.v("getScrollDirection", y + " < " + rawY + " ? " + (y < previousCollapseY));
        return y < rawY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
    }

    private void checkCollapseLimits() {
        int currentTopBarTranslation = (int) topBar.getTranslationY();
        int finalExpendedTranslation = 0;
        isExpended = isExpended(currentTopBarTranslation, finalExpendedTranslation);
        isCollapsed = isCollapsed(currentTopBarTranslation, finalCollapsedTranslation);
        canCollapse = calculateCanCollapse(currentTopBarTranslation, finalExpendedTranslation, finalCollapsedTranslation);
        canExpend = calculateCanExpend(currentTopBarTranslation, finalExpendedTranslation, finalCollapsedTranslation);
        Log.v("checkCollapseLimits", "canExpend: " + canExpend + " canCollapse: " + canCollapse);
    }

    private boolean calculateCanCollapse(int currentTopBarTranslation, int finalExpendedTranslation, int finalCollapsedTranslation) {
        return currentTopBarTranslation > finalCollapsedTranslation &&
               currentTopBarTranslation <= finalExpendedTranslation;
    }

    private boolean calculateCanExpend(int currentTopBarTranslation, int finalExpendedTranslation, int finalCollapsedTranslation) {
        return currentTopBarTranslation >= finalCollapsedTranslation &&
               currentTopBarTranslation < finalExpendedTranslation &&
               scrollView.getScrollY() == 0;
    }

    private boolean isCollapsedAndScrollingDown(ScrollDirection.Direction direction) {
        return isCollapsed && direction == ScrollDirection.Direction.Down;
    }

    private boolean isExpendedAndScrollingUp(ScrollDirection.Direction direction) {
        return isExpended && direction == ScrollDirection.Direction.Up;
    }

    private  boolean isNotCollapsedOrExpended() {
        return canExpend && canCollapse;
    }

    private boolean isCollapsed(int currentTopBarTranslation, int finalCollapsedTranslation) {
        return currentTopBarTranslation == finalCollapsedTranslation;
    }

    private boolean isExpended(int currentTopBarTranslation, int finalExpendedTranslation) {
        return currentTopBarTranslation == finalExpendedTranslation;
    }

    @CheckResult
    private float translateTopBarAndScrollView(float y) {
        if (initialMoveY == -1) {
            initialMoveY = previousTouchEvent.getRawY();
        }
        if (previousCollapseY == -1) {
            previousCollapseY = y;
        }

        delta = calculateDelta(y);
        previousCollapseY = y;
        return delta;
    }

    private float calculateDelta(float y) {
        return y - previousCollapseY;
    }

    private void updateInitialTouchY(MotionEvent event) {
        if (isTouchDown(previousTouchEvent) && isMoveEvent(event)) {
            saveInitialTouchY(previousTouchEvent);
        } else if (isTouchUp(event) && isMoveEvent(previousTouchEvent)) {
            clearInitialTouchY();
        }
    }

    private void saveInitialTouchY(MotionEvent event) {
        touchDownY = event.getRawY();
        previousCollapseY = touchDownY;
        Log.i("saveInitialTouchY", "Saving initial touch: " + touchDownY + " scroll: " + scrollView.getScrollY());
    }

    private void clearInitialTouchY() {
        Log.d("clearInitialTouchY", "Clearing initial touch " + delta);
        touchDownY = -1;
        initialMoveY = -1;
        previousCollapseY = -1;
        delta = 0;
    }

    private boolean isMoveEvent(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_MOVE;
    }

    private boolean isTouchDown(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_DOWN;
    }

    private boolean isTouchUp(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_UP;
    }
}
