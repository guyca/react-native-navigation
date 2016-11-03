package com.reactnativenavigation.params.parsers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.reactnativenavigation.params.SearchParams;
import com.reactnativenavigation.react.ImageLoader;

public class SearchParamsParser extends Parser {

    public static SearchParams parse(Bundle params) {
        return new SearchParams(getIcon(params));
    }

    private static Drawable getIcon(Bundle params) {
        if (params.get("icon") != null) {
            return ImageLoader.loadImage(params.getString("icon"));
        }
        return null;
    }
}
