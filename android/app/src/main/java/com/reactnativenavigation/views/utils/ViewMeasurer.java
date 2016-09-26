package com.reactnativenavigation.views.utils;

import android.view.View;

import static android.view.View.*;

public class ViewMeasurer {

    private View view;

    public ViewMeasurer(View view) {
        this.view = view;
    }

    public int getMeasuredHeight(int heightMeasuerSpec) {
        return MeasureSpec.getSize(heightMeasuerSpec);
    }

    public int getMeasuredWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }
}
