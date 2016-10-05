package com.reactnativenavigation.views.collapsingToolbar;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.CollapsingTopBar;
import com.reactnativenavigation.views.ContentView;

public class ScrollListener implements ScrollViewDelegate.OnScrollListener {
    private static final String TAG = "ScrollListener";
    private CollapsingTopBar topBar;
    private ContentView contentView;
    private float touchDownY = -1;
    private float previousMoveY = -1;
    private float initialMoveY = -1;
    private float previousCollapseY = -1;
    private ScrollView scrollView;
    private boolean isExpended;
    private boolean isCollapsed = true;
    private boolean canCollapse = true;
    private boolean canExpend = false;
    private float delta;
    private float previousTouchDelta = 0;
    private MotionEvent previousTouchEvent;
    private static int finalCollapsedTranslation;
    private int scrollAmountOnInitialTouch;
    private float exTotalDelta;

    public ScrollListener(final CollapsingTopBar topBar, ContentView contentView) {
        this.topBar = topBar;
        this.contentView = contentView;
        ViewUtils.runOnPreDraw(topBar, new Runnable() {
            @Override
            public void run() {
                finalCollapsedTranslation = -topBar.getHeight();
                finalCollapsedTranslation += topBar.getCollapsingToolBar().getCollapsedTopBarHeight();
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
        if (isTouchDown(previousTouchEvent) && isMoveEvent(event)) {
            saveInitialTouchY(previousTouchEvent);
        } else if (isTouchUp(event) && isMoveEvent(previousTouchEvent)) {
            clearInitialTouchY();
        }
    }

    private void saveInitialTouchY(MotionEvent event) {
        touchDownY = event.getRawY();
        previousCollapseY = touchDownY;
        scrollAmountOnInitialTouch = scrollView.getScrollY();
        Log.i("saveInitialTouchY", "Saving initial touch: " + touchDownY + " scroll: " + scrollView.getScrollY());
    }

    private void clearInitialTouchY() {
        Log.d("clearInitialTouchY", "Clearing initial touch " + delta);
        touchDownY = -1;
        initialMoveY = -1;
        previousTouchDelta = delta; // This get's nullified after touch twice!! Save it when moving
        delta = 0;
        previousCollapseY = -1;

        previousTouchDelta = exTotalDelta;
        exTotalDelta = 0;
    }

    private boolean handleTouch(MotionEvent event) {
        if (!isMoveEvent(event)) {
            Log.v(TAG, "Ignoring touch event " + getHumanReadableMotionEventName(event));
            return false;
        }

        final float y = event.getRawY();
        if (shouldTranslateTopBarAndScrollView(y, event)) {
            translateTopBarAndScrollView(y);
            previousTouchEvent = MotionEvent.obtain(event);
            return true;
        } else {
            Log.e(TAG, "Not handling scroll");
            previousCollapseY = -1;
            previousTouchEvent = MotionEvent.obtain(event);
            return false;
        }
    }

    private boolean shouldTranslateTopBarAndScrollView(float y, MotionEvent event) {
        checkCollapseLimits();
        ScrollDirection.Direction direction = getScrollDirection(y);
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

    private boolean isMoveEvent(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_MOVE;
    }

    private boolean isTouchDown(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_DOWN;
    }

    private boolean isTouchUp(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_UP;
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

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == (previousCollapseY == -1 ? touchDownY : previousCollapseY)) {
            return ScrollDirection.Direction.None;
        }
//        Log.v("getScrollDirection", y + " < " + previousCollapseY + " ? " + (y < previousCollapseY));
//        return y < previousCollapseY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;

        if (previousTouchEvent == null) {
            return ScrollDirection.Direction.None;
        }
        float rawY = previousTouchEvent.getRawY();
        Log.v("getScrollDirection", y + " < " + rawY + " ? " + (y < previousCollapseY));
        return y < rawY ? ScrollDirection.Direction.Up : ScrollDirection.Direction.Down;
    }

    private void translateTopBarAndScrollView(float y) {
        if (initialMoveY == -1) {
            initialMoveY = previousTouchEvent.getRawY();
//            Log.w("translation", "Setting initialMoveY " + initialMoveY);
        }
        if (previousCollapseY == -1) {
            previousCollapseY = y;
        }

        delta = y - touchDownY + previousTouchDelta - scrollAmountOnInitialTouch;
        exTotalDelta = calculateExDelta(y);
//        Log.v("TEST", "y - touchDownY: " + y + " - " + touchDownY + "=" + (y - touchDownY) +
//                      " previousTouchDelta: " + previousTouchDelta +
//                      " scrollAmountOnInitialTouch: " + scrollAmountOnInitialTouch +
//                      " delta: " + delta
//        );
//        Log.v("deltaa", "" + delta + " exDelta: " + exTotalDelta + "PTD: " + previousTouchDelta);
        translateViews(calculateDelta(y));
        previousCollapseY = y;
    }

    private float calculateDelta(float y) {
        float delta = y - previousCollapseY;
//        Log.i("calculateDelta", y + " - " + previousCollapseY + " = " + (y - previousCollapseY));
        return delta;
    }

    private float calculateExDelta(float y) {
        return y - initialMoveY - scrollAmountOnInitialTouch + previousTouchDelta;
    }

    private void translateViews(float delta) {
//        float translation = exTotalDelta;
//        translation = correctTranslationValue(translation);
//        Log.i("translation", "" + translation + " delta: " + delta);
        topBar.collapseBy(delta);
        contentView.collapseBy(delta);
    }

    public static float correctTranslationValue(float translation) {
        final float expendedTranslation = 0;
        if (translation < finalCollapsedTranslation) {
            translation = finalCollapsedTranslation;
        }
        if (translation > expendedTranslation) {
//            Log.e("translation", "corrected " + translation + " to 0");
            translation = expendedTranslation;
        }
        return translation;
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
}
