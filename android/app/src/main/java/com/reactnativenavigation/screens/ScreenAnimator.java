package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.sharedElementTransition.SharedElementsAnimator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

class ScreenAnimator {
    private static final int DURATION = 250;
    private static final int ALPHA_HIDE_DURATION = 100;
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR_1x5 = new DecelerateInterpolator(1.5f);
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final float ENTER_EXIT_ALPHA = 0.95f;
    private static final float ENTER_EXIT_SCALE = 0.95f;
    private final float translationY;
    private final float translationX;
    private Screen screen;

    ScreenAnimator(Screen screen) {
        this.screen = screen;
        translationY = 0.05f * ViewUtils.getWindowHeight(screen.activity);
        translationX = ViewUtils.getWindowWidth(screen.activity);
    }

    void showInitialScreen(boolean animate, final Runnable onAnimationEnd) {
        show(animate, onAnimationEnd);
    }

    public void show(boolean animate, final Runnable onAnimationEnd) {
        if (animate) {
            createShowAnimator(onAnimationEnd).start();
        } else {
            screen.setVisibility(View.VISIBLE);
            NavigationApplication.instance.runOnMainThread(onAnimationEnd, DURATION);
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
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, 0, 1);

        AnimatorSet set = new AnimatorSet();
        switch (String.valueOf(this.screen.screenParams.animationType)) {
            case "fade": {
                alpha.setDuration(DURATION);
                alpha.setInterpolator(DECELERATE_INTERPOLATOR);
                set.play(alpha);
                break;
            }
            case "slide-horizontal": {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(screen, View.TRANSLATION_X, this.translationX, 0);
                translationX.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
                translationX.setDuration(DURATION);
                set.play(translationX);
                break;
            }
            default: {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY, 0);
                set.setInterpolator(DECELERATE_INTERPOLATOR_1x5);
                set.setDuration(DURATION);
                set.playTogether(translationY, alpha);
                break;
            }
        }

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                screen.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onAnimationEnd != null) {
                    onAnimationEnd.run();
                }
            }
        });
        return set;
    }

    private Animator createHideAnimator(final Runnable onAnimationEnd) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, 0);

        AnimatorSet set = new AnimatorSet();
        switch (String.valueOf(this.screen.screenParams.animationType)) {
            case "fade": {
                alpha.setDuration(DURATION);
                alpha.setInterpolator(DECELERATE_INTERPOLATOR);
                set.play(alpha);
                break;
            }
            case "slide-horizontal": {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(screen, View.TRANSLATION_X, this.translationX);
                translationX.setInterpolator(ACCELERATE_INTERPOLATOR);
                translationX.setDuration(DURATION);
                set.play(translationX);
                break;
            }
            default: {
                ObjectAnimator translationY = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, this.translationY);
                translationY.setDuration(DURATION);
                alpha.setDuration(ALPHA_HIDE_DURATION);
                set.setInterpolator(DECELERATE_INTERPOLATOR);
                set.playTogether(translationY, alpha);
                break;
            }
        }

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        return set;
    }

    void showWithSharedElementsTransitions(Runnable onAnimationEnd) {
        hideContentViewAndTopBar();
        screen.setVisibility(View.VISIBLE);
        new SharedElementsAnimator(this.screen.sharedElements).show(new Runnable() {
            @Override
            public void run() {
                animateContentViewAndTopBar(1, 280);
            }
        }, onAnimationEnd);
    }

    private void hideContentViewAndTopBar() {
        if (screen.screenParams.animateScreenTransitions) {
            screen.getContentView().setAlpha(0);
        }
        screen.getTopBar().setAlpha(0);
    }

    void hideWithSharedElementsTransition(Runnable onAnimationEnd) {
        new SharedElementsAnimator(screen.sharedElements).hide(new Runnable() {
            @Override
            public void run() {
                animateContentViewAndTopBar(0, 200);
            }
        }, onAnimationEnd);
    }

    private void animateContentViewAndTopBar(int alpha, int duration) {
        List<Animator> animators = new ArrayList<>();
        if (screen.screenParams.animateScreenTransitions) {
            animators.add(ObjectAnimator.ofFloat(screen.getContentView(), View.ALPHA, alpha));
        }
        animators.add(ObjectAnimator.ofFloat(screen.getTopBar(), View.ALPHA, alpha));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setDuration(duration);
        set.start();
    }

    void animateEnter() {
        List<Animator> animators = new ArrayList<>();

        animators.add(ObjectAnimator.ofFloat(screen, View.ALPHA, ENTER_EXIT_ALPHA, 1));
        animators.add(ObjectAnimator.ofFloat(screen, View.SCALE_X, ENTER_EXIT_SCALE, 1));
        animators.add(ObjectAnimator.ofFloat(screen, View.SCALE_Y, ENTER_EXIT_SCALE, 1));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setDuration(DURATION);
        set.start();
    }

    void animateExit() {
        List<Animator> animators = new ArrayList<>();

        animators.add(ObjectAnimator.ofFloat(screen, View.ALPHA, ENTER_EXIT_ALPHA));
        animators.add(ObjectAnimator.ofFloat(screen, View.SCALE_X, ENTER_EXIT_SCALE));
        animators.add(ObjectAnimator.ofFloat(screen, View.SCALE_Y, ENTER_EXIT_SCALE));

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                screen.setAlpha(1);
                screen.setScaleX(1);
                screen.setScaleY(1);
            }
        });
        set.playTogether(animators);
        set.setDuration(DURATION);
        set.start();
    }
}
