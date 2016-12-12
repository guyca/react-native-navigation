package com.reactnativenavigation.screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.reactnativenavigation.utils.ViewUtils;

class AnimatorFactory {
    private static final int FADE_SHOW_DURATION = 150;
    private static final int FADE_HIDE_ALPHA_DURATION = 150;
    private static final int SLIDE_DOWN_SHOW_ALPHA_DURATION = 200;
    private static final int SLIDE_DOWN_SHOW_TRANSLATION_DURATION = 280;
    private static final int SLIDE_DOWN_HIDE_ALPHA_DELAY = 100;
    private static final int SLIDE_DOWN_HIDE_TRANSLATION_DURATION = 250;
    private static final int SLIDE_DOWN_HIDE_ALPHA_DURATION = 150;

    private Screen screen;
    private final float translationY;
    private final DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private LinearInterpolator linearInterpolator = new LinearInterpolator();
    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    AnimatorFactory(Screen screen) {
        this.screen = screen;
        translationY = 0.08f * ViewUtils.getScreenHeight();
    }

    AnimatorSet createShowAnimator(final Runnable onAnimationEnd) {
        switch (screen.styleParams.screenAnimationType) {
            case Fade:
                return createFadeShowAnimatorSet(onAnimationEnd);
            case SlideDown:
                return createSlideDownShowAnimatorSet(onAnimationEnd);
            default:
                throw new RuntimeException("Unsupported animation");
        }
    }

    AnimatorSet createHideAnimator(final Runnable onAnimationEnd) {
        switch (screen.styleParams.screenAnimationType) {
            case Fade:
                return createFadeHideAnimatorSet(onAnimationEnd);
            case SlideDown:
                return createSlideDownHideAnimatorSet(onAnimationEnd);
            default:
                throw new RuntimeException("Unsupported animation");
        }
    }

    private AnimatorSet createFadeShowAnimatorSet(final Runnable onAnimationEnd) {
        return createAnimatorSet(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                screen.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        }, createAlphaAnimator(linearInterpolator, 0, FADE_SHOW_DURATION, 0, 1));
    }

    private AnimatorSet createSlideDownShowAnimatorSet(final Runnable onAnimationEnd) {
        ObjectAnimator translation = createTranslationAnimator(decelerateInterpolator, SLIDE_DOWN_SHOW_TRANSLATION_DURATION, translationY, 0);
        ObjectAnimator alpha = createAlphaAnimator(decelerateInterpolator, 0, SLIDE_DOWN_SHOW_ALPHA_DURATION, 0, 1);
        return createAnimatorSet(new AnimatorListenerAdapter() {
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
        }, translation, alpha);
    }

    private AnimatorSet createSlideDownHideAnimatorSet(final Runnable onAnimationEnd) {
        ObjectAnimator alpha = createAlphaAnimator(linearInterpolator, SLIDE_DOWN_HIDE_ALPHA_DELAY, SLIDE_DOWN_HIDE_ALPHA_DURATION, 0);
        ObjectAnimator translation = createTranslationAnimator(accelerateInterpolator, SLIDE_DOWN_HIDE_TRANSLATION_DURATION, translationY);
        return createAnimatorSet(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        }, alpha, translation);
    }

    private ObjectAnimator createTranslationAnimator(Interpolator interpolator, int duration, float... values) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(screen, View.TRANSLATION_Y, values);
        translation.setInterpolator(interpolator);
        translation.setDuration(duration);
        return translation;
    }

    private AnimatorSet createFadeHideAnimatorSet(final Runnable onAnimationEnd) {
        return createAnimatorSet(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        }, createAlphaAnimator(new LinearInterpolator(), 0, FADE_HIDE_ALPHA_DURATION, 0));
    }

    private AnimatorSet createAnimatorSet(Animator.AnimatorListener listener, Animator... animators) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.addListener(listener);
        return set;
    }

    private ObjectAnimator createAlphaAnimator(Interpolator interpolator, int delay, int duration, float... values) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(screen, View.ALPHA, values);
        alpha.setInterpolator(interpolator);
        alpha.setStartDelay(delay);
        alpha.setDuration(duration);
        return alpha;
    }
}
