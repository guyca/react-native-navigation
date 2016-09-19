package com.reactnativenavigation.views;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.view.ViewGroup;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.params.TitleBarButtonParams;
import com.reactnativenavigation.params.TitleBarLeftButtonParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingToolBar;

import java.util.List;

public class TopBar extends AppBarLayout {

    private CollapsingToolBar collapsingToolBar;
    private TitleBar titleBar;
    private TopTabs topTabs;

    public TopBar(Context context) {
        super(context);
        setFitsSystemWindows(true);
        setId(ViewUtils.generateViewId());
    }

    public void addTitleBarAndSetButtons(List<TitleBarButtonParams> rightButtons,
                                         TitleBarLeftButtonParams leftButton,
                                         LeftButtonOnClickListener leftButtonOnClickListener,
                                         String navigatorEventId, boolean overrideBackPressInJs) {
        createCollapsingTopBar();
        titleBar = new TitleBar(getContext());
        collapsingToolBar.addView(titleBar, 1);
        titleBar.setRightButtons(rightButtons, navigatorEventId);
        titleBar.setLeftButton(leftButton, leftButtonOnClickListener, navigatorEventId, overrideBackPressInJs);
    }

    private void createCollapsingTopBar() {
        collapsingToolBar = new CollapsingToolBar(getContext());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) CollapsingToolBar.MAX_HEIGHT);
        addView(collapsingToolBar, lp);
    }

    public void setTitle(String title) {
        titleBar.setTitle(title);
    }

    public void setSubtitle(String subtitle) {
        titleBar.setSubtitle(subtitle);
    }

    public void setStyle(StyleParams styleParams) {
        if (styleParams.topBarColor.hasColor()) {
            setBackgroundColor(styleParams.topBarColor.getColor());
        }
        setVisibility(styleParams.topBarHidden ? GONE : VISIBLE);
        titleBar.setStyle(styleParams);
        setTopTabsStyle(styleParams);
    }

    public void setTitleBarRightButtons(String navigatorEventId, List<TitleBarButtonParams> titleBarButtons) {
        titleBar.setRightButtons(titleBarButtons, navigatorEventId);
    }

    public TabLayout initTabs() {
        topTabs = new TopTabs(getContext());
        addView(topTabs);
        return topTabs;
    }

    public void setTitleBarLeftButton(String navigatorEventId,
                                      LeftButtonOnClickListener leftButtonOnClickListener,
                                      TitleBarLeftButtonParams titleBarLeftButtonParams,
                                      boolean overrideBackPressInJs) {
        titleBar.setLeftButton(titleBarLeftButtonParams, leftButtonOnClickListener, navigatorEventId,
                overrideBackPressInJs);
    }

    private void setTopTabsStyle(StyleParams style) {
        if (topTabs == null) {
            return;
        }

        topTabs.setTopTabsTextColor(style);
        topTabs.setSelectedTabIndicatorStyle(style);
    }

    public TitleBar getTitleBar() {
        return titleBar;
    }

    public CollapsingToolBar getCollapsingToolBar() {
        return collapsingToolBar;
    }
}
