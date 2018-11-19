package com.hordiienko.slotmachine.utils;

import android.animation.ArgbEvaluator;

public class ColorAnimator extends ArgbEvaluator {
    private int startColor;
    private int endColor;
    private float step;

    private float colorFraction;
    private float currentStep;

    public ColorAnimator(int startColor, int endColor, float step) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.step = step;
    }

    public void reset() {
        colorFraction = 0;
        currentStep = step;
    }

    public int nextColor() {
        int color = (int) evaluate(colorFraction, startColor, endColor);

        colorFraction += currentStep;

        if (colorFraction > 1) {
            colorFraction = 1;
            currentStep = -step;
        } else if (colorFraction < 0) {
            colorFraction = 0;
            currentStep = step;
        }

        return color;
    }
}
