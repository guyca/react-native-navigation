package com.reactnativenavigation.screens;

import android.support.v7.app.AppCompatActivity;

import com.reactnativenavigation.params.ScreenParams;
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
        contentView.setViewMeasurer(new CollapsingContentViewMeasurer(contentView, topBar));
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        if (screenParams.styleParams.drawScreenBelowTopBar) {
            params.addRule(BELOW, topBar.getId());
        }
        addView(contentView, params);
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
