package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.views.TitleBar;

public class CollapsingTitleBar extends TitleBar {
    private CollapsingTextView title;
    private int collapsedHeight;

    public CollapsingTitleBar(Context context, int collapsedHeight) {
        super(context);
        this.collapsedHeight = collapsedHeight;
        addCollapsingTitle();
    }

    private void addCollapsingTitle() {
        title = new CollapsingTextView(getContext(), collapsedHeight);
        addView(title);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title.setText((String) title);
    }

    @Override
    protected void setTitleTextColor(StyleParams params) {
        title.setTextColor(params);
    }

    @Override
    protected void setSubtitleTextColor(StyleParams params) {
    }

    public void collapseBy(float translation) {
        title.setTranslationY(0);
        setTranslationY(-translation);
        title.collapseBy(translation);
    }
}
