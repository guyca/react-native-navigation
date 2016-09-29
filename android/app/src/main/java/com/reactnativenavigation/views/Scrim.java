package com.reactnativenavigation.views;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.reactnativenavigation.params.StyleParams;

import static com.reactnativenavigation.views.Scrim.State.Invisible;
import static com.reactnativenavigation.views.Scrim.State.Visible;


public class Scrim extends View {
    private final float threshold;
    private final static int ANIMATION_DURATION = 600;
    private final Interpolator interpolator;

    enum State {Visible, Invisible};
    private State state = Invisible;

    public Scrim(Context context, StyleParams.Color color, float threshold) {
        super(context);
        this.threshold = threshold;
        setBackgroundColor(color.getColor());
        setAlpha(0);
        interpolator = new DecelerateInterpolator();
    }

    public void handleCollapse(float delta) {
        if (shouldShowScrim(delta)) {
            showScrim();
        } else if (shouldHideScrim(delta)) {
            hideScrim();
        }

    }

    private boolean shouldShowScrim(float delta) {
        return Math.abs(delta) >= threshold && state == Invisible;
    }

    private boolean shouldHideScrim(float delta) {
        return Math.abs(delta) < threshold && state == Visible;
    }

    private void showScrim() {
        state = Visible;
        animate()
                .alpha(1)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }

    private void hideScrim() {
        state = Invisible;
        animate()
                .alpha(0)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(interpolator);
    }
}
