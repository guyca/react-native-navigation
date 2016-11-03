package com.reactnativenavigation.params;

import android.graphics.drawable.Drawable;

public class SearchParams {
    public static final String KEY_SEARCH = "search";

    public SearchParams(Drawable icon) {
        this.icon = icon;
    }

    public Drawable icon;
}
