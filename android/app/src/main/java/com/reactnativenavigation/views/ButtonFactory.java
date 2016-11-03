package com.reactnativenavigation.views;

import android.view.Menu;
import android.view.View;

import com.reactnativenavigation.params.SearchParams;
import com.reactnativenavigation.params.TitleBarButtonParams;

public class ButtonFactory {
    public static TitleBarButton create(Menu menu, View parent, TitleBarButtonParams params, String navigatorEventId) {
        switch (params.eventId) {
            case SearchParams.KEY_SEARCH:
                return new TitleBarSearchButton(menu, parent, params, navigatorEventId);
            default:
                return new TitleBarButton(menu, parent, params, navigatorEventId);
        }
    }
}
