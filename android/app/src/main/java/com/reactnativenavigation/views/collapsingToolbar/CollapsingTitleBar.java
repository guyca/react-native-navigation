package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.params.StyleParams;
import com.reactnativenavigation.views.TitleBar;

public class CollapsingTitleBar extends TitleBar implements View.OnTouchListener {
    private CollapsingTextView title;
    private int collapsedHeight;
    private final ScrollListener scrollListener;

    public CollapsingTitleBar(Context context, int collapsedHeight, ScrollListener scrollListener) {
        super(context);
        this.collapsedHeight = collapsedHeight;
        this.scrollListener = scrollListener;
        addCollapsingTitle();
        setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return scrollListener.onTouch(event);
    }
}
