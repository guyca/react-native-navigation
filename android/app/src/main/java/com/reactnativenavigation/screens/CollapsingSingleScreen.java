package com.reactnativenavigation.screens;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;

import com.reactnativenavigation.params.ScreenParams;
import com.reactnativenavigation.views.ContentView;
import com.reactnativenavigation.views.LeftButtonOnClickListener;
import com.reactnativenavigation.views.collapsingToolbar.CollapseCalculator;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingContentViewMeasurer;
import com.reactnativenavigation.views.collapsingToolbar.CollapsingTopBar;
import com.reactnativenavigation.views.collapsingToolbar.OnScrollViewAddedListener;
import com.reactnativenavigation.views.collapsingToolbar.ScrollListener;
import com.reactnativenavigation.views.collapsingToolbar.TitleBarCollapseCalculator;

public class CollapsingSingleScreen extends SingleScreen {
    public CollapsingSingleScreen(AppCompatActivity activity, ScreenParams screenParams, LeftButtonOnClickListener titleBarBarBackButtonListener) {
        super(activity, screenParams, titleBarBarBackButtonListener);
    }

    @Override
    protected void createTopBar() {
        final CollapsingTopBar topBar = new CollapsingTopBar(getContext(), styleParams.collapsingTopBarParams);
        topBar.setScrollListener(getScrollListener(topBar));
        this.topBar = topBar;
    }

    @Override
    protected void createContent() {
        contentView = new ContentView(getContext(), screenParams.screenId, screenParams.navigationParams);
        if (screenParams.styleParams.collapsingTopBarParams.hasImage()) {
            contentView.setViewMeasurer(new CollapsingContentViewMeasurer((CollapsingTopBar) topBar, this));
        }
        setupCollapseDetection((CollapsingTopBar) topBar);
        addView(contentView, createLayoutParams());
    }

    private void setupCollapseDetection(final CollapsingTopBar topBar) {
        contentView.setupScrollDetection(getScrollListener(topBar));
        contentView.setOnScrollViewAddedListener(new OnScrollViewAddedListener() {
            @Override
            public void onScrollViewAdded(ScrollView scrollView) {
                topBar.onScrollViewAdded(scrollView);
            }
        });
    }

    @NonNull
    private ScrollListener getScrollListener(final CollapsingTopBar topBar) {
        return new ScrollListener(getCollapseCalculator(topBar),
                new ScrollListener.OnScrollListener() {
                    @Override
                    public void onScroll(float amount) {
                        if (screenParams.styleParams.collapsingTopBarParams.hasImage()) {
                            contentView.collapse(amount);
                        }
                        topBar.collapse(amount);
                    }
                }
        );
    }

    @NonNull
    private CollapseCalculator getCollapseCalculator(CollapsingTopBar topBar) {
        if (screenParams.styleParams.collapsingTopBarParams.hasImage()) {
            return new CollapseCalculator(topBar);
        } else {
            return new TitleBarCollapseCalculator(topBar);
        }
    }
}
