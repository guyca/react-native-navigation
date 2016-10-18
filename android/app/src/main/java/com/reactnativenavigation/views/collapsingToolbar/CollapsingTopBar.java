package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;

import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.views.TitleBar;
import com.reactnativenavigation.views.TopBar;

public class CollapsingTopBar extends TopBar {
    private CollapsingToolBar collapsingToolBar;

    public CollapsingTopBar(Context context, CollapsingTopBarParams params) {
        super(context);
        createCollapsingTopBar(params);
    }

    private void createCollapsingTopBar(CollapsingTopBarParams params) {
        collapsingToolBar = new CollapsingToolBar(getContext(), params);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, (int) CollapsingToolBar.MAX_HEIGHT);
        addView(collapsingToolBar, lp);
    }

    @Override
    protected TitleBar createTitleBar() {
        return new CollapsingTitleBar(getContext(), collapsingToolBar.getCollapsedTopBarHeight());
    }

    @Override
    protected void addTitleBar() {
        collapsingToolBar.addView(titleBar, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public int getTitleBarHeight() {
        return titleBar.getMeasuredHeight();
    }

    public CollapsingToolBar getCollapsingToolBar() {
        return collapsingToolBar;
    }

    public void collapseBy(float delta) {
        float translation = CollapseDeltaCalculator.correctTranslationValue(getTranslationY() + delta);
        ((CollapsingTitleBar) titleBar).collapseBy(translation);
        collapsingToolBar.collapseBy(translation);
    }
}
