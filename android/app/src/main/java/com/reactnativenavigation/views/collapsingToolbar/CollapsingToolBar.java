package com.reactnativenavigation.views.collapsingToolbar;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reactnativenavigation.R;
import com.reactnativenavigation.utils.ViewUtils;

public class CollapsingToolBar extends CollapsingToolbarLayout {
    public static final float MAX_HEIGHT = ViewUtils.convertDpToPixel(256);

    private ImageView image;


    public CollapsingToolBar(Context context) {
        super(context);
        setFitsSystemWindows(true);
        createBackDropImage();
    }

    private void createBackDropImage() {
        image = new ImageView(getContext());
        image.setImageResource(R.drawable.gyro_header);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(image, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
