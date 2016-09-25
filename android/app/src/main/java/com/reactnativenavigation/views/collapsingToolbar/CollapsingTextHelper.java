package com.reactnativenavigation.views.collapsingToolbar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class CollapsingTextHelper {
    private static final boolean USE_SCALING_TEXTURE = Build.VERSION.SDK_INT < 18;

    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;
    static {
        DEBUG_DRAW_PAINT = DEBUG_DRAW ? new Paint() : null;
        if (DEBUG_DRAW_PAINT != null) {
            DEBUG_DRAW_PAINT.setAntiAlias(true);
            DEBUG_DRAW_PAINT.setColor(Color.MAGENTA);
        }
    }

    private boolean mDrawTitle;
    private float mExpandedFraction;
    private boolean mUseTexture;
    private Bitmap mExpandedTitleTexture;
    private Paint mTexturePaint;
    private float mTextureAscent;
    private float mTextureDescent;

    private float scale;
    private float currentTextSize;

    private String text;
    private final TextPaint textPaint;
    private Interpolator titleSizeInterpolator;
    private final View collapsingTopBar;

    public CollapsingTextHelper(View collapsingTopBar) {
        this.collapsingTopBar = collapsingTopBar;
        textPaint = createTextPaint();
        titleSizeInterpolator = new DecelerateInterpolator();
    }

    private TextPaint createTextPaint() {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        return textPaint;
    }

    public void draw(Canvas canvas) {
        canvas.drawText(text, 0, 0, textPaint);
    }

    public void setText(String text) {
        this.text = text;
    }


    //    private void createAnimators() {
//        xAnimator = PropertyValuesHolder.ofFloat(View.SCALE_X, title.getScaleX());
//        yAnimator = PropertyValuesHolder.ofFloat(View.SCALE_Y, title.getScaleY());
//        animator = ObjectAnimator.ofPropertyValuesHolder(title, xAnimator, yAnimator);
//        animator.setDuration(1);
//    }
//
//    public void update(float fraction) {
//        animator.setFloatValues(interpolate(fraction), interpolate(fraction));
//        animator.start();
//    }
//
//    private float interpolate(float f) {
//        aFloat = (minScale * (1.0f - f)) + (maxScale * f);
//        Log.i("interpolate", "" + aFloat);
//        return aFloat;
//    }
}
