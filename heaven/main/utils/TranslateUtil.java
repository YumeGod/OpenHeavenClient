/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.utils;

import heaven.main.ui.gui.clickgui.AnimationUtil;

public class TranslateUtil {
    private float x;
    private float y;
    private long lastMS;

    public TranslateUtil(float x, float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetX, float targetY, float smoothing) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        int deltaX = (int)(Math.abs(targetX - this.x) * smoothing);
        int deltaY = (int)(Math.abs(targetY - this.y) * smoothing);
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, deltaX);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, deltaY);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

