package com.reactnativenavigation.views;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.R;
import com.reactnativenavigation.params.TitleBarButtonParams;

public class TitleBarSearchButton extends TitleBarButton implements SearchView.OnQueryTextListener {

    public TitleBarSearchButton(Menu menu, View parent, TitleBarButtonParams buttonParams, @Nullable String navigatorEventId) {
        super(menu, parent, buttonParams, navigatorEventId);
    }

    MenuItem addToMenu(int index) {
        ((Activity) parent.getContext()).getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem item = menu.findItem(R.id.toolbar_action_search);
        if (buttonParams.icon != null) {
            item.setIcon(buttonParams.icon);
        }
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        EditText searchEditText = (EditText) findChildByClass(searchView, EditText.class);
        searchEditText.setTextColor(Color.WHITE);
        return item;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Never really called, so just for clarity.
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        WritableMap arguments = Arguments.createMap();
        arguments.putString("query", query);
        NavigationApplication.instance.sendNavigatorEvent("search", navigatorEventId + "screen", arguments);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // TODO: dont do
    private View findChildByClass(ViewGroup root, Class clazz) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View view = root.getChildAt(i);
            if (clazz.isAssignableFrom(view.getClass())) {
                return view;
            }

            if (view instanceof ViewGroup) {
                view = findChildByClass((ViewGroup) view, clazz);
                if (view != null && clazz.isAssignableFrom(view.getClass())) {
                    return view;
                }
            }
        }

        return null;
    }
}
