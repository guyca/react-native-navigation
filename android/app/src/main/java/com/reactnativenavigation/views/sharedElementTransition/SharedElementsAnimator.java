package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class SharedElementsAnimator {
    private final SharedElements sharedElements;
    private final Runnable onAnimationEnd;
    private List<Animator> animators = new ArrayList<>();

    public SharedElementsAnimator(SharedElements sharedElements, Runnable onAnimationEnd) {
        this.sharedElements = sharedElements;
        this.onAnimationEnd = onAnimationEnd;
        createTransitionAnimators();
    }

    private void createTransitionAnimators() {
        for (String key : sharedElements.toElements.keySet()) {
            animators.addAll(new SharedElementAnimatorCreator(sharedElements.getFromElement(key), sharedElements.getToElement(key)).create());
        }
    }

    public void animate() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.playTogether(animators);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (SharedElementTransition fromElement : sharedElements.getFromElements()) {
                    fromElement.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationEnd.run();
            }
        });
        animatorSet.start();
    }
}
