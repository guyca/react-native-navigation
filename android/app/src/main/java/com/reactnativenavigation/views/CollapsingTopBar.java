package com.reactnativenavigation.views;

import android.content.Context;
import android.view.ViewGroup;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingToolBar;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;

public class CollapsingTopBar extends TopBar {
    private CollapsingToolBar collapsingToolBar;

    public CollapsingTopBar(Context context, CollapsingTopBarParams params) {
        super(context);
        createCollapsingTopBar(params);
    }

    private void createCollapsingTopBar(CollapsingTopBarParams params) {
        collapsingToolBar = new CollapsingToolBar(getContext(), params);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) CollapsingToolBar.MAX_HEIGHT);
        addView(collapsingToolBar, lp);
    }

    @Override
    protected void addTitleBar() {
        collapsingToolBar.addView(titleBar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public int getTitleBarHeight() {
        return titleBar.getMeasuredHeight();
    }

    public CollapsingToolBar getCollapsingToolBar() {
        return collapsingToolBar;
    }

    public void collapseBy(float delta) {
        float translation = ScrollListener.correctTranslationValue(getTranslationY() + delta);
        titleBar.collapseBy(translation);
        collapsingToolBar.collapseBy(translation);
    }
}
