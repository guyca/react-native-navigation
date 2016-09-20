package com.reactnativenavigation.views.collapsingToolbar;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.facebook.react.uimanager.JSTouchDispatcher;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.reactnativenavigation.NavigationApplication;

public class ScrollViewDelegate implements View.OnTouchListener {
    private final EventDispatcher eventDispatcher;

    public interface OnScrollListener {
        boolean onTouch(MotionEvent event);

        void onScrollViewAdded(ScrollView scrollView);

        boolean didInterceptTouchEvent(MotionEvent ev);
    }

    private JSTouchDispatcher jsTouchDispatcher;
    private OnScrollListener listener;
    private Boolean didInterceptLastTouchEvent = null;
    private boolean touchDownReported = false;

    public ScrollViewDelegate(JSTouchDispatcher jsTouchDispatcher) {
        this.jsTouchDispatcher = jsTouchDispatcher;
        eventDispatcher = NavigationApplication.instance.getReactGateway().getReactContext().
                getNativeModule(UIManagerModule.class).getEventDispatcher();
    }

    public void onViewAdded(View child) {
        if (child instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) child;
            scrollView.setScrollbarFadingEnabled(false);
            listener.onScrollViewAdded(scrollView);
        }
    }

    public void setListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public boolean didInterceptTouchEvent(MotionEvent ev) {
        boolean didInterceptTouchEvent = listener.didInterceptTouchEvent(ev);
        if (!touchDownReported && wasLastTouchIntercepted()) {
            sendDownEventBeforePassingTouchEventsToScrollView(ev);
            return false;
        }
        didInterceptLastTouchEvent = didInterceptTouchEvent;
        return true;
    }

    private void sendDownEventBeforePassingTouchEventsToScrollView(MotionEvent ev) {
        MotionEvent simulatedDownEvent = MotionEvent.obtain(ev);
        simulatedDownEvent.setAction(MotionEvent.ACTION_DOWN);
        jsTouchDispatcher.handleTouchEvent(simulatedDownEvent, eventDispatcher);
        touchDownReported = true;
    }

    private boolean wasLastTouchIntercepted() {
        return didInterceptLastTouchEvent != null && didInterceptLastTouchEvent;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return this.listener.onTouch(event);
    }
}
