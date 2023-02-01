/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.ui.tenacity.animations.impl;

import heaven.main.ui.tenacity.animations.Animation;
import heaven.main.ui.tenacity.animations.Direction;

public class DecelerateAnimation
extends Animation {
    public DecelerateAnimation(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DecelerateAnimation(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        double x1 = x / (double)this.duration;
        return 1.0 - (x1 - 1.0) * (x1 - 1.0);
    }
}

