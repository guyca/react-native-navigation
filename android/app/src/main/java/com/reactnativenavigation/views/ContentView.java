package com.reactnativenavigation.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.ReactRootView;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.NavigationParams;
import com.reactnativenavigation.screens.SingleScreen;
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
        scrollViewDelegate = new ScrollViewDelegate();
        scrollViewDelegate.setListener(new ScrollListener(topBar, this));
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
//        Log.d(TAG, "onInterceptTouchEvent() called with: " + "ev = [" + ev + "]");
        return scrollViewDelegate.didInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
//        Log.d(TAG, "onTouchEvent() called with: " + "ev = [" + ev + "]");
        return scrollViewDelegate.onTouch(this, ev);
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
