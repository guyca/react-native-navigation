package com.reactnativenavigation.views;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        return item;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Never really called, so just for clarity.
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("ASDASD", "onQueryTextSubmit "+query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("ASDASD", "onQueryTextChange "+newText);
        return false;
    }
}
