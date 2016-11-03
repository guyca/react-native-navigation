package com.reactnativenavigation.views;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.RelativeLayout;

import com.reactnativenavigation.params.SearchViewParams;

public class SearchResultsView extends RelativeLayout {
    private static final String TAG = "SearchResultsView";
    private ContentView contentView;

    public SearchResultsView(RelativeLayout parent, SearchViewParams params) {
        super(parent.getContext());
        createLayout(parent, params);
        setOnClickListener(createDismissListener());
    }

    private OnClickListener createDismissListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                contentView.unmountReactView();
                ((ViewManager) SearchResultsView.this.getParent()).removeView(SearchResultsView.this);
            }
        };
    }

    private void createLayout(RelativeLayout parent, SearchViewParams params) {
        setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final int width = (int) (parent.getWidth() * 0.8f);
        final int height = 1500;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
        lp.topMargin = 230;
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentView = new ContentView(parent.getContext(), params.screenId, params.navigationParams);
        contentView.setLayoutParams(lp);
        addView(contentView);
    }


}
