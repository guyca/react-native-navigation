package com.reactnativenavigation.screens;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingContentViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBar;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SingleScreen extends Screen {
    private ContentView contentView;

    public SingleScreen(AppCompatActivity activity, ScreenParams screenParams,
                        LeftButtonOnClickListener titleBarBarBackButtonListener) {
        super(activity, screenParams, titleBarBarBackButtonListener);
    }

    @Override
    protected void createTopBar() {
        if (screenParams.hasCollapsingTopBar()) {
            final CollapsingTopBar topBar = new CollapsingTopBar(getContext(), styleParams.collapsingTopBarParams);
            topBar.setScrollListener(new ScrollListener(topBar,
                    new ScrollListener.OnScrollListener() {
                        @Override
                        public void onScroll(float delta) {
                            contentView.collapseBy(delta);
                            topBar.collapseBy(delta);
                        }
                    }
            ));
            this.topBar = topBar;
        } else {
            super.createTopBar();
        }
    }

    @Override
    protected void createContent() {
        contentView = new ContentView(getContext(), screenParams.screenId, screenParams.navigationParams);
        if (screenParams.hasCollapsingTopBar()) {
            contentView.setViewMeasurer(new CollapsingContentViewMeasurer((CollapsingTopBar) topBar));
            setupScrollDetection((CollapsingTopBar) topBar);
        }
        addView(contentView, createLayoutParams());
    }

    private void setupScrollDetection(final CollapsingTopBar topBar) {
        contentView.setupScrollDetection(new ScrollListener(topBar,
                new ScrollListener.OnScrollListener() {
                    @Override
                    public void onScroll(float delta) {
                        contentView.collapseBy(delta);
                        topBar.collapseBy(delta);
                    }
                }
        ));
        contentView.setOnScrollViewAddedListener(new OnScrollViewAddedListener() {
            @Override
            public void onScrollViewAdded(ScrollView scrollView) {
                topBar.onScrollViewAdded(scrollView);
            }
        });
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
