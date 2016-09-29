package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.reactnativenavigation.R;
import com.reactnativenavigation.params.CollapsingTopBarParams;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.Scrim;

public class CollapsingToolBar extends FrameLayout {
    public static final float MAX_HEIGHT = ViewUtils.convertDpToPixel(256);
    private final CollapsingTopBarParams params;

    private SimpleDraweeView backdrop;
    private Scrim scrim;
    private int topBarHeight = -1;
    //    private CollapsingTextHelper collapsingTextHelper;

    public CollapsingToolBar(Context context, CollapsingTopBarParams params) {
        super(context);
        this.params = params;
        setFitsSystemWindows(true);
        createBackDropImage();
        createScrim();
    }

    private void createBackDropImage() {
        backdrop = new SimpleDraweeView(getContext());
        setImageSource();
        backdrop.setScaleType(ImageView.ScaleType.CENTER_CROP);
        backdrop.setFitsSystemWindows(true);
        addView(backdrop, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void createScrim() {
        scrim = new Scrim(getContext(), params.scrimColor, MAX_HEIGHT / 2);
        addView(scrim);
    }

    private void setImageSource() {
        if (params.imageUri != null) {
            backdrop.setImageURI(params.imageUri);
        }
    }

    public int getCollapsedTopBarHeight() {
        if (topBarHeight > -1) {
            return topBarHeight;
        }

        int[] attrs = new int[] {R.attr.actionBarSize};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        topBarHeight = ta.getDimensionPixelSize(0, -1);
        ta.recycle();
        return topBarHeight;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("onLayout", "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Let the collapsing text helper draw its text
//        collapsingTextHelper.draw(canvas);
    }

    public void collapseBy(float delta) {
        ((View) getParent()).setTranslationY(delta);
        scrim.handleCollapse(delta);
    }

    //    public void setTitle(String title) {
//        collapsingTextHelper.setText(title);
//    }
}
