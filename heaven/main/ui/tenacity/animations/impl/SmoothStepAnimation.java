/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.tenacity.animations.impl;

import heaven.main.ui.tenacity.animations.Animation;
import heaven.main.ui.tenacity.animations.Direction;

public class SmoothStepAnimation
extends Animation {
    public SmoothStepAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public SmoothStepAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        double x1 = x / (double)this.duration;
        return -2.0 * Math.pow(x1, 3.0) + 3.0 * Math.pow(x1, 2.0);
    }
}

