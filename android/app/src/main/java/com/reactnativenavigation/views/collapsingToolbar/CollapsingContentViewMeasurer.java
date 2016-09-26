package com.reactnativenavigation.views.collapsingToolbar;

import android.view.View;

import com.reactnativenavigation.views.TopBar;
import com.reactnativenavigation.views.utils.ViewMeasurer;

public class CollapsingContentViewMeasurer extends ViewMeasurer {
    final float titleBarHeight;
    public CollapsingContentViewMeasurer(View view, TopBar topBar) {
        super(view);
        titleBarHeight = topBar.getTitleBarHeight();
    }

    @Override
    public int getMeasuredHeight(int heightMeasureSpec) {
        return (int) (super.getMeasuredHeight(heightMeasureSpec) + CollapsingToolBar.MAX_HEIGHT - titleBarHeight);
    }
}
