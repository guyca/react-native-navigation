package com.reactnativenavigation.views.collapsingToolbar;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;

import static com.facebook.react.common.ReactConstants.TAG;

public class DeltaCalculator {

    private float delta;
    private MotionEvent previousTouchEvent;
    private float touchDownY = -1;
    private float initialMoveY = -1;
    private float previousCollapseY = -1;
    private boolean isExpended;
    private boolean isCollapsed = true;
    private boolean canCollapse = true;
    private boolean canExpend = false;
    private CollapsingView view;
    protected ScrollView scrollView;
    private static float finalCollapsedTranslation;

    public DeltaCalculator(final CollapsingView collapsingView) {
        this.view = collapsingView;
        ViewUtils.runOnPreDraw(view.asView(), new Runnable() {
            @Override
            public void run() {
                finalCollapsedTranslation = view.getFinalCollapseValue();
            }
        });
    }

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

    void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Nullable
    Float calculate(MotionEvent event) {
        updateInitialTouchY(event);
        if (!isMoveEvent(event)) {
            Log.v(TAG, "Ignoring touch event");
            return null;
        }

        if (shouldTranslateTopBarAndScrollView(event)) {
            return calculateDelta(event);
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
        float currentTopBarTranslation = view.getCurrentCollapseValue();
        float finalExpendedTranslation = 0;
        isExpended = isExpended(currentTopBarTranslation, finalExpendedTranslation);
        isCollapsed = isCollapsed(currentTopBarTranslation, finalCollapsedTranslation);
        canCollapse = calculateCanCollapse(currentTopBarTranslation, finalExpendedTranslation, finalCollapsedTranslation);
        canExpend = calculateCanExpend(currentTopBarTranslation, finalExpendedTranslation, finalCollapsedTranslation);
    }

    private boolean calculateCanCollapse(float currentTopBarTranslation, float finalExpendedTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation > finalCollapsedTranslation &&
               currentTopBarTranslation <= finalExpendedTranslation;
    }

    private boolean calculateCanExpend(float currentTopBarTranslation, float finalExpendedTranslation, float finalCollapsedTranslation) {
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

    private boolean isCollapsed(float currentTopBarTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation == finalCollapsedTranslation;
    }

    private boolean isExpended(float currentTopBarTranslation, float finalExpendedTranslation) {
        return currentTopBarTranslation == finalExpendedTranslation;
    }

    @CheckResult
    private float calculateDelta(MotionEvent event) {
        float y = event.getRawY();
        if (initialMoveY == -1) {
            if (previousTouchEvent == null) {
                previousTouchEvent = MotionEvent.obtain(event);
            }
            initialMoveY = previousTouchEvent.getRawY();
        }
        if (previousCollapseY == -1) {
            previousCollapseY = y;
        }

        delta = y - previousCollapseY;
        previousCollapseY = y;
        previousTouchEvent = MotionEvent.obtain(event);
        return delta;
    }

    private void updateInitialTouchY(MotionEvent event) {
        if (isTouchDown(previousTouchEvent) && isMoveEvent(event)) {
            saveInitialTouchY(previousTouchEvent);
        } else if (isTouchUp(event) && isMoveEvent(previousTouchEvent)) {
            clearInitialTouchY();
        }
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

    private void saveInitialTouchY(MotionEvent event) {
        touchDownY = event.getRawY();
        previousCollapseY = touchDownY;
    }

    private void clearInitialTouchY() {
        touchDownY = -1;
        initialMoveY = -1;
        previousCollapseY = -1;
        delta = 0;
    }



}
