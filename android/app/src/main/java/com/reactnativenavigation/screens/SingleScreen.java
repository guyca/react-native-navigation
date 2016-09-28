package com.reactnativenavigation.screens;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.CollapsingTopBar;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingContentViewMeasurer;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SingleScreen extends Screen {
    private ContentView contentView;

    public SingleScreen(AppCompatActivity activity, ScreenParams screenParams,
                        LeftButtonOnClickListener titleBarBarBackButtonListener) {
        super(activity, screenParams, titleBarBarBackButtonListener);
    }

    @Override
    protected void createContent() {
        contentView = new ContentView(getContext(), screenParams.screenId, screenParams.navigationParams, topBar);
        if (screenParams.hasCollapsingTopBar()) {
            contentView.setViewMeasurer(new CollapsingContentViewMeasurer(contentView, (CollapsingTopBar) topBar));
        }
        addView(contentView, createLayoutParams());
    }

    @NonNull
    private LayoutParams createLayoutParams() {
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        if (screenParams.styleParams.drawScreenBelowTopBar) {
            params.addRule(BELOW, topBar.getId());
        }
        return params;
    }

    @Override
    public void unmountReactView() {
        contentView.unmountReactView();
    }

    @Override
    public void setOnDisplayListener(OnDisplayListener onContentViewDisplayedListener) {
        contentView.setOnDisplayListener(onContentViewDisplayedListener);
    }
}
