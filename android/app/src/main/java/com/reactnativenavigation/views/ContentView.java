package com.reactnativenavigation.views;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.ReactRootView;
import com.facebook.react.uimanager.JSTouchDispatcher;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.screens.SingleScreen;
import com.reactnativenavigation.utils.ReflectionUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollViewDelegate;

public class ContentView extends ReactRootView {
    private static final String TAG = "ContentView";
    private final String screenId;
    private final NavigationParams navigationParams;

    boolean isContentVisible = false;
    private SingleScreen.OnDisplayListener onDisplayListener;
    private ScrollViewDelegate scrollViewDelegate;
    private JSTouchDispatcher jsTouchDispatcher;

    public void setOnDisplayListener(SingleScreen.OnDisplayListener onDisplayListener) {
        this.onDisplayListener = onDisplayListener;
    }

    public ContentView(Context context, String screenId, NavigationParams navigationParams) {
        this(context, screenId, navigationParams, null);
    }

    public ContentView(Context context, String screenId, NavigationParams navigationParams, TopBar topBar) {
        super(context);
        this.screenId = screenId;
        this.navigationParams = navigationParams;
        attachToJS();
        jsTouchDispatcher = getJsTouchDispatcher();
        scrollViewDelegate = new ScrollViewDelegate(jsTouchDispatcher);
        scrollViewDelegate.setListener(new ScrollListener(topBar, this));
    }

    private JSTouchDispatcher getJsTouchDispatcher() {
        return (JSTouchDispatcher) ReflectionUtils.getDeclaredField(this, "mJSTouchDispatcher");
    }

    private void attachToJS() {
        startReactApplication(NavigationApplication.instance.getReactGateway().getReactInstanceManager(), screenId,
                navigationParams.toBundle());
    }

    public String getNavigatorEventId() {
        return navigationParams.navigatorEventId;
    }

    public void unmountReactView() {
        unmountReactApplication();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent() called with: " + "ev = [" + ev + "]");
        return super.onInterceptTouchEvent(ev);
//        return scrollViewDelegate.didInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        Log.d(TAG, "onTouchEvent() called with: " + "ev = [" + ev + "]");
//        return scrollViewDelegate.onTouch(this, ev) || super.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.v(TAG, "dispatchTouchEvent. " + ev.getRawY());
        if (scrollViewDelegate.didInterceptTouchEvent(ev)) {
            scrollViewDelegate.onTouch(this, ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onViewAdded(final View child) {
        super.onViewAdded(child);
        detectContentViewVisible(child);
        scrollViewDelegate.onViewAdded(child);
    }

    private void detectContentViewVisible(View child) {
        if (onDisplayListener != null) {
            ViewUtils.runOnPreDraw(child, new Runnable() {
                @Override
                public void run() {
                    if (!isContentVisible) {
                        isContentVisible = true;
                        onDisplayListener.onDisplay();
                        onDisplayListener = null;
                    }
                }
            });
        }
    }
}
