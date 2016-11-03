package com.reactnativenavigation.views.collapsingToolbar;

public class TitleBarCollapseCalculator extends CollapseCalculator {

    public TitleBarCollapseCalculator(final CollapsingView collapsingView) {
        super(collapsingView);
    }

    @Override
    protected boolean calculateCanCollapse(float currentTopBarTranslation, float finalExpendedTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation > -finalCollapsedTranslation &&
               currentTopBarTranslation <= finalExpendedTranslation;
    }

    @Override
    protected boolean calculateCanExpend(float currentTopBarTranslation, float finalExpendedTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation < finalCollapsedTranslation &&
                    currentTopBarTranslation < finalExpendedTranslation;
    }

    @Override
    protected float calculateCollapse(float y) {
        float translation = y - previousCollapseY + view.getCurrentCollapseValue();
        if (translation < -view.getFinalCollapseValue()) {
            translation = -view.getFinalCollapseValue();
        }
        final float expendedTranslation = 0;
        if (translation > expendedTranslation) {
            translation = expendedTranslation;
        }
        return translation;
    }

    @Override
    protected boolean isCollapsed(float currentTopBarTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation == -finalCollapsedTranslation;
    }
}
