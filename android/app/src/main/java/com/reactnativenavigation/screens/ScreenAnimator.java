package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.view.View;

import com.reactnativenavigation.NavigationApplication;

import javax.annotation.Nullable;

class ScreenAnimator {
    private Screen screen;

    ScreenAnimator(Screen screen) {
        this.screen = screen;
    }

    public void show(boolean animate, final Runnable onAnimationEnd) {
        if (animate) {
            createShowAnimator(onAnimationEnd).start();
        } else {
            screen.setVisibility(View.VISIBLE);
            NavigationApplication.instance.runOnMainThread(onAnimationEnd, 200);
        }
    }

    public void show(boolean animate) {
        if (animate) {
            createShowAnimator(null).start();
        } else {
            screen.setVisibility(View.VISIBLE);
        }
    }

    public void hide(boolean animate, Runnable onAnimationEnd) {
        if (animate) {
            createHideAnimator(onAnimationEnd).start();
        } else {
            screen.setVisibility(View.INVISIBLE);
            onAnimationEnd.run();
        }
    }

    private Animator createShowAnimator(final @Nullable Runnable onAnimationEnd) {
        return new AnimatorFactory(screen).createShowAnimator(onAnimationEnd);
    }

    private Animator createHideAnimator(final Runnable onAnimationEnd) {
        return new AnimatorFactory(screen).createHideAnimator(onAnimationEnd);
    }
}
